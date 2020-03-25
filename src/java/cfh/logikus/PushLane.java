package cfh.logikus;

import java.util.stream.Stream;

import javax.swing.BorderFactory;

public class PushLane extends ModuleImpl {

    private final SwitchContact group;
    private final PushButton button;
    
    public PushLane(String id, Module parent) {
        super(id, parent);
        
        group = new SwitchContact.Vertical(id, this);
        group.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        button = new PushButton(id + "+", this);
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
