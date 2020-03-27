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
    public Stream<Contact> contacts() {
        return Stream.concat(
            contact1.contacts(),
            contact2.contacts());
    }
    
    @Override
    public Stream<Contact> connected(Contact contact) {
        if (closed) {
            return contacts();
        } else {
            return Stream
                .of(contact1, contact2)
                .filter(g -> g.contacts().anyMatch(c -> c.equals(contact)))
                .findAny()
                .map(ContactGroup::contacts)
                .orElse(Stream.empty());
        }
    }

    public void closed(boolean closed) {
        this.closed = closed;
    }

    protected abstract ContactGroup createContact(String id);
    protected abstract void populate();
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public static class Horizontal extends SwitchContact {
        public Horizontal(String id, Module parent) {
            super(id, parent);
        }
        @Override
        protected ContactGroup createContact(String id) {
            return new ContactGroup.Vertical(id, parent);
        }
        @Override
        protected void populate() {
            var insets = settings.insets();
            setLayout(new GridBagLayout());
            add(contact1, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, WEST, NONE, insets, 0, 0));
            add(contact2, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, EAST, NONE, insets, 0, 0));
        }
    }
    
    public static class Vertical extends SwitchContact {
        public Vertical(String id, Module parent) {
            super(id, parent);
        }
        @Override
        protected ContactGroup createContact(String id) {
            return new ContactGroup.Horizontal(id, parent);
        }
        @Override
        protected void populate() {
            var insets = settings.insetsVertical();
            setLayout(new GridBagLayout());
            add(contact1, new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0, NORTH, VERTICAL, insets, 0, 0));
            add(contact2, new GridBagConstraints(0, 1, 1, 1, 0.0, 1.0, SOUTH, VERTICAL, insets, 0, 0));
        }
    }
}
