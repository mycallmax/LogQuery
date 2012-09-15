import java.io.*;
import java.net.*;
class LogQueryServer {
	private int port;
	private String logFilePath;
	private ServerSocket sock;

	LogQueryServer(int port, String log_file_path) {
		this.port = port;
		this.logFilePath = log_file_path;
	}

	public void start() {
		// 1. Create the server socket
		try {
			sock = new ServerSocket(port);
		} catch(IOException ex) {
			exceptionHandler(ex);
		}

		// 2. Wait for accepting the client sockets
		Socket client_sock = null;
		while(true) {
			try {
				client_sock = sock.accept();
			} catch(IOException ex) {
				exceptionHandler(ex);
			}
			LogQueryServerThread worker =
				new LogQueryServerThread(client_sock, logFilePath);
			worker.start();
		}
	}

	private void exceptionHandler(Exception ex) {
		System.out.println(ex.getMessage());
	}

	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Usage: java LogQueryServer <port> <logfile_path>");
			System.exit(-1);
		}
		LogQueryServer server = new LogQueryServer(Integer.parseInt(args[0]), args[1]);
		server.start();
	}
}
