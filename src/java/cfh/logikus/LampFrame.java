package cfh.logikus;

import java.awt.Graphics;
import java.util.stream.Stream;

public class LampFrame extends Component {
    
    private boolean light;
    
    public LampFrame(String id, Module parent) {
        super(id, parent);
        
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

    @Override
    public Stream<Contact> contacts() { return Stream.empty(); }
}
