package server;

import instruction.EndedGame;
import instruction.Instruction;
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
	
	private int _serverPort;
	private Thread _loungeThread;
	private ServerSocket _loungeSocket;
	private ArrayList<PlayerThread> _playersList = new ArrayList<PlayerThread>();
	
	private Model _model = new Model();
	
	private String _passwordLounge = null;
	private String _nameLounge = null;
	
	public Lounge(int nbSlots) {
		this._slots = nbSlots;
		
		try {
			this._loungeSocket = new ServerSocket(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this._serverPort = this._loungeSocket.getLocalPort();
		
		this._loungeThread = new Thread(this);
		this._loungeThread.start();
	}
	
	@Override
	public void run() {
		
		System.out.println("Ouverture d'un salon sur le port : " + this._serverPort);
		
		while(!this._stop)
		{			
			this._model = new Model();
			this._stopGame = false;
			this._playersList.clear();
			
			while(!this._stopGame)
			{
				/*
				 * Waiting for the players.
				 * The game starts when the lounge is full.
				 */
				while(_playersList.size() < _slots)
				{
					
					try {
						new PlayerThread(_loungeSocket.accept(), this);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				/*
				 * Player list is randomly sort.
				 */
				Collections.shuffle(_playersList);
				
				/*
				 * Initialize the model for all clients.
				 */
				this.sendAll(new ShareModel(_model));
				
				for (int j = 0; j < 3; j++) {
					
					/*
					 * A turn
					 */
					for (int i = 0; i < _slots; i++) {
						
						this.sendTo(_playersList.get(i), new YourTurn());
						
						Instruction instruction = this.listenTo(_playersList.get(i));				
						ShareModel m = (ShareModel) instruction;
						_model = m.getModel();
						
						this.sendAll(new ShareModel(_model));
						
					}
				}
				
				this.sendAll(new EndedGame());
				this._stopGame = true;
			}
		}
		
		System.out.println("Lounge ended");
	}
	
	/**
	 * Listen to a {@link PlayerThread} to catch the {@link Instruction} 
	 * @param client
	 * @return {@link Instruction} Instruction sent by a client 
	 */
	synchronized public Instruction listenTo(PlayerThread client) {
		ObjectInputStream input;
		Instruction instruction = null;
		try {
			input = new ObjectInputStream(client.getSocketClient().getInputStream());
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
	 * @param player {@link PlayerThread}
	 */
	synchronized public void addPlayer(PlayerThread c) {
		this._playersList.add(c);
	}
	
	/**
	 * Send the object to all  {@link PlayerThread} connected on the lounge.
	 * @param instruction {@link Instruction}
	 */
	synchronized public void sendAll(Instruction instruction)
	{
		for (PlayerThread tmp : _playersList) {
			this.sendTo(tmp, instruction);
		}
	}
	
	/**
	 * Send the object to the client connected on the lounge.
	 * @param client {@link PlayerThread}
	 * @param instruction {@link Model}
	 */
	synchronized public void sendTo(PlayerThread client, Instruction instruction) {
		
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
	 * @return  This function returns the count of how many slot are empty on the server.<br />
	 * There are an limited number of slot for each game.
	 */
	synchronized public int getFreePlaces() {
		return this._slots - this._playersList.size();
	}
	
	public ArrayList<PlayerThread> getPlayersList() {
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
	
	public int getServerPort() {
		return this._serverPort;
	}
	
	public void setNameLounge(String nameLounge) {
		this._nameLounge = nameLounge;
	}
	
	@Override
	public String toString() {
		return (this._nameLounge != null) ? this._nameLounge : super.toString();
	}
	
}