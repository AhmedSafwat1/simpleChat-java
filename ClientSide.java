
import java.awt.*;
import java.awt.Window.Type;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.*;
public class ClientSide extends JFrame{
	public JTextArea ta;
	public JTextField tf;
	public Socket s;
	//public PrintWriter pw;
	public JButton okButton;
	DataInputStream dis;
	PrintStream pw;
	BufferedReader inputLine;
	public String name;
	RederHandler th;
	ClientSide()
	{
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		name = JOptionPane.showInputDialog(this, "Type your Name");
		if(name == null || name.isEmpty())
			name = "unknown";
		System.out.println(name);
		ta = new JTextArea(20,50);
		ta.setEditable(false);
		JScrollPane scroll = new JScrollPane(ta);
		tf = new JTextField(40);
		okButton = new JButton("Send");
		setLayout(new FlowLayout());
		this.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
		      	try
				{	
					//pw.print(name+ " ");
					pw.close();
					//th.stop();
					s.close();
					
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
		    }
		});
		
		add(scroll);
		add(tf);
		add(okButton);
		// chating
		try {
			s =  new Socket(InetAddress.getLocalHost(), 5005);
			pw = new PrintStream(s.getOutputStream());
			dis = new DataInputStream(s.getInputStream());
			inputLine = new BufferedReader(new InputStreamReader(dis));
			th = new RederHandler();
			th.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		okButton.addActionListener(new SendListner());
		tf.addActionListener(new SendListner());
		
	
	
	}
	class SendListner implements ActionListener
	{
		public void actionPerformed(ActionEvent ae){
//			ta.append(tf.getText()+"\n");
//			tf.setText("");
			
			pw.println(name+" : " + tf.getText());
			//pw.flush();
			System.out.println(pw+""+tf.getText());
			tf.setText("");
			}
	}
	public static void main(String args[])
	{
		ClientSide ui=new ClientSide();
		ui.setSize(600, 400);
		ui.setResizable(false);
		ui.setVisible(true);
	}
class RederHandler extends Thread
{
	
	public void run() {
	//	System.out.println("run");
		while(true)
		{
			
			try {
				
				String msg ;
				//System.out.println("run");
				if((msg = inputLine.readLine()) != null )
				{
					//	System.out.println(msg);
					ta.append( msg+"\n");
				}else{
					JOptionPane.showMessageDialog(this,"Error server Shudown ");
					break;
				}
				
			} catch (IOException e) {
				
				//e.printStackTrace();
				JOptionPane.showMessageDialog(this,"Error server Shudown ");
				break;
			}
			catch(NullPointerException e) 
	        { 
	            System.out.print("NullPointerException Caught"); 
	        }
			
		}
		
	}
	
}
	
}

