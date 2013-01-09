package server.thread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import server.Server;
import server.instructions.ConnectedToLounge;
import server.instructions.Disconnection;
import server.instructions.Instruction;
import server.instructions.JoinLounge;

/**
 * Manage the client's socket and his instructions;
 * 
 * @author Adrien Saunier
 * @version 0.3
 */
public class ClientThread implements Runnable {

	// ------------ ATTRIBUTES ------------ //
	
	private Socket _socketClient;
	private Thread _threadClient;
	private Server _server;
	
	private boolean _stop = false;
	
	// ------------ CONSTRUCTORS ------------ //
	
	/**
	 * Initialize a specific {@link Thread} for a client.
	 * 
	 * @param socket {@link Socket}
	 * @param server {@link Server}
	 */
	public ClientThread(Socket socket, Server server) {
		this._socketClient = socket;
		this._server = server;
		
		_threadClient = new Thread(this);
		_threadClient.start();
	}

	// ------------ METHODS ------------ //
	
	/**
	 * Wait for instructions from the socket.
	 */
	@Override
	public void run() {
		
		Instruction instruction = null;
		while(!_stop)
		{
			instruction = this.listenTo();
			
			if(instruction instanceof Disconnection) {
				this._stop = true;
			}
			else if(instruction instanceof JoinLounge) {				
				Instruction response = this._server.transferTo(this, ((JoinLounge)instruction));
				if(response instanceof ConnectedToLounge)
					this._stop = true;
					
				this.sendInstruction(response);
			}
		}
	}
	
	// ------------ METHODS ------------ //
	
	/**
	 * Send the object to the client connected on the lounge.
	 * @param instruction {@link Instruction}
	 */
	synchronized public void sendInstruction(Instruction instruction) {
		
		ObjectOutputStream output;
		try {
			output = new ObjectOutputStream(this._socketClient.getOutputStream());
			output.flush(); // Clean the buffer
			output.writeObject(instruction);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Listen to the client to catch the {@link Instruction}
	 * @return {@link Instruction} Instruction sent by a client 
	 */
	synchronized public Instruction listenTo() {
		
		ObjectInputStream input;
		Instruction instruction = null;
		try {
			input = new ObjectInputStream(this._socketClient.getInputStream());
			instruction = (Instruction) input.readObject();
		} catch (IOException e) {
			this._server.removeDisconnectedClient(this);
			this._stop = true;			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return instruction;
	}
	
	// ------------ GETTERS ------------ //
	
	public Socket getSocketClient() {
		return _socketClient;
	}	
}
