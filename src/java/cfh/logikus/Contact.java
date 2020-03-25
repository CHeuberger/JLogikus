package cfh.logikus;

import java.awt.Graphics;
import java.util.stream.Stream;

public class Contact extends Component {
    
    private final ContactGroup group;
    
    private transient boolean connected = false;
    private transient boolean active = false;

    public Contact(ContactGroup group) {
        super(group.module());
        this.group = group;
        
        setPreferredSize(settings.contactSize());
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        var r = settings.contactRadius();
        g.setColor(settings.contactColor());
        g.fillOval(getWidth()/2-r-1, getHeight()/2-r-1, r+r-1, r+r-1);
    }
    
    public void disconnected() {
        connected = false;
    }
    
    public void connected() {
        connected = true;
    }
    
    public boolean isConnected() {
        return connected;
    }
    
    public void deactivate() {
        active = false;
    }
    
    public void activate() {
        active = true;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public Stream<Contact> contacts() {
        return module.contacts();
    }
    
    @Override
    public String toString() {
        return group.toString();  // TODO count and so
    }
}
