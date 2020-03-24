package cfh.logikus;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class Output extends Component {
    
    private final LampFrame lamp;
    private final ContactGroup contacts;
    
    public Output() {
        lamp = new LampFrame();
        contacts = new ContactGroup.Horizontal();
        
        var panel = new JPanel();
        panel.add(contacts);
        
        setLayout(new BorderLayout());
        add(lamp);
        add(panel, BorderLayout.PAGE_END);
        setPreferredSize(settings.outputSize());
    }
}
