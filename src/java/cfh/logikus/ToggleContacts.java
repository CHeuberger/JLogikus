package cfh.logikus;

import static java.awt.GridBagConstraints.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class ToggleContacts extends ModuleImpl {

    private final SwitchContact open;
    private final SwitchContact closed;

    protected transient boolean toggled = false;
    
    private final transient JComponent panel;
    
    public ToggleContacts(String id, Module parent) {
        super(id, parent);
        var id1 = id.length()>=2 ? id.substring(0, id.length()-1) : id + "A";
        var id2 = id.length()>=2 ? id.substring(0,  id.length()-2)+id.charAt(id.length()-1) : id + "B";
        open = new SwitchContact.Horizontal(id1, this);
        closed = new SwitchContact.Horizontal(id2, this);
        
        panel = new JPanel();
        var insets = settings.insets();
        panel.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        panel.setLayout(new GridBagLayout());
        panel.add(open,   new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, SOUTH, HORIZONTAL, insets , 0, 0));
        panel.add(closed, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, NORTH, HORIZONTAL, insets , 0, 0));
    }
    
    public JComponent panel() {
        return panel;
    }
    
    public void toggle(boolean toggled) {
        this.toggled = toggled;
        open.closed(toggled);
        closed.closed(!toggled);
    }
    
    @Override
    public Stream<Contact> contacts() {
        return Stream.concat(
            open.contacts(),
            closed.contacts()
            );
    }
    
    @Override
    public Stream<Contact> connected(Contact contact) {
        return Stream
            .of(open, closed)
            .filter(g -> g.contacts().anyMatch(c -> c.equals(contact)))
            .findAny()
            .map(s -> s.connected(contact))
            .orElse(Stream.empty());
    }
}
