package cfh.jlogikus;

import static java.awt.GridBagConstraints.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.stream.Stream;

public abstract class SwitchContact extends Component {

    protected final ContactGroup contact1;
    protected final ContactGroup contact2;
    
    protected transient boolean closed = false;
    
    private SwitchContact(String id, Module parent) {
        super(id, parent);
        
        contact1 = createContact(id + "a");
        contact2 = createContact(id + "b");
        
        populate();
    }
    
    @Override
    public Stream<ContactGroup> groups() {
        return Stream.of(contact1, contact2);
    }
    
    @Override
    public Stream<ContactGroup> connected(Contact contact) {
        if (closed) {
            return groups();
        } else {
            return groups().filter(g -> g.contacts().anyMatch(c -> c.equals(contact)));
        }
    }

    public void closed(boolean close) {
        closed = close;
    }

    protected abstract ContactGroup createContact(String id0);
    protected abstract void populate();
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public static class Horizontal extends SwitchContact {
        public Horizontal(String id, Module parent) {
            super(id, parent);
        }
        @Override
        protected ContactGroup createContact(String id0) {
            return new ContactGroup.Vertical(id0, parent);
        }
        @Override
        protected void populate() {
            var insets = settings.insets();
            setLayout(new GridBagLayout());
            add(contact1, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, WEST, NONE, insets, 0, 0));
            add(contact2, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, EAST, NONE, insets, 0, 0));
        }
    }
    
    //------------------------------------------------------------------------------------------------------------------
    
    public static class Vertical extends SwitchContact {
        public Vertical(String id, Module parent) {
            super(id, parent);
        }
        @Override
        protected ContactGroup createContact(String id0) {
            return new ContactGroup.Horizontal(id0, parent);
        }
        @Override
        protected void populate() {
            var insets = settings.insets();
            setLayout(new GridBagLayout());
            add(contact1, new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0, NORTH, NONE, insets, 0, 0));
            add(contact2, new GridBagConstraints(0, 1, 1, 1, 0.0, 1.0, NORTH, NONE, insets, 0, 0));
        }
    }
}
