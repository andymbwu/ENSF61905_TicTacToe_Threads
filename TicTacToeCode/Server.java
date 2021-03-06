import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
	Socket xSocket;
	Socket oSocket;
	ServerSocket serverSocket;

	PrintWriter xSocketOut;
	PrintWriter oSocketOut;
	BufferedReader xSocketIn;
	BufferedReader oSocketIn;
	
	/**
	 * Thread Pool to Handle Communication.
	 */
	private ExecutorService pool;

	public Server() { // throws IOException {

		try {
			serverSocket = new ServerSocket(9899);
			this.pool = Executors.newFixedThreadPool(5);
			
		} catch (IOException e) {
			System.out.println("Create new socket error");
			System.out.println(e.getMessage());
		}
		System.out.println("TicTacToe.Server is running");
	}

	public void runServer() {
		try {

			while (true) {
				xSocket = serverSocket.accept();
				System.out.println("TicTacToe.Player X has been connected");
				
				oSocket = serverSocket.accept();
				System.out.println("TicTacToe.Player O has been connected");

				xSocketIn = new BufferedReader((new InputStreamReader(xSocket.getInputStream())));
				oSocketIn = new BufferedReader((new InputStreamReader(oSocket.getInputStream())));
				xSocketOut = new PrintWriter(xSocket.getOutputStream(), true);
				oSocketOut = new PrintWriter(oSocket.getOutputStream(), true);
				
				//TicTacToe.Game needs to have both sockets as input arguments to it's constructor
				Game game = new Game(xSocket,oSocket);
				
				pool.execute(game);
			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
			// Stop accepting new games and finish any active ones, then shutdown the threadpool.
			pool.shutdown();
			try {
				xSocket.close();
				oSocket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
	}

	public static void main(String[] args) throws IOException {
		Server myServer = new Server();
		myServer.runServer();
	}

}