package server;

import instruction.BackToMenu;
import instruction.EndedGame;
import instruction.Instruction;
import instruction.MapSent;
import instruction.ShareModel;
import instruction.YourTurn;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;

import model.Model;

public class Lounge implements Runnable, Serializable{

	private int _slots;
	
	private boolean _stopGame = false;
	private boolean _stop = false;
	
	private Thread _loungeThread;
	private ArrayList<ClientThread> _playersList = new ArrayList<ClientThread>();
	private Server _server;
	
	private Model _model = new Model();
	
	private String _passwordLounge = null;
	private String _nameLounge = null;
	
	public Lounge(int nbSlots, Server serv) {
		this._slots = nbSlots;
		this._server = serv;
		
		this._loungeThread = new Thread(this);
		this._loungeThread.start();
	}
	
	@Override
	public void run() {
		
		while(!this._stop)
		{			
			this._model = new Model();
			this._stopGame = false;
			this._playersList.clear();
			
			while(!this._stopGame)
			{
				this.waitForBeginning();
				System.out.println("Fin d'attente." + this.getFreePlaces());
				/*
				 * Player list is randomly sort.
				 */
				Collections.shuffle(_playersList);
				
				/*
				 * Initialize the model for all clients.
				 */
				this.sendAll(new ShareModel(_model));
//				this.sendTo(this._playersList.get(0), new YourTurn());
				
//				Instruction instruction = null;
//				instruction = this.listenTo(_playersList.get(0));
				
				
				Instruction instruction = null;
				
				for (int j = 0; j < 3; j++) {
					
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
			}
		}
		
		System.out.println("Lounge ended");
	}
	
	private void closeGame() {
		this.sendAll(new EndedGame());
		this._server.bringBackClient(this._playersList);
		this._stopGame = true;
	}
	
	private void waitForBeginning() {
		while(this.getFreePlaces() != 0);
	}
	
	/**
	 * Listen to a {@link ClientThread} to catch the {@link Instruction} 
	 * @param clientThread
	 * @return {@link Instruction} Instruction sent by a client 
	 */
	synchronized public Instruction listenTo(ClientThread clientThread) {
		ObjectInputStream input;
		Instruction instruction = null;
		try {
			input = new ObjectInputStream(clientThread.getSocketClient().getInputStream());
			instruction = (Instruction) input.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return instruction;
	}
	
	/**
	 * Add a player to the list.
	 * @param player {@link ClientThread}
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
	 * @param instruction {@link Model}
	 */
	synchronized public void sendTo(ClientThread client, Instruction instruction) {
		
		ObjectOutputStream output;
		try {
			output = new ObjectOutputStream(client.getSocketClient().getOutputStream());
			output.flush(); // Clean the buffer
			output.writeObject(instruction);
			//output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
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

	/**
	 * @return  This function returns the count of how many slot are empty on the server.<br />
	 * There are an limited number of slot for each game.
	 */
	synchronized public int getFreePlaces() {
		return this._slots - this._playersList.size();
	}
	
	public ArrayList<ClientThread> getPlayersList() {
		return _playersList;
	}

	public void setPassword(String password) {
		this._passwordLounge = password;
	}
	
	public boolean isProtected() {
		return (this._passwordLounge != null) ? true : false ;
	}
	
	public String getNameLounge() {
		return this._nameLounge;
	}
	
	public void setNameLounge(String nameLounge) {
		this._nameLounge = nameLounge;
	}
	
	@Override
	public String toString() {
		return (this._nameLounge != null) ? this._nameLounge : super.toString();
	}
	
}