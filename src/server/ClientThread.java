package server;

import instruction.ConnectedToLounge;
import instruction.Disconnection;
import instruction.GetMap;
import instruction.Instruction;
import instruction.JoinLounge;
import instruction.MapSent;
import instruction.ShareModel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import model.Model;

public class ClientThread implements Runnable {

	private Socket _socketClient;
	private Thread _threadClient;
	private Server _server;
	
	private boolean _stop = false;
	
	public ClientThread(Socket socket, Server server) {
		this._socketClient = socket;
		this._server = server;
		
		_threadClient = new Thread(this);
		_threadClient.start();
	}

	@Override
	public void run() {
		System.out.println("Auto : Player connected.");
		
		Instruction instruction = null;
		while(!_stop)
		{
			instruction = this.listenTo();
			
			if(instruction instanceof GetMap)
			{
				this.sendInstruction(new MapSent(this._server.generateMap()));
			}
			else if(instruction instanceof Disconnection)
			{
				this._stop = true;
			}
			else if(instruction instanceof JoinLounge)
			{
				Instruction response = this._server.transfertTo(this, ((JoinLounge)instruction));
				if(response instanceof ConnectedToLounge)
					this._stop = true;
					
				this.sendInstruction(response);
			}
		}
	}
	
	public Socket getSocketClient() {
		return _socketClient;
	}
	
	/**
	 * Send the object to the client connected on the lounge.
	 * @param instruction {@link Model}
	 */
	synchronized public void sendInstruction(Instruction instruction) {
		
		ObjectOutputStream output;
		try {
			output = new ObjectOutputStream(this._socketClient.getOutputStream());
			output.flush(); // Clean the buffer
			output.writeObject(instruction);
			//output.close();
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
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return instruction;
	}
	
	public void reinitialize() {
		_stop = false;
		this._threadClient.run();
	}
	
}
