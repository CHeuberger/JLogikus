package cfh.logikus;

import static java.awt.GridBagConstraints.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;

public class PushLane extends Component {

    private final SwitchContact contacts;
    private final PushButton button;
    
    public PushLane() {
        contacts = new SwitchContact.Vertical();
        button = new PushButton();
        
        contacts.setBorder(BorderFactory.createRaisedBevelBorder());

        var insets = new Insets(0, 0, 0, 0);
        
        setLayout(new GridBagLayout());
        add(contacts, new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0, NORTH, NONE, insets, 0, 0));
        add(button, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, CENTER, NONE, insets, 0, 0));
    }
}
