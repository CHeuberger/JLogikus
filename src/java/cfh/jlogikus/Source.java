package cfh.jlogikus;

import java.util.stream.Stream;

public class Source extends ModuleImpl {

    private final ContactGroup group;
    private final Label label;
    
    public Source(String id, Module parent) {
        super(id, parent);
        
        group = new ContactGroup.Horizontal(id, this);
        label = new Label(id);
    }
    
    public ContactGroup group() {
        return group;
    }
    
    public Label label() {
        return label;
    }
    
    @Override
    public Stream<ContactGroup> groups() {
        return Stream.of(group);
    }
    
    @Override
    public Stream<ContactGroup> connected(Contact contact) {
        return groups();
    }
}
