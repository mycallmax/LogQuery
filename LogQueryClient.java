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
				printResult(worker.getServer(), worker.getResultFile());
			} catch(InterruptedException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	private void printResult(ServerProperty server, File resultFile) {
		if (resultFile == null) {
			return;
		}
		resultFile.deleteOnExit();
		System.out.println(
			"Result from the Server " + server.ip + ":" + server.port + ":");
		try {
			BufferedReader result = new BufferedReader(new FileReader(resultFile));
			String line;
			while ((line = result.readLine()) != null) {
				System.out.println(line);
			}
			result.close();
		} catch(FileNotFoundException ex) {
			System.out.println(ex.getMessage());
			System.exit(-1);
		} catch(IOException ex) {
			System.out.println(ex.getMessage());
		}
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage: java LogQueryClient <grep_arg1> <grep_arg2> ... <grep_argN>");
			System.exit(-1);
		}
		LogQueryClient client = new LogQueryClient();
		client.addServer(new ServerProperty("130.126.112.146", 1111));
		client.addServer(new ServerProperty("130.126.112.117", 1122));
		client.addServer(new ServerProperty("130.126.112.148", 1133));
		client.addServer(new ServerProperty("130.126.112.146", 1144));
		client.connect(args);
	}
}
