package cfh.logikus;

import java.util.stream.Stream;

public class Output extends ModuleImpl {
    
    private final LampFrame lamp;
    private final ContactGroup group;
    
    public Output(String id, Module module) {
        super(id, module);
        
        lamp = new LampFrame(id + "f", this);
        group = new ContactGroup.Horizontal(id + "_", this);
    }
    
    public LampFrame lamp() {
        return lamp;
    }
    
    public ContactGroup group() {
        return group;
    }
    
    @Override
    public Stream<Contact> contacts() {
        return group.contacts();
    }
}
