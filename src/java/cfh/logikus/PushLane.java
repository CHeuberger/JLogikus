package cfh.logikus;

import java.util.stream.Stream;

import javax.swing.BorderFactory;

public class PushLane extends Module {

    private final SwitchContact group;
    private final PushButton button;
    
    public PushLane(String id) {
        super(id);
        
        group = new SwitchContact.Vertical(this);
        group.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        button = new PushButton(this);
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
