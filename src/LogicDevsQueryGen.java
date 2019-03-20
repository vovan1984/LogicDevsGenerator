import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Generate queries to update LOGIC_DEVS table based on input file
 * of CTNs.
 * 
 * @author Vladimir Igumnov
 *
 */
public class LogicDevsQueryGen 
{
	public static void main(String[] args) 
	{
		// open input and output files
		try
		{
			// Set same look and feel to be used on all systems (Windows, Mac, etc.)
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");

			// Display welcome window
			SwingUtilities.invokeLater( () -> new LogicDevsWindow("LogicDevs queries generator."));
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
		{
			System.out.println("Can't set unified user interface on " + System.getProperty("os.name"));
			System.out.println(e.getMessage());
		}

	}
}
