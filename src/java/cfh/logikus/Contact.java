package cfh.logikus;

import java.awt.Graphics;
import java.util.Objects;
import java.util.stream.Stream;

public class Contact extends Component {
    
    private transient Connection connection = null;
    private transient boolean active = false;

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
    
    public void connected(Connection connection) {
        this.connection = Objects.requireNonNull(connection);
    }
    
    public boolean isConnected() {
        return connection != null;
    }
    
    public Connection connection() {
        return connection;
    }
    
    public void deactive() {
        active = false;
    }
    
    public void active() {
        active = true;
    }
    
    public boolean isActive() {
        return active;
    }
    
    @Override
    public Stream<Contact> contacts() {
        return Stream.of(this);
    }
    
    @Override
    public Stream<Contact> connected(Contact contact) {
        return parent.connected(contact);
    }
}
