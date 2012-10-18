package server;

import java.io.Serializable;
import java.net.Socket;

public class PlayerThread implements Runnable, Serializable {

	private Lounge _lounge = null;
	private Socket _socketClient;
	private Thread _threadClient;
	
	public PlayerThread(Socket socket, Lounge lounge) {
		this(socket);
		this._lounge = lounge;
		this._lounge.addPlayer(this);
	}
	
	public PlayerThread(Socket socket) {
		this._socketClient = socket;
		
		_threadClient = new Thread(this);
		_threadClient.start();
	}

	@Override
	public void run() {
		System.out.println("Lounge " + this._lounge.getNameLounge() + " : Player connected.");
		System.out.print("> ");
	}
	
	public Socket getSocketClient() {
		return _socketClient;
	}
	
	public void setLounge(Lounge _lounge) {
		this._lounge = _lounge;
	}
	
}
