package cfh.jlogikus;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class TestGUI {

    private static final String VERSION = "0.0.2";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new TestGUI()::initGUI);
    }
    
    private JFrame frame;
    
    private TestGUI() {
    }
    
    private void initGUI() {
        frame = new JFrame();
        frame.setTitle("JLogikus " + VERSION + " by Carlos Heuberger") ;
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
        frame.add(new LogikusPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
