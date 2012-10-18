package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Allow you to manage the server (quit, ...)
 */
public class Terminal implements Runnable {

	private Server _serv;
	private Thread _terminalThread;
	private BufferedReader _input;
	private String _instruction;
	
	public Terminal(Server server) {
		this._serv = server;
		this._input = new BufferedReader(new InputStreamReader(System.in));
		
		this._terminalThread = new Thread(this);
		this._terminalThread.start();
	}
	
	/**
	 * @return void
	 * Execute the instructions to manage the server
	 */
	@Override
	public void run() {
		
		try {
			System.out.print("> ");
			while((this._instruction = this._input.readLine()) != null)
			{
				if(this._instruction.equalsIgnoreCase("quit")) {
					
					this._serv.closeLounges();
					
					
					System.out.println("=====================");
					System.out.println("Server stopping ...");
					System.out.println("=====================");
					System.exit(0);
				}
				else if(this._instruction.equalsIgnoreCase("new")) {
					
					System.out.println("Creating a new lounge ...");
					System.out.println("How many slots available for this lounge ?");
					
					Scanner sc = new Scanner(System.in);
					
					boolean verifIntOk = false;
					int nbSlots = this._serv.getDefaultSlots();
					do
					{
						System.out.print("> ");
						try
						{
							nbSlots = sc.nextInt();
							verifIntOk = true;
						}
						catch (InputMismatchException e) {
							verifIntOk = false;
						}
					}while(!verifIntOk);
					
					System.out.println("Choose the name of the lounge ?");
					String loungeName;
					
					sc.nextLine();
					do
					{
						System.out.print("> ");
						loungeName = sc.nextLine();
					} while(loungeName.isEmpty() || this._serv.findLoungeByName(loungeName) != null);
					
					System.out.println("Is it private lounge ? (Y for Yes, N for No)");
					String priv;
					do
					{
						System.out.print("> ");
						priv = sc.nextLine();
					} while(!priv.equalsIgnoreCase("Y") && !priv.equalsIgnoreCase("N"));
					
					ArrayList<Lounge> list = this._serv.getLoungeList();
					Lounge lounge = new Lounge(nbSlots);
					 
					if(priv.equalsIgnoreCase("Y"))
					{
						System.out.println("What is the lounge's password ?");
						
						String password;
						do
						{
							System.out.print("> ");
							password = sc.nextLine();
						}
						while(password.isEmpty());
						lounge.setPassword(password);
					}
					
					lounge.setNameLounge(loungeName);
					list.add(lounge);
				}
				else if(this._instruction.equalsIgnoreCase("free")) {
					
					int totalFreeSpaces = 0;
					for (Lounge it : this._serv.getLoungeList()) {
						totalFreeSpaces += it.getFreePlaces();
					}
					System.out.println("There are " + totalFreeSpaces + " global free slot(s).");
					System.out.println("Write \"map\" to see a detailed view of free slots.");
				}
				else if(this._instruction.equalsIgnoreCase("map"))
				{
					if(this._serv.getLoungeList().size() >= 0)
					{
						String tmp;
						for (Lounge it : this._serv.getLoungeList()) {
							
							tmp = it+" - (" + it.getFreePlaces() + " free slot(s))";
							tmp = (it.isProtected()) ? "[Private] " + tmp : "[Public ] " + tmp;
							
							System.out.println(tmp);
							
							for (PlayerThread c : it.getPlayersList()) {
								System.out.println(" --> " + c);
							}
						}
					}
					else
						System.out.println("There are no lounges currently.");
				}
				else if(this._instruction.equalsIgnoreCase("help")) {
					System.out.println("=====================");
					System.out.println("Free : Count of free places on the server.");
					System.out.println("Help : Show the list of available instructions.");
					System.out.println("Quit : Stop the server.");
					System.out.println("Map  : List of available lounges.");
					System.out.println("=====================");
				}
			
				System.out.flush();
				System.out.print("> ");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
