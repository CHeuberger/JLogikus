package cfh.logikus;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class Source extends Component {

    private final ContactGroup contacts;
    
    public Source() {
        contacts = new ContactGroup.Horizontal();
        var panel = new JPanel();
        panel.add(contacts);
        
        setLayout(new BorderLayout());
        add(new LeftFrame());
        add(panel, BorderLayout.PAGE_END);
    }
    
    
}
