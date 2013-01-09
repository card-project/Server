package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

import server.instructions.ConnectedToLounge;
import server.instructions.ConnexionFailedLoungeFull;
import server.instructions.ConnexionFailedWrongPassword;
import server.instructions.Instruction;
import server.instructions.JoinLounge;
import server.instructions.LoungeNoLongerExists;
import server.thread.ClientThread;
import server.thread.Lounge;
import server.view.Logger;
import server.view.Terminal;

/**
 * Entry point of the server for "1000Bornes" program.
 * This class manage the users and the lounges.
 * The server is, by default, running on the port 10000.
 * 
 * @author Adrien Saunier
 * @version 0.3
 *
 */
public class Server {

	// ------------ ATTRIBUTES ------------ //
	
	// Keep the list of players
	private static ArrayList<ClientThread> _waitingPlayers = new ArrayList<ClientThread>();

	// Keep the list of lounges
	private static ArrayList<Lounge> _loungeList = new ArrayList<Lounge>();

	// Default size for the lounge
	private static int _defaultSlots = 6;
	
	private static ServerSocket _socketServer = null;

	
	// ------------ MAIN ------------ //
	/**
	 * @param args
	 * 
	 * Initialize the server. It defines a default {@link Lounge} and it is waiting for connections from users.
	 * It manages the switch between the lounges and the waiting list.
	 */
	public static void main(String[] args) {
		
		Server server = new Server();

		/*
		 * Initialize the default lounge
		 */
		Lounge defaultLounge = new Lounge(2, server);
		defaultLounge.setNameLounge("Default");
		_loungeList.add(defaultLounge);
		
		
		
		try {
			_socketServer = new ServerSocket(0);
			Logger.getInstance().addDelimiter();
			Logger.getInstance().addEntry(LoggerType.INFO, "Server is running on port " + server.getLocalPort());
			
			serverInformations(_socketServer);

			new Terminal(server);

			while (true) {
				try {
					_waitingPlayers.add(new ClientThread(_socketServer.accept(),server));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// Closing the main socket
				_socketServer.close();
			} catch (IOException e) {
				Logger.getInstance().addEntry(LoggerType.CRITICAL, "Impossible to close the Server socket.");
				e.printStackTrace();
			}
		}
	}
	
	// ------------ METHODS ------------ //

	/**
	 * Display the informations of the server in the terminal;
	 * 
	 * @param server {@link ServerSocket}
	 */
	public static void serverInformations(ServerSocket server) {

		try {
			String message = new String(
					"=======================================\n"
							+ "Milles bornes : server-side\n"
							+ "Version : 0.1\n"
							+ "=======================================\n"
							+ "Port : " + server.getLocalPort()
							+ "\nIp Address : "
							+ InetAddress.getLocalHost().getHostAddress()
							+ "\n=======================================\n"
							+ "Enter \"help\" to learn available instructions.");

			System.out.println(message);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Send the object to the client connected on the lounge.
	 * 
	 * @param client {@link ClientThread}
	 * @param instruction {@link Instruction}
	 */
	synchronized public void sendTo(ClientThread client, Instruction instruction) {

		ObjectOutputStream output;
		try {
			output = new ObjectOutputStream(client.getSocketClient()
					.getOutputStream());
			output.flush(); // Clean the buffer
			output.writeObject(instruction);
			
		} catch (IOException e) {
			Logger.getInstance().addEntry(LoggerType.WARNING,"Unable to send an Instruction to a client.");
			e.printStackTrace();
		}
	}

	/**
	 * Create a lounge with the default number of available slots. 
	 * 
	 * @return {@link Lounge}
	 */
	synchronized public Lounge createLounge() {
		return this.createLounge(_defaultSlots);
	}

	/**
	 * Create a lounge with the specified number of available slots. 
	 * 
	 * @param nbSlots
	 * @return {@link Lounge}
	 */
	synchronized public Lounge createLounge(int nbSlots) {
		Lounge lounge = new Lounge(nbSlots, this);
		_loungeList.add(lounge);
		Logger.getInstance().addEntry("A new lounge has been created.");
		return lounge;
	}
	
	/**
	 * When a game is ended, this method is called.
	 * It brings back the players from the lounge to the waiting list. 
	 * 
	 * @param list {@link ArrayList} of {@link ClientThread} 
	 */
	synchronized public void bringBackClient(ArrayList<ClientThread> list)
	{
		Iterator<ClientThread> iterator = list.iterator();
		while (iterator.hasNext()) {
			ClientThread ct = iterator.next();
			_waitingPlayers.add(new ClientThread(ct.getSocketClient(), this));
		}
	}
	
	/**
	 * Remove a player if he is disconnected and add entry to the log file through the {@link Logger} class.
	 * 
	 * @param clientThread {@link ClientThread}
	 */
	synchronized public void removeDisconnectedClient(ClientThread clientThread) {
		
		if(_waitingPlayers.remove(clientThread))
			Logger.getInstance().addEntry(LoggerType.WARNING, "A player has been removed. Reason : Player unreachable.");
		
		
	}

	/**
	 * Return the free lounges.
	 * 
	 * @return {@link ArrayList}
	 */
	synchronized public ArrayList<Lounge> getFreeLounge() {
		ArrayList<Lounge> list = new ArrayList<Lounge>();

		for (Lounge l : _loungeList) {
			if (l.getFreePlaces() > 0)
				list.add(l);
		}

		return list;
	}
	
	/**
	 * Return the list of waiting players.
	 * 
	 * @return {@link ArrayList}
	 */
	public ArrayList<ClientThread> getWaitingPlayers() {
		return _waitingPlayers;
	}
	
	/**
	 * Close all the lounges.
	 */
	public void closeLounges() {
		for (Lounge l : _loungeList) {
			l.closeLounge();
		}
	}

	/**
	 * Return a {@link Lounge} if there is a lounge's name matching with the param. 
	 * 
	 * @param loungeName {@link String} 
	 * @return {@link Lounge}
	 */
	synchronized public Lounge findLoungeByName(String loungeName) {

		Iterator<Lounge> iterator = null;
		iterator = _loungeList.iterator();

		boolean stop = false;
		Lounge elem = null;

		while (iterator.hasNext() && !stop) {
			elem = iterator.next();

			if (elem.getNameLounge().equalsIgnoreCase(loungeName)) {
				stop = true;
			}
		}

		return (stop == false) ? null : elem;
	}

	/**
	 * Try to transfer a {@link ClientThread} to a {@link Lounge} and return the corresponding {@link Instruction}
	 * 
	 * @param c {@link ClientThread}
	 * @param joinLounge {@link JoinLounge}
	 * @return {@link Instruction}
	 */
	synchronized public Instruction transferTo(ClientThread c, JoinLounge joinLounge) {

		Lounge lounge = this.findLoungeByName(joinLounge.getLoungeName());

		Instruction result = null;
		
		if (lounge != null)
		{
			if(lounge.checkPassword(joinLounge.getPassword()))
			{
				if (_waitingPlayers.contains(c))
				{
					if (lounge.addPlayer(c))
					{
						result = new ConnectedToLounge(lounge.getPlayersNameList());
						_waitingPlayers.remove(c);
					}
					else
						result = new ConnexionFailedLoungeFull();
				}
				else
					System.err.println("Player unfound");					
			}
			else
				result = new ConnexionFailedWrongPassword();
			
		} else
			result = new LoungeNoLongerExists();

		return result;
	}
	
	// ------------ GETTERS ------------ //
	
	synchronized public ArrayList<Lounge> getLoungeList() {
		return _loungeList;
	}
	
	public static int getDefaultSlots() {
		return _defaultSlots;
	}
	
	public int getLocalPort() {
		return _socketServer.getLocalPort();
	}
	
	public String getLocalAdress() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostAddress();
	}
}
