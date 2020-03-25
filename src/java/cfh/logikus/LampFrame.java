package cfh.logikus;

import java.awt.Graphics;

public class LampFrame extends Component {
    
    private boolean light;
    
    public LampFrame(Module module) {
        super(module);
        
        setPreferredSize(settings.lampSize());
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(settings.displayFrame());
        g.fillRect(0, 0, getWidth(), getHeight());
        if (light) {
            g.setColor(settings.lampLight());
            g.fillRect(10, 10, getWidth()-21, getHeight()-21);
        }
    }
}
