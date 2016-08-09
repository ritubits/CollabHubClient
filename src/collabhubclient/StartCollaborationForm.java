package collabhubclient;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import collabhubclient.StartCollaborationClient;

public class StartCollaborationForm {

	static boolean DEBUG= false;
	static StartCollaborationClient collabClient = null;
	static Boolean registerSuceed = false;
	static String regProjectName= null;
		
		public StartCollaborationForm()
		{
			collabClient= new StartCollaborationClient();
		}
		
		public StartCollaborationClient getCollabClient()
		{
			//after starting collaboration
			//all should use this client
			return collabClient;
		}
		
		public Boolean getCollabStatus()
		{
			return registerSuceed;
		}
		
		public Boolean executeForm()
		{
		JFrame frame = new JFrame("Start Collaboration: Collaborator");
		frame.setSize(400, 250);
		
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel panel = new JPanel();
		frame.add(panel);
		addComponents(panel, frame);

		frame.setVisible(true);
		return registerSuceed;
	}

	private static void addComponents(JPanel panel, JFrame thisFrame) {

		panel.setLayout(null);

		JLabel projectLabel = new JLabel("Enter Project Name:");
		projectLabel.setBounds(10, 10, 150, 25);
		panel.add(projectLabel);

		final JTextField projectText = new JTextField(20);
		projectText.setBounds(200, 10, 160, 25);
		panel.add(projectText);

		
		JLabel collabLabel = new JLabel("Enter Your Name:");
		collabLabel.setBounds(10, 40, 150, 25);
		panel.add(collabLabel);

		final JTextField collabText = new JTextField(20);
		collabText.setBounds(200, 40, 160, 25);
		panel.add(collabText);
		
		

	        JLabel serverLabel = new JLabel("Enter Tomcat IP address:");
			serverLabel.setBounds(10, 80, 150, 25);
			panel.add(serverLabel);

			final JTextField serverText = new JTextField(20);
			serverText.setBounds(200,80, 160, 25);
			serverText.setText("localhost:8080");
			panel.add(serverText);
	        
	        JLabel DBserverLabel = new JLabel("Enter MySQL IP address:");
	        DBserverLabel.setBounds(10, 120, 150, 25);
			panel.add(DBserverLabel);

			final JTextField DBserverText = new JTextField(20);
			DBserverText.setBounds(200, 120, 160, 25);
			DBserverText.setText("localhost:3306");
			panel.add(DBserverText);
			
		JButton registerButton = new JButton("Connect");
		registerButton.setBounds(130, 160, 100, 30);
		
		panel.add(registerButton);
		
		registerButton.addActionListener(
				new ActionListener(){
					public void actionPerformed(
							ActionEvent e){
										
						// do all null checking here
						
						if (DEBUG) System.out.println("In StartCollaborationForm");
						if ((!projectText.getText().isEmpty())) {
							if (DEBUG) System.out.println("Project Name:"+ projectText.getText());
							if (DEBUG) System.out.println("Collaborator Name:"+ collabText.getText());
							if (DEBUG) System.out.println("IP Address:"+ serverText.getText());}
					
						if ((!projectText.getText().isEmpty()) && (!collabText.getText().isEmpty()) && (!serverText.getText().isEmpty()) && (!DBserverText.getText().isEmpty()))
						{
						//call startCollaborationClient
						// update table userdetails_projectName
						try {
							collabClient.setConfigProjectValues(projectText.getText(), collabText.getText(), serverText.getText(), DBserverText.getText());
							boolean done = collabClient.createCollabClient();
							// if client created
							if (done)
							{
								boolean status= collabClient.updateUserTable();
								if (status)
								{
								
									// dispose this window
									registerSuceed= true;
									regProjectName = projectText.getText();
									JOptionPane.showMessageDialog(null, "Successfully Connected to CollabHub", "Message Info", JOptionPane.INFORMATION_MESSAGE);
									
								}
								else 
									JOptionPane.showMessageDialog(null, "Unable to connect to CollabHub", "Message Info", JOptionPane.INFORMATION_MESSAGE);
							}
						}
						catch (Exception ex)
						{
							if (DEBUG) System.out.println("Error calling collabClient");
							JOptionPane.showMessageDialog(null, "Unable to connect to CollabHub", "Message Info", JOptionPane.INFORMATION_MESSAGE);
							ex.printStackTrace();
						}
						finally
						{
							//do not close client here
						/*	try{
								if (collabClient != null) collabClient.closeClient();
							}
							catch (Exception ee){
								ee.printStackTrace();
							}
							*/
						}//finally
						}//if
						else
							JOptionPane.showMessageDialog(null, "Unable to connect to CollabHub", "Message Info", JOptionPane.INFORMATION_MESSAGE);
						// multiple levels of a project not allowed at present.
						// will include at the end if required and possible
						// check if project already exists.
						//If yes, check level, if already exists for that level then prompt
						// register

						
						
										  }
									}
							);


	}// add components
	


}