package cfh.logikus;

import static java.awt.GridBagConstraints.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class ToggleContacts extends Module {

    private final SwitchContact open;
    private final SwitchContact closed;

    private final transient JComponent panel;
    
    public ToggleContacts() {
        open = new SwitchContact.Horizontal();
        closed = new SwitchContact.Horizontal();
        
        panel = new JPanel();
        panel.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        panel.setLayout(new GridBagLayout());
        panel.add(open,   new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, SOUTH, HORIZONTAL, insets , 0, 0));
        panel.add(closed, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, NORTH, HORIZONTAL, insets , 0, 0));
    }
    
    public JComponent panel() {
        return panel;
    }
    
    @Override
    public Stream<Contact> contacts() {
        return Stream.concat(
            open.contacts(),
            closed.contacts()
            );
    }
}
