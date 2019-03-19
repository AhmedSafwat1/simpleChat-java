import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;


public class ServerSide {
	ServerSocket serverSocket;
	Socket s;
	public ServerSide()
	{
		try {
			serverSocket = new ServerSocket(5005);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(true)
		{
			
			try {
				s = serverSocket.accept();
				new ChatHandler(s);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	public static void main(String[] args)
	{
		new ServerSide();
	}
	
}
class ChatHandler extends Thread
{
	DataInputStream dis;
	PrintStream ps;
	Socket s;
	static Vector<ChatHandler> clientsVector = new Vector<ChatHandler>();
	BufferedReader inputLine ;
	public ChatHandler(Socket cs)
	{
		s = cs;
		try {
			dis = new DataInputStream(cs.getInputStream());
			inputLine = new BufferedReader(new InputStreamReader(dis));
			ps = new PrintStream(cs.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		clientsVector.add(this);
		//System.out.println(clientsVector.size());
		start();
	}
	public void run()
	{
		while(true)
		{
			String str;
			try {
				
				str = inputLine.readLine();
				if(str != null)
					sendMessageToAll(str);
				else
					break;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	void sendMessageToAll(String msg)
	{
		for(ChatHandler ch : clientsVector)
		{
			// System.out.println(ch.s.isConnected());
			ch.ps.println(msg);
			//ch.ps.flush();
		}
	}
}