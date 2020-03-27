package cfh.jlogikus;

import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new GUI()::initGUI);
    }
    
    private final String version;
    private JFrame frame;
    
    private GUI() {
        version = Optional.ofNullable(getClass().getPackage().getImplementationVersion()).orElse("dev");
    }
    
    private void initGUI() {
        frame = new JFrame();
        frame.setTitle("JLogikus - " + version + " - by Carlos Heuberger") ;
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
        frame.add(new LogikusPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
