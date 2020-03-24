package cfh.logikus;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class Output extends Component {
    
    private final ContactGroup contacts;
    
    public Output() {
        contacts = new ContactGroup.Horizontal();
        var panel = new JPanel();
        panel.add(contacts);
        
        setLayout(new BorderLayout());
        add(new LampFrame());
        add(panel, BorderLayout.PAGE_END);
        setPreferredSize(settings.outputSize());
    }
    
    
}
