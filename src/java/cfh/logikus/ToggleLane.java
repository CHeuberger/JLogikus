package cfh.logikus;

import static java.awt.GridBagConstraints.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class ToggleLane extends Module {
    
    private final List<ToggleContacts> contacts;
    private final ToggleButton button;
    
    private final JComponent contactPanel;
    
    public ToggleLane() {
        this.contacts = unmodifiableList(
            Stream.generate(ToggleContacts::new).limit(settings.switchCount()).collect(toList())
            );
        this.button = new ToggleButton();
        this.contactPanel = new JPanel();
        
        contactPanel.setLayout(new GridBagLayout());
        for (var contact : contacts) {
            contactPanel.add(contact.panel(), new GridBagConstraints(0, RELATIVE, 1, 1, 0.0, 1.0, CENTER, NONE, insets, 20, 0));
        }
    }
    
    public JComponent contactPanel() {
        return contactPanel;
    }
    
    public ToggleButton button() {
        return button;
    }
    
    @Override
    public Stream<Contact> contacts() {
        return contacts.stream().flatMap(ToggleContacts::contacts);
    }
}
