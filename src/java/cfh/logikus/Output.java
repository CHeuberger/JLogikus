package cfh.logikus;

import java.util.stream.Stream;

public class Output extends Module {
    
    private final LampFrame lamp;
    private final ContactGroup group;
    
    public Output() {
        lamp = new LampFrame();
        group = new ContactGroup.Horizontal();
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
