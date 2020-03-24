package cfh.logikus;

import java.awt.Graphics;

public class LampFrame extends Component {
    
    private boolean light;
    
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(settings.displayFrame());
        g.fillRect(0, 0, getWidth(), getHeight());
        if (light) {
            g.setColor(settings.light());
            g.fillRect(10, 10, getWidth()-21, getHeight()-21);
        }
    }
}
