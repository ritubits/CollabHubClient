package collabhubclient;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import collabhubclient.DeRegProjectClient;

public class EntryFormDeRegProject {

	
	static DeRegProjectClient deRegObject = null;
	
		
		public EntryFormDeRegProject()
		{
			deRegObject= new DeRegProjectClient();
		}
		
		public void executeForm()
		{
		JFrame frame = new JFrame("DeRegister Project: Admin");
		frame.setSize(400, 250);
		
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel panel = new JPanel();
		frame.add(panel);
		addComponents(panel, frame);

		frame.setVisible(true);
		
	}

	private static void addComponents(JPanel panel, JFrame thisFrame) {

		panel.setLayout(null);

		JLabel projectLabel = new JLabel("Enter Project Name:");
		projectLabel.setBounds(10, 10, 150, 25);
		panel.add(projectLabel);

		final JTextField projectText = new JTextField(20);
		projectText.setBounds(200, 10, 160, 25);
		panel.add(projectText);

		
		JLabel ownerLabel = new JLabel("Enter Owner Name:");
		ownerLabel.setBounds(10, 40, 150, 25);
		panel.add(ownerLabel);

		final JTextField ownerText = new JTextField(20);
		ownerText.setBounds(200, 40, 160, 25);
		panel.add(ownerText);
		
		

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
			
		JButton registerButton = new JButton("DeRegister");
		registerButton.setBounds(130, 160, 100, 30);
		
		panel.add(registerButton);
		
		registerButton.addActionListener(
				new ActionListener(){
					public void actionPerformed(
							ActionEvent e){
										
						// do all null checking here
						
						System.out.println("In deregisterProjectForm: DERegister");
						if ((!projectText.getText().isEmpty())) {
						System.out.println("Project Name:"+ projectText.getText());
						System.out.println("Owner Name:"+ ownerText.getText());
						System.out.println("IP Address:"+ serverText.getText());}
					
						if ((!projectText.getText().isEmpty()) && (!ownerText.getText().isEmpty()) && (!serverText.getText().isEmpty()) && (!DBserverText.getText().isEmpty()))
						{
						// call the deregHttpClient here- which calls the DeRegProjectServlet
						//which creates table for the regProject_projectName 
						// send the parameters ...
						// write to file in the servlet
						try {
							deRegObject.setConfigProjectValues(projectText.getText(), ownerText.getText(), serverText.getText(), DBserverText.getText());
							boolean status= deRegObject.executeClient();
							if (status)
							{
							// give message written to config file
								// dispose this window
							JOptionPane.showMessageDialog(null, "Project Successfully DeRegistered", "Message Info", JOptionPane.INFORMATION_MESSAGE);
								
							}
							else 
								JOptionPane.showMessageDialog(null, "Unable to deregister the project", "Message Info", JOptionPane.INFORMATION_MESSAGE);
						}
						catch (Exception ex)
						{
							System.out.println("Error calling DeRegClient");
							JOptionPane.showMessageDialog(null, "Unable to deregister the project", "Message Info", JOptionPane.INFORMATION_MESSAGE);
							ex.printStackTrace();
						}
						finally
						{
							//close client here
							try{
								if (deRegObject != null) deRegObject.closeClient();
							}
							catch (Exception ee){
								ee.printStackTrace();
							}
							
						}//finally
						}//if
						else
							JOptionPane.showMessageDialog(null, "Unable to deregister the project", "Message Info", JOptionPane.INFORMATION_MESSAGE);
						
						
										  }
									}
							);


	}// add components
	


}