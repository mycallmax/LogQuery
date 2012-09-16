import java.io.*;
import java.net.*;
import java.util.List;
import java.util.ArrayList;
class LogQueryClientThread extends Thread {
	private ServerProperty server = null;
	private List< String > args = null;
	private File resultFile = null;
	private ObjectOutputStream output;
	private BufferedReader input;
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
			input = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			output.writeObject(args);
			output.flush();
			resultFile = File.createTempFile(server.ip + "_" + server.port, ".log");
			BufferedWriter tmpOut = new BufferedWriter(new FileWriter(resultFile));
			String line;
			while((line = input.readLine()) != null) {
				tmpOut.write(line);
				tmpOut.newLine();
			}
			tmpOut.flush();
			tmpOut.close();
			output.close();
			input.close();
			sock.close();
		} catch(IOException ex) {
			System.out.println(
				"Failed to connect " + server.ip + ":"
				+ server.port + " : " + ex.getMessage()
			);
		}
	}

	public File getResultFile() {
		return resultFile;	
	}

	public ServerProperty getServer() {
		return server;
	}
}
