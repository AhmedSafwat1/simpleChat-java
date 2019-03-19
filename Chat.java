import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Chat extends JFrame implements Runnable {
	Thread th;
	Client c = new Client();
	JTextField tf = new JTextField(40);
	JButton okButton = new JButton("Send");
	JTextArea ta = new JTextArea(20,50);
	String name;
	public Chat(){
		th = new Thread(this);
		this.setLayout(new FlowLayout());
		JScrollPane scroll = new JScrollPane(ta);
		ta.setEditable(false);
		okButton.addActionListener(new SendListener());
		tf.addActionListener(new SendListener());
		add(scroll);
		add(tf);
		add(okButton);
		name = JOptionPane.showInputDialog(this, "Enter Your Name");
		if(name == null || name.isEmpty())
			name = "unknown";
		th.start();

		this.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
		      	try
				{	
					c.ps.print(name+ " ");
					c.ps.close();
					c.dis.close();
					c.mySocket.close();
					th.stop();
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
		    }
		});
	
	}

	class SendListener implements ActionListener{
		public void actionPerformed(ActionEvent ev){
			c.ps.println(name + " : "+tf.getText()+"\n");
			tf.setText("");	
		}
	}

	public void run()
	{
		while(!c.mySocket.isClosed())
		{
			try{
				String str = c.dis.readLine();
				if(str == null)
				{
					c.mySocket.close();
					break;
				}
				ta.append(str+"\n");
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
		JOptionPane.showMessageDialog(null,"Error server Shudown ");
	}

	public static void main(String args[])
	{
		Chat ui = new Chat();
		ui.setSize(600, 400);
		ui.setResizable(false);
		ui.setVisible(true);
		ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}


class Client
{
	Socket mySocket;
	DataInputStream dis;
	PrintStream ps;

	public Client(){
		try {
			mySocket = new Socket(InetAddress.getLocalHost(), 5005);
			dis = new DataInputStream(mySocket.getInputStream ());
			ps = new PrintStream(mySocket.getOutputStream ());
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}

	}
}