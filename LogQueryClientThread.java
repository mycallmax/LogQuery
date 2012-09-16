import java.io.*;
import java.net.*;
import java.util.List;
class LogQueryClientThread extends Thread {
	private ServerProperty server = null;
	private List< String > args = null;
	private List< String > result = null;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private LogQueryClient parent;
  private Object resultQueue = new Object();
	LogQueryClientThread(ServerProperty server, List< String > args, LogQueryClient parent) {
		this.parent = parent;
		this.server = server;
		this.args = args;
	}

  private void printResult(List< String > result) {
    if (result == null) {
      return;
    }
    synchronized(resultQueue){
    System.out.println(
      "Result from the Server " + server.ip + ":" + server.port + ":");
    for (String entry : result) {
      System.out.println(entry);
    }
   }
  }

	public void run() {
		try {
			Socket sock = new Socket(server.ip, server.port);
			output = new ObjectOutputStream(sock.getOutputStream());
			input = new ObjectInputStream(sock.getInputStream());
			output.writeObject(args);
			output.flush();
		  String message = "wait";
			while(message.equals("wait"))
		  {
				result = (List< String >) input.readObject();
				printResult(result);
        message = (String)input.readObject();
		  } 
			output.close();
			input.close();
			sock.close();
		} catch(IOException ex) {
			System.out.println(
				"Failed to connect " + server.ip + ":"
				+ server.port + " : " + ex.getMessage()
			);
		} catch(ClassNotFoundException ex) {
			System.out.println(ex.getMessage());
		}
	}
}
