import java.io.*;
import java.net.*;
class LogQueryServer {
	private int port;
	private ServerSocket sock;

	LogQueryServer(int port) {
		this.port = port;
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
			LogQueryServerThread worker = new LogQueryServerThread(client_sock);
			worker.start();
		}
	}

	private void exceptionHandler(Exception ex) {
		System.out.println(ex.getMessage());
	}

	public static void main(String[] args) {
		LogQueryServer server = new LogQueryServer(Integer.parseInt(args[0]));
		server.start();
	}
}
