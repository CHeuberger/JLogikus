package cfh.jlogikus;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class TestGUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new TestGUI()::initGUI);
    }
    
    private JFrame frame;
    
    private TestGUI() {
    }
    
    private void initGUI() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
        frame.add(new LogikusPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
