import java.net.*;
import java.io.*;
import java.util.*;
class LogQueryServerThread extends Thread {
	private Socket clientSock;
	private String logFilePath;

	LogQueryServerThread(Socket client_sock, String log_file_path) {
		if (client_sock == null) {
			System.out.println("client_sock can not be null.");
			System.exit(-1);
		}
		this.clientSock = client_sock;
		this.logFilePath = log_file_path;
	}

	private void exceptionHandler(Exception ex) {
		System.out.println(ex.getMessage());
	}	

	private void grep(List< String > args,ObjectInputStream input,ObjectOutputStream output) {
		Runtime runtime = Runtime.getRuntime();
		Process process = null;
		ArrayList< String > result_list = new ArrayList< String >();
		try {
      long size = 0;
			LinkedList< String > cmd_array = new LinkedList< String >(args);
			cmd_array.add(logFilePath);
			cmd_array.add(0, "grep");
			process = runtime.exec(cmd_array.toArray(new String[cmd_array.size()]));
			BufferedReader result =
				new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while((line = result.readLine()) != null) {
			  // Send result in chunks of 4MB
        size = size + line.length(); 
				result_list.add(line);
				if(size > 4194304)
				{
				  output.writeObject(result_list);
          output.flush();
          output.writeObject("wait");
					output.flush();
					size = 0;
				  result_list.clear();
			  }  	
			}
			output.writeObject(result_list);
      output.flush();
      output.writeObject("done"); 
      output.flush();
			process.waitFor();
		} catch(InterruptedException ex) {
			exceptionHandler(ex);
		} catch(IOException ex) {
			exceptionHandler(ex);
		}
	}

	public void run() {
		try {
      ObjectOutputStream output = new ObjectOutputStream(clientSock.getOutputStream());	
			ObjectInputStream input = new ObjectInputStream(clientSock.getInputStream());
      List< String > args = (List< String >) input.readObject();
			System.out.println("Received request: " + args.toString());
      grep(args,input,output);
      input.close();
			output.close();
			clientSock.close();
		} catch(IOException ex) {
			exceptionHandler(ex);
		} catch(ClassNotFoundException ex) {
			exceptionHandler(ex);
		}
	}
}
