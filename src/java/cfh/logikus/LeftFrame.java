package cfh.logikus;

import java.awt.Graphics;
import javax.swing.JComponent;

public class LeftFrame extends JComponent {

    private final Settings settings = Settings.INSTANCE;
    
    
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(settings.displayFrame());
        g.fillPolygon(
            new int[] { getWidth()/2, getWidth(),  getWidth(),  0 },
            new int[] { 0,            0,           getHeight(), getHeight() },
            4);
    }
}
