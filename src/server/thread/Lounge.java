package server.thread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import models.Game;
import server.LoggerType;
import server.NetworkGamePreparator;
import server.Server;
import server.instructions.ClientHasQuit;
import server.instructions.DefineYourPlayer;
import server.instructions.EndedGame;
import server.instructions.GameStarted;
import server.instructions.Instruction;
import server.instructions.ShareModel;
import server.instructions.YourTurn;
import server.view.Logger;

/**
 * The Lounge class manages a game and the players.
 * 
 * @author Adrien Saunier
 * @version 0.3
 */
public class Lounge implements Runnable, Serializable{

	// ------------ ATTRIBUTES ------------ //
	
	private static final long serialVersionUID = 8850009785041490126L;
	
	private int _slots;
	
	private boolean _stopGame = false;
	private boolean _stop = false;
	
	private Thread _loungeThread;
	private ArrayList<ClientThread> _playersList = new ArrayList<ClientThread>();
	private Server _server;
	
	private Game _model;
	
	private String _passwordLounge = null;
	private String _nameLounge = null;
	
	// ------------ CONSTRUCTORS ------------ //
	
	/**
	 * Create a lounge with a number of available slots and the server.
	 * 
	 * @param nbSlots int
	 * @param serv {@link Server}
	 */
	public Lounge(int nbSlots, Server serv) {
		this._slots = nbSlots;
		this._server = serv;
		
		this._loungeThread = new Thread(this);
		this._loungeThread.start();
	}
	
	// ------------ METHODS ------------ //
	
	/**
	 * The lounge initializes the game and waits for players.
	 * Then, it distributes the turn's token between players.
	 */
	@Override
	public void run() {
		
		while(!this._stop)
		{			
			this._model = new Game();
			this._stopGame = false;
			this._playersList.clear();
			
			while(!this._stopGame)
			{
				IPServerBroadcaster broadcaster = new IPServerBroadcaster(_server, this._nameLounge);
				this.waitForBeginning();
				
				new NetworkGamePreparator(this._model, this).execute();
				this.distributeRole();
				this._model.startGame();

				// Player list is randomly sort.
				Collections.shuffle(_playersList);
				
				// Initialize the model for all clients.
				this.sendAll(new GameStarted(_model));
				Logger.getInstance().addEntry("The lounge " + this._nameLounge + " started a game.");
				
				Instruction instruction = null;
				
				while(!this._model.gameIsOver()) {
					
					/*
					 * A turn
					 */
					for (int i = 0; i < this._playersList.size(); i++) {
						
						this.sendTo(_playersList.get(i), new YourTurn());
						
						instruction = this.listenTo(_playersList.get(i));
						
						if(instruction instanceof ShareModel)
						{
							ShareModel m = (ShareModel) instruction;
							_model = m.getModel();
							
							this.sendAll(new ShareModel(_model));
						}
						else
						{
							System.out.println("Wrong instruction.");
						}						
					}
				}
				
				this.closeGame();
				Logger.getInstance().addEntry("The lounge " + this._nameLounge + " has terminated his game.");
			}
		}
		
		System.out.println("Lounge ended");
		Logger.getInstance().addEntry("The lounge " + this._nameLounge + " has been closed.");
	}
	
	/**
	 * Close the lounge and send and {@link EndedGame} instruction.
	 */
	private void closeGame() {
		this.sendAll(new EndedGame());
		this._server.bringBackClient(this._playersList);
		this._stopGame = true;
	}
	
	/**
	 * Running until there is no free places.
	 */
	private void waitForBeginning() {
		while(this.getFreePlaces() != 0);
	}
	
	private void distributeRole() {
		int i = 0;
		for (ClientThread c : this._playersList) {
			c.sendInstruction(new DefineYourPlayer(this._model.getPlayers()[i]));
			i++;
		}
	}
	
