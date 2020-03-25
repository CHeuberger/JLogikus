package cfh.logikus;

import java.util.stream.Stream;

public class Source extends ModuleImpl {

    private final ContactGroup group;
    
    public Source(String id, Module parent) {
        super(id, parent);
        
        group = new ContactGroup.Horizontal(id + "_", this);
    }
    
    public ContactGroup group() {
        return group;
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
