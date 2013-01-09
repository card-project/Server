package server.view;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import server.LoggerType;

/**
 * This class manage the log system.
 * 
 * @author Adrien Saunier
 * @version 0.3
 */
public class Logger {
	
	// ------------ ATTRIBUTES ------------ //
	
	private final String PATH = "log.txt";
	private File _file = null;

	// ------------ CONSTRUCTORS ------------ //
	
	private Logger() {
		this.init();
	}
	
	private static class LoggerHolder {
		public final static Logger INSTANCE = new Logger();
	}
	
	// ------------ METHODS ------------ //
	
	public static Logger getInstance() {
		return LoggerHolder.INSTANCE;
	}
	/**
	 * Add an entry into the log file with the "info" status.
	 * 
	 * @param log {@link String}
	 */
	public void addEntry(String log) {
		this.addEntry(LoggerType.INFO, log);
	}
	
	/**
	 * Add an entry into the log file.
	 * 
	 * @param type
	 * @param log {@link String}
	 */
	public void addEntry(LoggerType type, String log) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(this._file,true));
			
			writer.write("[" + type + "] " + log);
			writer.newLine();
			writer.flush();
			
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Add a delimiter into the log file between to log session. 
	 */
	public void addDelimiter() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(this._file,true));
			
			writer.write("========================================");
			writer.newLine();
			writer.flush();
			
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Initialize the {@link Logger} with default informations.
	 */
	private void init() {
		
		_file = new File(PATH);
		
		if(!_file.exists())
		{
			try {
				_file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
