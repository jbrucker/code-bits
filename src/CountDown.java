import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * A JFrame with a background image and count-down timer.
 *
 * The background image is drawn on a JLabel, which is not scaled.
 * For a scalable background, try this:
 * https://tips4java.wordpress.com/2008/10/12/background-panel/
 * 
 * @see https://tips4java.wordpress.com/2008/10/12/background-panel/
 *
 */
public class CountDown extends JFrame {
	private static final String BACKGROUND = "/res/BG2.png";
	private JFrame window;
	private JPanel pane;

	public CountDown() {
		window = this;
		window.setDefaultCloseOperation(EXIT_ON_CLOSE);
		initComponents();
	}

	private void initComponents() {
		// How to get the screen actual height and width, in pixels.
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		// actual width and height are integers (pixels)
		int height = (int) size.getHeight();
		int width = (int) size.getWidth();
		// set up the background image and make the window size match (if possible)
		// For a scalable background, try "background-panel" (see link in Javadoc, above).
		ImageIcon image = new ImageIcon(this.getClass().getResource(BACKGROUND));
		int w = Math.min( width, image.getIconWidth() );
		int h = Math.min( height, image.getIconHeight() );
		// make the window size match the background image size.
		window.setBounds((width-w)/2, (height-h)/2, w, h);
		window.setContentPane(new JLabel(image));
		
		// supply a new layout manager for rest of the contents
		window.setLayout( new GridBagLayout() );
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridheight = GridBagConstraints.REMAINDER;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		pane = new JPanel();
		pane.setOpaque(false); // so we can see the background image
		pane.setPreferredSize(new Dimension(width,height));
		window.add(pane, gbc);
	}
	
	/**
	 * Display a count-down timer using Timer and TimerTask.
	 * You only need *one* Timer and TimerTask.
	 * 
	 * @param countStart the starting count, such as 3 (shows "3", "2", "1", "TYPE")
	 */
	public void countDown(final int countStart) {
		// Create font once it once and save it so we don't recreate it (waste memory).
		final String FONTNAME = "28 Days Later.ttf";
		Font font = AbstractFont.getFont(FONTNAME);
		Font numberFont = font.deriveFont(Font.BOLD, 300);
		// A slightly smaller font for displaying text message, like "TYPE"
		Font textFont = font.deriveFont(Font.BOLD, numberFont.getSize()*3/4);
		JLabel number = new JLabel("     ", SwingConstants.CENTER);
		number.setForeground(Color.YELLOW);
		number.setFont(numberFont);
		// this might not be necessary
		number.setSize(pane.getWidth()/2, pane.getHeight()/2);
		number.setLocation( (pane.getWidth() - number.getWidth()) / 2,
				(pane.getHeight() - number.getHeight()) / 2 );
		pane.add(number);
		
		// A count-down timer using TimerTask
		Timer timer = new Timer();
		// The task to perform repeatedly, until cancelled:
		final TimerTask task = new TimerTask() {
			int countdown = countStart;
			public void run() {
				if (countdown > 0) number.setText(Integer.toString(countdown));
				else if (countdown == 0) {
					number.setFont(textFont);
					number.setText("TYPE");
				}
				else {
					// finished countdown. Remove components and cancel the task.
					number.setVisible(false);
					pane.remove(number);
					cancel(); // cancel the TimerTask
				}
				countdown--;
			}
		};
		long period = 1000;  // frequency in milliseconds
		long delay = 0;      // delay before first time the task is run
		timer.scheduleAtFixedRate(task, delay, period);
	}
	
	/**
	 * Demonstrate the frame with countdown timer.
	 * @param args not used
	 */
	public static void main(String[] args) {
		CountDown frame = new CountDown();
		frame.setVisible(true);
		frame.countDown(3);
	}
}
