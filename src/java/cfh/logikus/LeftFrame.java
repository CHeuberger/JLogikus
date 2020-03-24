package cfh.logikus;

import java.awt.Graphics;

public class LeftFrame extends Component {
    
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(settings.displayFrame());
        g.fillPolygon(
            new int[] { getWidth()/2, getWidth(),  getWidth(),  0 },
            new int[] { 0,            0,           getHeight(), getHeight() },
            4);
    }
}
