package cfh.logikus;

import java.awt.Graphics;
import javax.swing.JComponent;

public class RightFrame extends JComponent {

    private final Settings settings = Settings.INSTANCE;
    
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(settings.displayFrame());
        g.fillPolygon(
            new int[] { 0, 3*getWidth()/4,  getWidth(),  0 },
            new int[] { 0, 0,               getHeight(), getHeight() },
            4);
    }
}
