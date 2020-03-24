package cfh.logikus;

import static java.awt.GridBagConstraints.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class PushLane extends JComponent {

    private final ContactGroup contactA = new ContactGroup(Orient.HORIZONTAL);
    private final ContactGroup contactB = new ContactGroup(Orient.HORIZONTAL);
    private final PushButton button = new PushButton();
    
    public PushLane() {
        var panel = new JPanel();
        panel.setBorder(BorderFactory.createRaisedBevelBorder());
        panel.setLayout(new GridLayout(0, 1));
        panel.add(contactA);
        panel.add(contactB);
        
        var insets = new Insets(0, 0, 0, 0);
        
        setLayout(new GridBagLayout());
        add(panel, new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0, NORTH, NONE, insets, 0, 0));
        add(button, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, CENTER, NONE, insets, 0, 0));
    }
}
