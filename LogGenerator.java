// imports
import java.io.*;
import java.util.*;
import java.util.logging.*;

public class LogGenerator 
{
	//private data members
	private final static Logger LOGGER = Logger.getLogger(LogGenerator.class.getName());
	//Log File name
	private String FileName; 
	// File size in MB
	private int    FileSize;
	private FileHandler fileHandler;
	// Pattern types include "uniform" , "frequent" and "rare"
	private String PatternType;
	// Total log messages logged already
	private int TotalMessages = 0;
	// indicates if the logging should continue or not
	private boolean runStatus = true;

	// class functions
	// Init class members 
	LogGenerator(String FileName, int FileSize, String PatternType)
	{
		this.FileName = "logs/" + FileName + "_" + FileSize + "MB_" + PatternType;
		this.FileSize = FileSize;
		this.PatternType = PatternType;
	}

	// Update the run Status
	private void updateRunStatus()
	{
		File file = new File(FileName);
		// Get the number of bytes in the file
		long length = file.length();
		if(length > (FileSize * 1024 * 1024) )
			runStatus = false;
	}

	// generates the logfile with "frequent" pattern
	// generates the INFO message 98% of times
	private void generateLogFrequent()
	{
		int num1;  int num2;
		Random gen1 = new Random(System.currentTimeMillis());
		Random gen2 = new Random(System.currentTimeMillis());
		while(runStatus)
		{
			num1 = gen1.nextInt(100);
			if(num1 > 1)
			{
				LOGGER.info("This is informational");
			}
			else
			{
				num2 = gen2.nextInt(4);
				switch(num2)
				{
					case 0: 
						LOGGER.severe("Attention required.");
						break;
					case 1:
						LOGGER.warning("Do this carefully.");
						break;
					case 2:
						LOGGER.fine("you are good.");
						break;
					case 3:
						LOGGER.finest("Very delicate.");
						break;
					default:
						;
				}
				TotalMessages = TotalMessages + 1;
				if(TotalMessages % 5 == 0)
					updateRunStatus();
			}
		}
	}

	// generates the logfile with "rare" pattern
	// generates the SEVERE message 0.1% of times
	private void generateLogRare()
	{
		int num1; int num2;
		Random gen1 = new Random(System.currentTimeMillis());
		Random gen2 = new Random(System.currentTimeMillis());
		while(runStatus)
		{
			num1 = gen1.nextInt(1000);
			if(num1 == 0)
			{
				LOGGER.severe("Attention required.");
			}
			else
			{
				num2 = gen2.nextInt(4);
				switch(num2)
				{
					case 0:
						LOGGER.info("This is informational");
						break;
					case 1:
						LOGGER.warning("Do this carefully.");
						break;
					case 2:
						LOGGER.fine("you are good.");
						break;
					case 3:
						LOGGER.finest("Very delicate.");
						break;
					default:
						;
				}
				TotalMessages = TotalMessages + 1;
				if(TotalMessages % 5 == 0)
					updateRunStatus();
			}
		}
	}

	// generates a logfile with uniform distribution
	private void generateLogUniform()
	{
		int num1;
		Random gen1 = new Random(System.currentTimeMillis());
		while(runStatus)
		{
			num1 = gen1.nextInt(5);
			switch(num1)
			{
				case 0:
					LOGGER.info("This is informational");
					break;
				case 1:
					LOGGER.warning("Do this carefully.");
					break;
				case 2:
					LOGGER.fine("you are good.");
					break;
				case 3:
					LOGGER.finest("Very delicate.");
					break;
				case 4:
					LOGGER.severe("Attention required.");
					break;
				default:
					;
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
		fileHandler.setFormatter(new SimpleFormatter());
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
		if(PatternType.equals("frequent"))
			generateLogFrequent();
		else if (PatternType.equals("rare"))
			generateLogRare();
		else
			generateLogUniform();
	}

	// Main Funtion
	public static void main(String[] args)
	{
		LogGenerator MyLogGenerator = new LogGenerator(args[0],Integer.parseInt(args[1]),args[2]);
		MyLogGenerator.generate();
	}
} 
