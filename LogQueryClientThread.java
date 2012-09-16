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
	LogQueryClientThread(ServerProperty server, List< String > args, LogQueryClient parent) {
		this.parent = parent;
		this.server = server;
		this.args = args;
	}

	public void run() {
		try {
			Socket sock = new Socket(server.ip, server.port);
			output = new ObjectOutputStream(sock.getOutputStream());
			input = new ObjectInputStream(sock.getInputStream());
			output.writeObject(args);
			output.flush();
			result = (List< String >) input.readObject();
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

	public List< String > getResult() {
		return result;	
	}

	public ServerProperty getServer() {
		return server;
	}
}
