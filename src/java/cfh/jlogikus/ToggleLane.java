package cfh.jlogikus;

import static java.awt.GridBagConstraints.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class ToggleLane extends ModuleImpl {
    
    private final List<ToggleContacts> contacts;
    private final Button button;
    
    private final JComponent contactPanel;
    
    public ToggleLane(String id, Module parent) {
        super(id, parent);
        
        this.contacts = unmodifiableList(
            IntStream.range(0, settings.switchCount())
            .map(i -> 'A' + i*2)
            .mapToObj(s -> new ToggleContacts(id + (char)s + (char)(s+1=='J'?'K':s+1), this))
            .collect(toList())
            );
        this.button = new Button.Toggle(id + "+", this);
        this.contactPanel = new JPanel();
        
        contactPanel.setLayout(new GridBagLayout());
        var insets = settings.insets();
        for (var contact : contacts) {
            contactPanel.add(contact.panel(), new GridBagConstraints(0, RELATIVE, 1, 1, 0.0, 1.0, CENTER, NONE, insets, 20, 0));
        }
    }
    
    public JComponent contactPanel() {
        return contactPanel;
    }
    
    public Button button() {
        return button;
    }
    
    @Override
    public Stream<Contact> contacts() {
        return contacts.stream().flatMap(ToggleContacts::contacts);
    }
    
    @Override
    public Stream<Contact> connected(Contact contact) {
        return contacts
            .stream()
            .filter(t -> t.contacts().anyMatch(c -> c.equals(contact)))
            .findAny()
            .map(s -> s.connected(contact))
            .orElse(Stream.empty());
    }
    
    @Override
    public void changed(Module module) {
        var toggled = button.pressed();
        contacts.forEach(t -> t.toggle(toggled));
        super.changed(module);
    }
}
