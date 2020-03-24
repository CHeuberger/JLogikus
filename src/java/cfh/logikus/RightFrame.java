package cfh.logikus;

import java.awt.Graphics;

public class RightFrame extends Component {

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
