package cfh.logikus;

import java.awt.Graphics;

public class RightFrame extends Component {
    
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(settings.displayFrame());
        g.fillPolygon(
            new int[] { 0, 3*getWidth()/4,  getWidth(),  0 },
            new int[] { 0, 0,               getHeight(), getHeight() },
            4);
    }
}
