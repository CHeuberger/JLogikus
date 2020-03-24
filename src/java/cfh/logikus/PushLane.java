package cfh.logikus;

import javax.swing.BorderFactory;

public class PushLane extends Module {

    private final SwitchContact contact;
    private final PushButton button;
    
    public PushLane() {
        contact = new SwitchContact.Vertical();
        contact.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        button = new PushButton();
    }
    
    public SwitchContact contact() {
        return contact;
    }
    
    public PushButton button() {
        return button;
    }
}
