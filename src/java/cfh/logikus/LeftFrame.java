package cfh.logikus;

import java.awt.Graphics;

import javax.swing.JComponent;

public class LeftFrame extends JComponent {

    private final Settings settings = Settings.get();
    
    public LeftFrame() {
        setPreferredSize(settings.leftSize());
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(settings.displayFrame());
        g.fillPolygon(
            new int[] { getWidth(), getWidth(),  0 },
            new int[] { 0,          getHeight(), getHeight() },
            3);
    }
}