	/**
	 * Listen to a {@link ClientThread} to catch the {@link Instruction} 
	 * @param clientThread {@link ClientThread}
	 * @return {@link Instruction} Instruction sent by a client 
	 */
	synchronized public Instruction listenTo(ClientThread clientThread) {
		ObjectInputStream input;
		Instruction instruction = null;
		try {
			input = new ObjectInputStream(clientThread.getSocketClient().getInputStream());
			instruction = (Instruction) input.readObject();
		} catch (IOException e) {
			this.removeDisconnectedClient(clientThread);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return instruction;
	}
	
	/**
	 * Add a player to the list.
	 * @param c {@link ClientThread}
	 */
	synchronized public boolean addPlayer(ClientThread c) {
		
		boolean ret = false;
		
		if((this._slots - this._playersList.size()) > 0)
		{
			this._playersList.add(c);
			ret = true;
		}
		
		return ret;
	}
	
	/**
	 * Send the object to all  {@link ClientThread} connected on the lounge.
	 * @param instruction {@link Instruction}
	 */
	synchronized public void sendAll(Instruction instruction)
	{
		for (ClientThread tmp : _playersList) {
			this.sendTo(tmp, instruction);
		}
	}
	
	/**
	 * Send the object to the client connected on the lounge.
	 * @param client {@link ClientThread}
	 * @param instruction
	 */
	synchronized public void sendTo(ClientThread client, Instruction instruction) {
		
		ObjectOutputStream output;
		try {
			output = new ObjectOutputStream(client.getSocketClient().getOutputStream());
			output.flush(); // Clean the buffer
			output.writeObject(instruction);
		} catch (IOException e) {
			this.removeDisconnectedClient(client);
			Logger.getInstance().addEntry(LoggerType.WARNING,"The lounge " + this._nameLounge + " is unable to send an Instruction to a client.");
		}
	}
	
	/**
	 * Remove a {@link ClientThread} if he is disconnected.
	 * If it remains just one player, the game is ended.
	 * 
	 * @param client {@link ClientThread}
	 */
	synchronized private void removeDisconnectedClient(ClientThread client) {
		
		if(this._playersList.remove(client))
		{
			this.sendAll(new ClientHasQuit());
			if(this._playersList.size() <= 1)
			{
				this.closeGame();
			}
		}
		
	}

	/**
	 * Close the running lounge.
	 */
	public void closeLounge() {
		this._stop = true;
	}
	
	/**
	 * Return true if the password sent by the client match with the official one.
	 * TODO In the next version, the password will be encrypted.
	 * 
	 * @param password
	 * @return boolean
	 */
	public boolean checkPassword(String password) {
		return (this._passwordLounge == null ||(this._passwordLounge != null && this._passwordLounge.equals(password)));
	}
	
	// ------------ GETTERS ------------ //

	/**
	 * @return  This function returns the count of how many slot are empty on the server.<br />
	 * There are an limited number of slot for each game.
	 */
	synchronized public int getFreePlaces() {
		return this._slots - this._playersList.size();
	}
	
	public int getTotalPlaces() {
		return this._slots;
	}
	
	public ArrayList<ClientThread> getPlayersList() {
		return _playersList;
	}

	public boolean isProtected() {
		return (this._passwordLounge != null) ? true : false ;
	}
	
	public String getNameLounge() {
		return this._nameLounge;
	}
	
	public String getPlayersList(int i) {
		return this._playersList.get(i).toString();
	}
	
	// ------------ SETTERS ------------ //
	
	public void setNameLounge(String nameLounge) {
		this._nameLounge = nameLounge;
	}

	public void setPassword(String password) {
		this._passwordLounge = password;
	}
	
	@Override
	public String toString() {
		return (this._nameLounge != null) ? this._nameLounge : super.toString();
	}

	public ArrayList<String> getPlayersNameList() {
		ArrayList<String> list = new ArrayList<String>();
		
		for (ClientThread player : this._playersList) {
			list.add(player.toString());
		}
		
		return list;
	}


	
}