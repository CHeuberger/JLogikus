package cfh.logikus;

import java.util.stream.Stream;

public class Source extends Module {

    private final ContactGroup group;
    
    public Source() {
        group = new ContactGroup.Horizontal();
    }
    
    public ContactGroup group() {
        return group;
    }
    
    @Override
    public Stream<Contact> contacts() {
        return group.contacts();
    }
}
