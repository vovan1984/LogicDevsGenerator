import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * This class represents window allowing to select input and output files, and
 * options to generate LOGIC_DEVS insert queries.
 * 
 * Once files and options are chosen, a user can press Generate button to create
 * file with queries.
 * 
 * @author Vladimir Igumnov
 *
 */
public class LogicDevsWindow extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private static final int INIT_WIDTH = 478;
	private static final int INIT_HEIGHT = 228;

	private final JFileChooser fc; // dialog for selecting files.
	
	// buttons on the window
	private final JButton selectInputButton, selectOutputButton,
	                      generateButton;
	
	private final JComboBox<String> logicDevsOptions;
	
	private String logicDevsValues[] = {"emaltef/EVL1",
			                            "hlr/HLR1",
			                            "hlrf/HLF1",
			                            "emalte/EVL1"};

	// fields for selecting input and output files
	JTextField inputFileTextField, outputFileTextField;

	/**
	 * Constructor for a LogicDevs program window. <br>
	 * It creates two input fields:
	 * <ul>
	 *     <li>Upper field is for input file</li>
	 *     <li>Lower field is for output file </li>
	 * </ul>
	 * 
	 * @param title Title to be displayed in the window.
	 */
	public LogicDevsWindow(String title)
	{
		super(title);   

		this.setLayout(new FlowLayout());

		// create file selector
		fc = new JFileChooser();

		// create input fields with default values.
		this.add(new JLabel("Please select input file: "));
		inputFileTextField = new JTextField("input.txt", 15);
		this.add(inputFileTextField);

		//add button to select input file
		selectInputButton = new JButton(createImageIcon("Open16.gif"));
		selectInputButton.addActionListener(this);
		this.add(selectInputButton);

		this.add(new JLabel("Please enter output file name: "));
		outputFileTextField = new JTextField("output.sql", 15);
		this.add(outputFileTextField);
		
		//add button to select output file
		selectOutputButton = new JButton(createImageIcon("Open16.gif"));
		selectOutputButton.addActionListener(this);
		this.add(selectOutputButton);

		// add selection of options
		this.add(new JLabel("Please select values for DVC_TP/LOGICAL_DVC_ID: "));
		logicDevsOptions = new JComboBox<>(logicDevsValues);
		logicDevsOptions.setBackground(Color.WHITE);
		this.add(logicDevsOptions);
		
		// add button to initiate the generation process
		generateButton = new JButton("Generate!");
		generateButton.addActionListener(this);
		this.add(generateButton);

		// set initial window size
		setSize(INIT_WIDTH, INIT_HEIGHT);

		// place window to the center of the screen
		centreWindow(this);

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		this.setVisible(true);
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = LogicDevsWindow.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	// place window to the center of the screen
	protected static void centreWindow(Window frame) 
	{
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
	}

	/**
	 * Generate output file after button is pressed:<br>
	 * <ol>
	 *     <li>Open input file</li>
	 *     <li>Read lines one by one</li>
	 *     <li>Generate sql query for input line</li>
	 *     <li>Write the query to the output file</li>
	 * </ol>
	 */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		//Handle open button action.
		if (e.getSource() == selectInputButton) 
			handleSelectInputButtonClick();
		else if (e.getSource() == selectOutputButton)
			handleSelectOutputButtonClick();
		else if (e.getSource() == generateButton)
            handleGenerateButtonClick();
	}


	/**
	 * If user clicks on button to generate LogicDevs queries, then
	 * create output file.
	 */
	private void handleGenerateButtonClick() {
		String inputFile = inputFileTextField.getText();
		String outputFile = outputFileTextField.getText();
		
		
		String dvcTp="", logicalDvcId="";
		
		// set values of fields based on user's selection.
		switch((String)logicDevsOptions.getSelectedItem())
		{
		    case "emaltef/EVL1" : dvcTp = "'emaltef'";
		                          logicalDvcId = "'EVL1'";
		                  		  break;
		                  		  
		    case "hlr/HLR1" : dvcTp="'hlr'";
		                      logicalDvcId = "'HLR1'";
		                      break;
		                      
		    case "hlrf/HLF1" : dvcTp="'hlrf'";
                               logicalDvcId = "'HLF1'";
                               break;
                               
		    case "emalte/EVL1" : dvcTp="'emalte'";
                               logicalDvcId = "'EVL1'";
                               break;
		}

		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(inputFile), "UTF-8"));
				PrintStream ow = new PrintStream(outputFile))
		{
			String line;

			// read lines of input file (CTNs) and write queries to the output file.
			while ( (line = br.readLine()) != null)
			{
				ow.println("INSERT INTO LOGIC_DEVS (MARKET_CODE,PRODUCT_TYPE,DVC_TP,FROM_NO,SYS_CREATION_DATE,DL_SERVICE_CODE,TO_NO,LOGICAL_DVC_ID,F_OFA) VALUES ('CAN','C'," +
			               dvcTp + ",'" +
						   line + "',SYSDATE, 'v410','" + 
			               line + "', " +
						   logicalDvcId + ",'10');");
				ow.println();
			}
		}
		catch (IOException e1) 
		{
			System.out.println("Can't find '" + inputFile + "'");
		}
	}

	/**
	 * If user clicks on button to select input file, then
	 * execute Open File Dialog.
	 */
	private void handleSelectInputButtonClick() 
	{
		int returnVal = fc.showOpenDialog(LogicDevsWindow.this);

		if (returnVal == JFileChooser.APPROVE_OPTION) 
		{
			File file = fc.getSelectedFile();
			try 
			{
				inputFileTextField.setText(file.getCanonicalPath());
			} 
			catch (IOException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} 
	}

	/**
	 * If user clicks on button to select output file, then
	 * execute Open File Dialog.
	 */
	private void handleSelectOutputButtonClick() 
	{
		int returnVal = fc.showOpenDialog(LogicDevsWindow.this);

		if (returnVal == JFileChooser.APPROVE_OPTION) 
		{
			File file = fc.getSelectedFile();
			try 
			{
				outputFileTextField.setText(file.getCanonicalPath());
			} 
			catch (IOException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} 
	}
}
