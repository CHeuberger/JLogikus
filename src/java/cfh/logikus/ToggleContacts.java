package cfh.logikus;

import java.awt.GridLayout;

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
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(2, 20, 2, 20),
            BorderFactory.createRaisedSoftBevelBorder()
            ));
        panel.setLayout(new GridLayout(0, 1));
        panel.add(open);
        panel.add(closed);
    }
    
    public JComponent panel() {
        return panel;
    }
}
