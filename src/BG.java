import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyListener;

public class BG {

	public BG(KeyListener listener) {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice dev = env.getDefaultScreenDevice();

		JFrame f = new JFrame();
		f.setUndecorated(true);
		f.setLocationRelativeTo(null);
		f.addKeyListener(listener);
		f.setBackground(Color.black);
		f.getContentPane().setBackground(Color.black);
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		f.setVisible(true);

		JLabel l = new JLabel("loading...", SwingConstants.CENTER);
		l.setFocusable(false);
		l.setForeground(Color.white);
		l.setSize(dev.getDisplayMode().getWidth(), dev.getDisplayMode().getHeight());

		l.setFont(new Font("Roboto", Font.BOLD, 70));

		f.getContentPane().add(l);
		dev.setFullScreenWindow(f);
	}
}
