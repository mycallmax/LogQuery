import java.io.*;
import java.net.*;
import java.util.*;
class LogQueryClient {
	private Socket sock;
	private ArrayList< ServerProperty > serverList =
		new ArrayList< ServerProperty>();

	public void addServer(ServerProperty server) {
		serverList.add(server);
	}
	
	public void connect(String[] args) {
		ArrayList< LogQueryClientThread > worker_list =
			new ArrayList< LogQueryClientThread >();
		for (ServerProperty server : serverList) {
			LogQueryClientThread worker =
				new LogQueryClientThread(server, Arrays.asList(args), this);
			worker_list.add(worker);
			worker.start();
		}
		for (LogQueryClientThread worker : worker_list) {
			try {
				worker.join();
				printResult(worker.getServer(), worker.getResult());
			} catch(InterruptedException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	private void printResult(ServerProperty server, List< String > result) {
		if (result == null) {
			return;
		}
		System.out.println(
			"Result from the Server " + server.ip + ":" + server.port + ":");
		for (String entry : result) {
			System.out.println(entry);
		}
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage: java LogQueryClient <grep_arg1> <grep_arg2> ... <grep_argN>");
			System.exit(-1);
		}
		LogQueryClient client = new LogQueryClient();
		client.addServer(new ServerProperty("localhost", 1111));
		client.addServer(new ServerProperty("localhost", 1122));
		client.addServer(new ServerProperty("localhost", 1133));
		client.addServer(new ServerProperty("localhost", 1144));
		client.connect(args);
	}
}
