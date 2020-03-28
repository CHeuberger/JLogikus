package cfh.jlogikus;

import java.awt.Graphics;
import java.util.Objects;
import java.util.stream.Stream;

public class Contact extends Component {
    
    private transient Connection connection = null;

    public Contact(String id, ContactGroup group) {
        super(id, group);
        
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
        connection = null;
    }
    
    public void connected(Connection to) {
        this.connection = Objects.requireNonNull(to);
    }
    
    public boolean isConnected() {
        return connection != null;
    }
    
    public Connection connection() {
        return connection;
    }
    
    public void clear() {
        connection = null;
    }
    
    public ContactGroup group() {
        return (ContactGroup) parent;
    }
    
    @Override
    public Stream<ContactGroup> groups() {
        throw new RuntimeException("not implemented");
//        return parent.groups();
    }
    
    @Override
    public Stream<ContactGroup> connected(Contact contact) {
        throw new RuntimeException("not implemented");
//        return parent.connected(contact);
    }
}
