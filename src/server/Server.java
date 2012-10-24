package server;

import instruction.ConnectedToLounge;
import instruction.ConnexionFailedLoungeFull;
import instruction.ConnexionFailedWrongPassword;
import instruction.Instruction;
import instruction.JoinLounge;
import instruction.LoungeNoLongerExists;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

import model.Map;
import model.Model;
import model.SimplifiedLounge;

public class Server {

	// Keep the list of players
	private static ArrayList<ClientThread> _waitingPlayers = new ArrayList<ClientThread>();

	// Keep the list of lounges
	private static ArrayList<Lounge> _loungeList = new ArrayList<Lounge>();

	// Default size for the lounge
	private static int _defaultSlots = 6;

	public static void main(String[] args) {

		Integer port = new Integer(10000);
		Server server = new Server();

		/**
		 * Initialize the default lounge
		 */
		Lounge defaultLounge = new Lounge(2, server);
		defaultLounge.setNameLounge("Default");
//		defaultLounge.setPassword("Pepito");
		
		_loungeList.add(defaultLounge);

		ServerSocket socketServer = null;
		try {
			socketServer = new ServerSocket(port);
			serverInformations(socketServer);

			new Terminal(server);

			while (true) {
				try {
					_waitingPlayers.add(new ClientThread(socketServer.accept(),server));

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// Closing the main socket
				socketServer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

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
	 * @param client
	 *            {@link ClientThread}
	 * @param instruction
	 *            {@link Model}
	 */
	synchronized public void sendTo(ClientThread client, Instruction instruction) {

		ObjectOutputStream output;
		try {
			output = new ObjectOutputStream(client.getSocketClient()
					.getOutputStream());
			output.flush(); // Clean the buffer
			output.writeObject(instruction);
			// output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	synchronized public ArrayList<Lounge> getLoungeList() {
		return _loungeList;
	}

	synchronized public Lounge createLounge() {
		return this.createLounge(_defaultSlots);
	}

	synchronized public Lounge createLounge(int nbSlots) {
		Lounge lounge = new Lounge(nbSlots, this);
		_loungeList.add(lounge);
		return lounge;
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

	public void closeLounges() {
		for (Lounge l : _loungeList) {
			l.closeLounge();
		}
	}

	public static int getDefaultSlots() {
		return _defaultSlots;
	}

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
	 * Generate an object which implements {@link Serializable}. The purpose is
	 * to share an object with the client.
	 * 
	 * @return {@link Map}
	 */
	synchronized public Map generateMap() {
		Map map = new Map();
		Lounge l = null;
		Iterator<Lounge> iterator = this.getFreeLounge().iterator();
		SimplifiedLounge sl = null;
		while (iterator.hasNext()) {

			l = iterator.next();
			sl = new SimplifiedLounge(l.getNameLounge(),
					l.isProtected());
			map.getLoungeList().add(sl);

		}

		return map;
	}

	synchronized public Instruction transfertTo(ClientThread c, JoinLounge joinLounge) {

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
						result = new ConnectedToLounge();
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
	
	synchronized public void bringBackClient(ArrayList<ClientThread> list)
	{
		Iterator<ClientThread> iterator = list.iterator();
		while (iterator.hasNext()) {
			ClientThread ct = iterator.next();
			ct.reinitialize();
			_waitingPlayers.add(ct);
		}
	}
}
