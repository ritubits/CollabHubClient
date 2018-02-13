package collabhubclient;

import collabhubclient.RegProjectClient;

public class EntryFormRegProject {

	static boolean DEBUG = false;
	static RegProjectClient regObject = null;
	static Boolean registerSuceed = false;
	static String regProjectName = null;

	/*
	 * public EntryFormRegProject() { regObject= new RegProjectClient(); }
	 * 
	 * public Boolean executeForm() { JFrame frame = new
	 * JFrame("Register Project: Admin"); frame.setSize(400, 250);
	 * 
	 * //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 * frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	 * 
	 * JPanel panel = new JPanel(); frame.add(panel); addComponents(panel,
	 * frame);
	 * 
	 * frame.setVisible(true); return registerSuceed; }
	 * 
	 * private static void addComponents(JPanel panel, JFrame thisFrame) {
	 * 
	 * panel.setLayout(null);
	 * 
	 * JLabel projectLabel = new JLabel("Enter Project Name:");
	 * projectLabel.setBounds(10, 10, 150, 25); panel.add(projectLabel);
	 * 
	 * final JTextField projectText = new JTextField(20);
	 * projectText.setBounds(200, 10, 160, 25); panel.add(projectText);
	 * 
	 * 
	 * JLabel ownerLabel = new JLabel("Enter Owner Name:");
	 * ownerLabel.setBounds(10, 40, 150, 25); panel.add(ownerLabel);
	 * 
	 * final JTextField ownerText = new JTextField(20); ownerText.setBounds(200,
	 * 40, 160, 25); panel.add(ownerText);
	 * 
	 * 
	 * 
	 * JLabel serverLabel = new JLabel("Enter Tomcat IP address:");
	 * serverLabel.setBounds(10, 80, 150, 25); panel.add(serverLabel);
	 * 
	 * final JTextField serverText = new JTextField(20);
	 * serverText.setBounds(200,80, 160, 25);
	 * serverText.setText("localhost:8080"); panel.add(serverText);
	 * 
	 * JLabel DBserverLabel = new JLabel("Enter MySQL IP address:");
	 * DBserverLabel.setBounds(10, 120, 150, 25); panel.add(DBserverLabel);
	 * 
	 * final JTextField DBserverText = new JTextField(20);
	 * DBserverText.setBounds(200, 120, 160, 25);
	 * DBserverText.setText("localhost:3306"); panel.add(DBserverText);
	 * 
	 * JButton registerButton = new JButton("Register");
	 * registerButton.setBounds(130, 160, 100, 30);
	 * 
	 * panel.add(registerButton);
	 * 
	 * registerButton.addActionListener( new ActionListener(){ public void
	 * actionPerformed( ActionEvent e){
	 * 
	 * // do all null checking here
	 * 
	 * if (DEBUG) System.out.println("In registerProjectForm: Register"); if
	 * ((!projectText.getText().isEmpty())) { if (DEBUG)
	 * System.out.println("Project Name:"+ projectText.getText()); if (DEBUG)
	 * System.out.println("Owner Name:"+ ownerText.getText()); if (DEBUG)
	 * System.out.println("IP Address:"+ serverText.getText());}
	 * 
	 * if ((!projectText.getText().isEmpty()) &&
	 * (!ownerText.getText().isEmpty()) && (!serverText.getText().isEmpty()) &&
	 * (!DBserverText.getText().isEmpty())) { // call the regHttpClient here-
	 * which calls the regProjectServlet //which creates table for the
	 * regProject_projectName //also caretes userDetails_projectname table //
	 * send the parameters ... // write to file in the servlet try {
	 * regObject.setConfigProjectValues(projectText.getText(),
	 * ownerText.getText(), serverText.getText(), DBserverText.getText());
	 * boolean status= regObject.executeClient(); if (status) { // give message
	 * written to config file // dispose this window registerSuceed= true;
	 * regProjectName = projectText.getText();
	 * JOptionPane.showMessageDialog(null, "Project Successfully Registered",
	 * "Message Info", JOptionPane.INFORMATION_MESSAGE);
	 * 
	 * } else JOptionPane.showMessageDialog(null,
	 * "Unable to register the project", "Message Info",
	 * JOptionPane.INFORMATION_MESSAGE); } catch (Exception ex) { if (DEBUG)
	 * System.out.println("Error calling RegClient");
	 * JOptionPane.showMessageDialog(null, "Unable to register the project",
	 * "Message Info", JOptionPane.INFORMATION_MESSAGE); ex.printStackTrace(); }
	 * finally { //close client here try{ if (regObject != null)
	 * regObject.closeClient(); } catch (Exception ee){ ee.printStackTrace(); }
	 * 
	 * }//finally }//if else JOptionPane.showMessageDialog(null,
	 * "Unable to register the project", "Message Info",
	 * JOptionPane.INFORMATION_MESSAGE); // multiple levels of a project not
	 * allowed at present. // will include at the end if required and possible
	 * // check if project already exists. //If yes, check level, if already
	 * exists for that level then prompt // register
	 * 
	 * 
	 * 
	 * } } );
	 * 
	 * 
	 * }// add components
	 */

}