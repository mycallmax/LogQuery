// imports
import java.io.*;
import java.util.*;
import java.util.logging.*;

public class LogGenerator 
{
	//private data members
	private final static Logger LOGGER = Logger.getLogger(LogGenerator.class.getName());
	// File size in MB
	private int    FileSize;
	// Machine ID
	private int MachineID;

	private String FileName;
	File file;
	private FileHandler fileHandler;
	// Total log messages logged already
	private int TotalMessages = 0;
	// indicates if the logging should continue or not
	private boolean runStatus = true;

	// class functions
	// Init class members 
	LogGenerator(String log_path, int FileSize, int MachineID)
	{
		this.MachineID = MachineID;
		this.FileName = log_path + "/machine." + MachineID + ".log";
		this.FileSize = FileSize;
		file = new File(FileName);
	}

	// Update the run Status
	private void updateRunStatus()
	{
		// Get the number of bytes in the file
		long length = file.length();
		if(length > (FileSize * 1024 * 1024) )
			runStatus = false;
	}

	private void generateLog()
	{
		int num1 = 0; int num2; int SomeMachineID1 = 0; int SomeMachineID2 = 0;
		Random gen1 = new Random(System.currentTimeMillis());
		Random gen2 = new Random(System.currentTimeMillis());
		while(runStatus)
		{
			num2 = gen2.nextInt(4) + 1;
			while(num2 == MachineID)
			{
				num2 = gen2.nextInt(4) + 1;
			}
			if(num2 > MachineID)
			{
				SomeMachineID1 = MachineID;
				SomeMachineID2 = num2;
			}
			else
			{
				SomeMachineID1 = num2;
				SomeMachineID2 = MachineID;
			}			
			num1 = gen1.nextInt(100);
			if(num1 >= 30)
			{
				// Log frequent messages
				if(num1%2 == 0)
					LOGGER.info("This is informational.  MachineID:" + MachineID + " SomeMachineIDs: " + SomeMachineID1 + "," + SomeMachineID2);
				else
					LOGGER.warning("This is warning. MachineID: " + MachineID + "  SomeMachineIDs: " + SomeMachineID1 + "," + SomeMachineID2);
			}
			else if (num1 >= 5 && num1 < 30)
			{
				// Log somewhat frequent messages
				if(num1%2 == 0)
					LOGGER.fine("This is fine.  MachineID: " + MachineID + "  SomeMachineIDs: " + SomeMachineID1 + "," + SomeMachineID2);
				else
					LOGGER.finest("This is finest. MachineID: "+  MachineID + "  SomeMachineIDs: " + SomeMachineID1 + "," + SomeMachineID2);
			}
			else
			{
				// Log rare message
				LOGGER.severe("This is severe. MachineID: " + MachineID + "  SomeMachineIDs: " + SomeMachineID1 + "," + SomeMachineID2);
			}

			TotalMessages = TotalMessages + 1;
			if(TotalMessages % 5 == 0)
				updateRunStatus();
		}
	}

	// Set FileHandler attributes
	private void SetFileHandler()
	{
		try {
			fileHandler = new FileHandler(FileName);
		} catch (IOException ex) {
			LOGGER.log(Level.SEVERE, null, ex);
		} catch (SecurityException ex) {
			LOGGER.log(Level.SEVERE, null, ex);
		}
		// Set the formatting
		fileHandler.setFormatter(new MyLogFormatter());
		LOGGER.addHandler(fileHandler);
		// set all levels
		LOGGER.setLevel(Level.ALL);
		// turn off console logging
		LOGGER.setUseParentHandlers(false);
	}

	// Common generate function
	private void generate()
	{
		SetFileHandler();
		generateLog();
	}

	// Main Funtion
	public static void main(String[] args)
	{
	 if (args.length < 3) {
      System.out.println("Usage: java LogGenerator <log_path> <filesize> <MachineID>");
      System.exit(-1);
    }

		LogGenerator MyLogGenerator = new LogGenerator(args[0], Integer.parseInt(args[1]),Integer.parseInt(args[2]));
		MyLogGenerator.generate();
	}
} 
