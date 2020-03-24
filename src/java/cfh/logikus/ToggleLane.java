package cfh.logikus;

import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

import java.awt.GridLayout;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class ToggleLane extends Module {
    
    private final List<SwitchContact> contacts;
    private final ToggleButton button;
    
    private final JComponent contactPanel;
    
    public ToggleLane() {
        this.contacts = unmodifiableList(
            Stream.generate(SwitchContact.HorizontalDouble::new).limit(settings.switchCount()).collect(toList())
            );
        this.button = new ToggleButton();
        this.contactPanel = new JPanel();
        
        contactPanel.setLayout(new GridLayout(0, 1));
        contactPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        contacts.forEach(contactPanel::add);
    }
    
    public JComponent contactPanel() {
        return contactPanel;
    }
    
    public ToggleButton button() {
        return button;
    }
}
