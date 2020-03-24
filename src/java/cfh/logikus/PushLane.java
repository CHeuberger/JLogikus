package cfh.logikus;

import java.util.stream.Stream;

import javax.swing.BorderFactory;

public class PushLane extends Module {

    private final SwitchContact group;
    private final PushButton button;
    
    public PushLane() {
        group = new SwitchContact.Vertical();
        group.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        button = new PushButton();
    }
    
    public SwitchContact group() {
        return group;
    }
    
    public PushButton button() {
        return button;
    }
    
    @Override
    public Stream<Contact> contacts() {
        return group.contacts();
    }
}
