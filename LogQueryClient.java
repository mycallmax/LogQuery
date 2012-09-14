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
			} catch(InterruptedException ex) {
				//TODO
			}
			printResult(worker.getServer(), worker.getResult());
		}
	}

	private void printResult(ServerProperty server, List< String > result) {
		System.out.println(
			"Result from the Server " + server.ip + ":" + server.port + ":");
		for (String entry : result) {
			System.out.println(entry);
		}
	}

	public static void main(String[] args) {
		LogQueryClient client = new LogQueryClient();
		client.addServer(new ServerProperty("localhost", 1111));
		client.addServer(new ServerProperty("localhost", 1122));
		//client.addServer(new ServerProperty("localhost", 1133));
		//client.addServer(new ServerProperty("localhost", 1144));
		client.connect(args);
	}
}
