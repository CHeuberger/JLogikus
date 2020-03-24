package cfh.logikus;

import java.awt.Graphics;

public class LampFrame extends Component {
    
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(settings.displayFrame());
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
