package cfh.logikus;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class Source extends JComponent {

    private final ContactGroup contacts;
    
    public Source(Orient orient) {
        contacts = new ContactGroup(orient);
        var panel = new JPanel();
        panel.add(contacts);
        
        setLayout(new BorderLayout());
        add(new LeftFrame());
        add(panel, BorderLayout.PAGE_END);
    }
    
    
}
