package cfh.jlogikus;

import java.util.stream.Stream;

import javax.swing.BorderFactory;

public class PushLane extends ModuleImpl {

    private final SwitchContact group;
    private final Button button;
    private final EditableLabel label;
    
    public PushLane(String id, Module parent) {
        super(id, parent);
        
        group = new SwitchContact.Vertical(id, this);
        group.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        button = new Button.Push(id + "+", this);
        label = new EditableLabel(id);
    }
    
    public SwitchContact group() {
        return group;
    }
    
    public Button button() {
        return button;
    }
    
    public EditableLabel label() {
        return label;
    }
    
    @Override
    public Stream<ContactGroup> groups() {
        return group.groups();
    }
    
    @Override
    public Stream<ContactGroup> connected(Contact contact) {
        return group.connected(contact);
    }
    
    @Override
    public void changed(Module module) {
        group.closed(button.pressed());
        super.changed(module);
    }
}
