package game;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


public class ChessServer extends Frame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4690432324852478593L;

	
	TextArea ta = new TextArea();
	
	/**
	 * The method initialize the chess server Frame
	 * @return: void
	 */
	public void launchFrame()
	{
		this.setTitle("ChessServer");
		add(ta, BorderLayout.CENTER);
		setBounds(100,100,300,300);	
		this.setResizable(false);
		this.addWindowListener(
			new WindowAdapter() 
			{
				public void windowClosing(WindowEvent e)
				{
					System.exit(0);
				}
			}
			);
		setVisible(true);
	}
	
	ServerSocket server = null;
	Collection<ClientConn> cClient = new ArrayList<ClientConn>();
	
	
	public ChessServer(int port) throws Exception
	{
		server = new ServerSocket(port);
		launchFrame();
	}
	
	/**
	 * The method start the chess Sever function, 
	 * if there is more than two player connect to the server, the last connecter will receive a warning information 
	 * @return: void
	 */
	public void startServer() throws Exception
	{
		int i = 0;  
		while(true)
		{
			
			Socket s = server.accept();	
			ClientConn cc = new ClientConn(s); 
			cClient.add(cc);
			ta.append("NEW-CLIENT " + s.getInetAddress() + ":" + s.getPort());
			ta.append("\n" + "CLIENTS-COUNT: " + cClient.size() + "\n\n");
			if(cClient.size()<=2){
				i++;		
				if(i % 2 != 0){
					cc.send("Master-true " + "\n");
				}else{
					cc.send("Master-false " + "\n");				
				}
			}else{
				cc.send("Chat-Server: Sorry. There is already two player on \nthis server,the room is full, the connection \nis disconnectted"+"\n");
				cc.send("Server-Disconnect \n");
				cc.dispose();
			}
			
		}
		
		
	}
	
	/**
	 * The class wrap up all resources for the player end socket
	 * it can send information to the player client, or disconnect the player.	 
	 */
	class ClientConn implements Runnable
	{
		Socket s = null;
		public ClientConn(Socket s)
		{
			this.s = s;
			(new Thread(this)).start();
		}
		
		public void send(String str) throws IOException
		{
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeUTF(str);
		}
		
		public void dispose()
		{
			try {
				if (s != null) s.close();
				cClient.remove(this);
				ta.append("A client out! \n");
				ta.append("CLIENT-COUNT: " + cClient.size() + "\n\n");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		public void run()
		{
			try {
				System.out.println("waiting for chess input");
				DataInputStream dis = new DataInputStream(s.getInputStream());
				System.out.println("waiting for chess input");
				String str = dis.readUTF();
				while(str != null && str.length() !=0)
				{
					System.out.println(str);
					for(Iterator<ClientConn> it = cClient.iterator(); it.hasNext(); )
					{
						ClientConn cc = (ClientConn)it.next();
						if(this != cc)
						{
							cc.send(str);
						}
					}
					str = dis.readUTF();
					send(str);
					
				}
				this.dispose();
			} 
			catch (Exception e) 
			{
				this.dispose();
			}
			
		}
		
		
	}
	
	public static void main(String[] args) throws Exception
	{
		ChessServer cs = new ChessServer(6666);
		cs.startServer();

	}
}
