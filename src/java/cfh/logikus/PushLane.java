package cfh.logikus;

import java.util.stream.Stream;

import javax.swing.BorderFactory;

public class PushLane extends ModuleImpl {

    private final SwitchContact group;
    private final Button button;
    
    public PushLane(String id, Module parent) {
        super(id, parent);
        
        group = new SwitchContact.Vertical(id, this);
        group.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        button = new Button.Push(id + "+", this);
    }
    
    public SwitchContact group() {
        return group;
    }
    
    public Button button() {
        return button;
    }
    
    @Override
    public Stream<Contact> contacts() {
        return group.contacts();
    }
    
    @Override
    public Stream<Contact> connected(Contact contact) {
        return contacts();
    }
    
    @Override
    public void changed(Module module) {
        group.closed(button.pressed());
        super.changed(module);
    }
}
