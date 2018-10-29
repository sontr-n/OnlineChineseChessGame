package game;

import javax.swing.JPanel;
import java.awt.Frame;
import javax.swing.JDialog;
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextField;

public class NetWorkSetting extends JDialog {

	
	private static final long serialVersionUID = 1L;		//Use VE to draw this interface.
	private JPanel jContentPane = null;
	private JPanel jPanel = null;
    private JPanel jPanel1 = null;
    private JButton jButton = null;
    private JLabel jLabel = null;
    private JTextField jTextField = null;
    private JTextField jTextField1 = null;
    private JTextField jTextField2 = null;
    private JTextField jTextField3 = null;
    private JLabel jLabel1 = null;
    private JTextField jTextField4 = null;

    public NetWorkSetting(Frame owner){
    	super(owner);
	    initialize();
    }

    private void initialize(){
	    setSize(300, 200);
	    setTitle("NetWorking Setting");
	    setResizable(false);
	    setContentPane(getJContentPane());
    }

    private JPanel getJContentPane(){
    	if(this.jContentPane == null){
    		this.jLabel = new JLabel();
    		this.jLabel.setText("Server IP:");
    		this.jLabel.setBounds(new Rectangle(1, 1, 118, 26));
    		this.jContentPane = new JPanel();
    		this.jContentPane.setLayout(null);
    		this.jContentPane.add(getJPanel(), null);
    		this.jContentPane.add(getJPanel1(), null);
    		this.jContentPane.add(getJButton(), null);
    	}
	    return this.jContentPane;
    }

    private JPanel getJPanel(){
	    if(this.jPanel == null){
	    	this.jPanel = new JPanel();
	    	this.jPanel.setLayout(null);
	    	this.jPanel.setBounds(new Rectangle(15, 5, 256, 64));
	    	this.jPanel.add(this.jLabel, null);
	    	this.jPanel.add(getJTextField(), null);
	    	this.jPanel.add(getJTextField1(), null);
	    	this.jPanel.add(getJTextField2(), null);
	    	this.jPanel.add(getJTextField4(), null);
	    }
	    return this.jPanel;
    }

    private JPanel getJPanel1(){
	    if(this.jPanel1 == null){
	    	this.jLabel1 = new JLabel();
	    	this.jLabel1.setBounds(new Rectangle(4, 2, 115, 22));
	    	this.jLabel1.setText("Port Number:");
	    	this.jPanel1 = new JPanel();
	    	this.jPanel1.setLayout(null);
	    	this.jPanel1.setBounds(new Rectangle(14, 76, 255, 53));
	    	this.jPanel1.add(this.jLabel1, null);
	    	this.jPanel1.add(getJTextField3(), null);
	    }
	    return this.jPanel1;
    }

    private JButton getJButton(){
	    if(this.jButton == null){
	    	this.jButton = new JButton();
	    	this.jButton.setBounds(new Rectangle(98, 130, 80, 30));
	    	this.jButton.setText("OK");
	    	this.jButton.addActionListener(new ActionListener(){
	    		public void actionPerformed(ActionEvent e){
	    			if(NetWorkSetting.this.getNetWorkSetting()) {
	    				NetWorkSetting.this.dispose();
	    				}
	    			}
	    		}
	    	);
	    }
	    return this.jButton;
	  }

	  private JTextField getJTextField(){
	    if(this.jTextField == null){
	    	this.jTextField = new JTextField();
	    	this.jTextField.setBounds(new Rectangle(19, 35, 40, 22));
	    	this.jTextField.setText("");
	    }
	    return this.jTextField;
	  }

	  private JTextField getJTextField1(){
	    if(this.jTextField1 == null){
	    	this.jTextField1 = new JTextField();
	    	this.jTextField1.setBounds(new Rectangle(78, 35, 40, 22));
	    	this.jTextField1.setText("");
	    }
	    return this.jTextField1;
	  }

	  private JTextField getJTextField2(){
	    if(this.jTextField2 == null){
	    	this.jTextField2 = new JTextField();
	    	this.jTextField2.setBounds(new Rectangle(137, 35, 40, 22));
	    	this.jTextField2.setText("");
	    }
	    return this.jTextField2;
	  }

	  private JTextField getJTextField3(){
	    if(this.jTextField3 == null){
	    	this.jTextField3 = new JTextField();
	    	this.jTextField3.setBounds(new Rectangle(15, 27, 60, 22));
	    	this.jTextField3.setText("6666");
	    }
	    return this.jTextField3;
	  }

	  private JTextField getJTextField4(){
	    if(this.jTextField4 == null){
	    	this.jTextField4 = new JTextField();
	    	this.jTextField4.setBounds(new Rectangle(196, 35, 40, 22));
	    	this.jTextField4.setText("");
	    }
	    return this.jTextField4;
	  }

	  private boolean getNetWorkSetting(){
		  String str1 = this.jTextField.getText();
		  String str2 = this.jTextField1.getText();
		  String str3 = this.jTextField2.getText();
		  String str4 = this.jTextField4.getText();
		  String port = this.jTextField3.getText();
		  String ip = str1 + "." + str2 + "." + str3 + "." + str4;
		  int i1 = Integer.parseInt(str1);
		  int i2 = Integer.parseInt(str2);
		  int i3 = Integer.parseInt(str3);
		  int i4 = Integer.parseInt(str4);
		  int i5 = Integer.parseInt(port);
		  Pattern p = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");		//Use regular expressions to ensure the port number is correct.
		  Matcher m = p.matcher(ip);
		  if(m.matches()&&i1>0&&i1<=255&&i2>=0&&i2<=255&&i3>=0&&i3<=255&&i4>0&&i4<=255&&i5>1024&&i5<=65535){
			  MainFrame.ipAddress = new String(ip);
			  MainFrame.port = new String(port);
			  return true;
		  }
		  
		  JOptionPane.showMessageDialog(null, 
				  "Something wrong with the IP address or Port number", "ERROR!", 
				  -1);
	    return false;
	  }
}