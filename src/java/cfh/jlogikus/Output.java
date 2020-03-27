package cfh.jlogikus;

import java.util.stream.Stream;

public class Output extends ModuleImpl {
    
    private final LampFrame lamp;
    private final ContactGroup group;
    private final EditableLabel label;
    
    public Output(String id, Module module) {
        super(id, module);
        
        lamp = new LampFrame(id + "l", this);
        group = new ContactGroup.Horizontal(id, this);
        label = new EditableLabel(id);
    }
    
    public LampFrame lamp() {
        return lamp;
    }
    
    public ContactGroup group() {
        return group;
    }
    
    public EditableLabel label() {
        return label;
    }
    
    @Override
    public Stream<Contact> contacts() {
        return group.contacts();
    }
    
    @Override
    public Stream<Contact> connected(Contact contact) {
        return contacts();
    }
}
