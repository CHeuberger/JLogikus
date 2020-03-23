package cfh.logikus;

import java.awt.Graphics;

import javax.swing.JComponent;

public class LampFrame extends JComponent {

    private final Settings settings = Settings.INSTANCE;
    
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(settings.displayFrame());
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
