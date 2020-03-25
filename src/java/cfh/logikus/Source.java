package cfh.logikus;

import java.util.stream.Stream;

public class Source extends Module {

    private final ContactGroup group;
    
    public Source(String id) {
        super(id);
        
        group = new ContactGroup.Horizontal(this);
    }
    
    public ContactGroup group() {
        return group;
    }
    
    @Override
    public Stream<Contact> contacts() {
        return group.contacts();
    }
}
