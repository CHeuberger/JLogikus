package cfh.jlogikus;

import java.awt.Graphics;

import javax.swing.JComponent;

public class RightFrame extends JComponent {

    private final Settings settings = Settings.get();

    public RightFrame() {
        setPreferredSize(settings.rightSize());
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(settings.displayFrame());
        g.fillPolygon(
            new int[] { 0, getWidth(),  0 },
            new int[] { 0, getHeight(), getHeight() },
            3);
    }
}
