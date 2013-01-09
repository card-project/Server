package server.thread;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import server.LoggerType;
import server.Server;
import server.view.Logger;

public class IPServerBroadcaster implements Runnable{

	// ------------ ATTRIBUTES ------------ //
	
	private Thread _broadcaster;
	private int _BROADCAST_PORT = 4446;
	private DatagramSocket _socket = null;
	private boolean _runBroadcaster = true;
	private String _virtualIP = new String("230.0.0.1");	// This IP Address has to be chosen
															// between 224.0.1.0 and 239.255.255.255 
	private Server _server = null;
	
	private String _loungeName;
	
	// ------------ CONSTRUCTORS ------------ //
	
	public IPServerBroadcaster(Server server, String loungeName) {
		
		this._server = server;
		this._loungeName = loungeName;
		
		Logger.getInstance().addEntry("Opening the broadcaster.");
		
		try {
			this._socket = new DatagramSocket();
		} catch (SocketException e) {
			Logger.getInstance().addEntry(LoggerType.CRITICAL, "Unable to open the broadcaster on port " + this._BROADCAST_PORT + ".");
			e.printStackTrace();
		}
		
		this._broadcaster = new Thread(this);
		this._broadcaster.start();
	}
	
	// ------------ METHODS ------------ //
	
	@Override
	public void run() {
	
		while (this.isRunningBroadcaster()) {
			
			try {
				
				byte[] buffer = new String(this._server.getLocalAdress() + ":" + this._server.getLocalPort() + ":" + this._loungeName).getBytes();
				
				InetAddress groupAdress = InetAddress.getByName(this.getVirtualIP());
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length, groupAdress, this._BROADCAST_PORT);
				
				this._socket.send(packet);
								
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					Logger.getInstance().addEntry(LoggerType.WARNING, "Unable to wait before broadcast again.");
					e.printStackTrace();
				}
				
			} catch (UnknownHostException e) {
				Logger.getInstance().addEntry(LoggerType.CRITICAL,"No address found for the broadcaster.");
				this.setRunBroadcaster(false);
			} catch (IOException e) {
				Logger.getInstance().addEntry(LoggerType.CRITICAL, "Unable to broadcast the server's address.");
				this.setRunBroadcaster(false);
			}
		}
	}
	
	// ------------ GETTERS ------------ //
	
	public boolean isRunningBroadcaster() {
		return _runBroadcaster;
	}
	
	public String getVirtualIP() {
		return _virtualIP;
	}
	
	// ------------ SETTERS ------------ //
	
	public void setRunBroadcaster(boolean _runBroadcaster) {
		this._runBroadcaster = _runBroadcaster;
	}
	
}
