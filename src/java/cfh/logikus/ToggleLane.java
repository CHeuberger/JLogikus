package cfh.logikus;

import static java.awt.GridBagConstraints.*;
import static java.util.stream.Collectors.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.JPanel;

public class ToggleLane extends Component {
    
    private final List<ToggleContact> contacts;
    private final ToggleButton button;
    
    public ToggleLane() {
        this.contacts = Collections.unmodifiableList(
            Stream.generate(ToggleContact::new).limit(settings.toggleCount()).collect(toList())
            );
        this.button = new ToggleButton();
        
        var panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));
        contacts.forEach(panel::add);
        
        var insets = new Insets(0, 0, 0, 0);
        
        setLayout(new GridBagLayout());
        add(panel, new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0, NORTH, NONE, insets, 0, 0));
        add(button, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, CENTER, NONE, insets, 0, 0));
    }
}
