package cfh.logikus;

import static java.awt.GridBagConstraints.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.stream.Stream;

public abstract class SwitchContact extends Component {

    protected final ContactGroup contact1;
    protected final ContactGroup contact2;
    
    private SwitchContact(Module module) {
        super(module);
        
        contact1 = createContact();
        contact2 = createContact();
        
        populate();
    }
    
    public Stream<Contact> contacts() {
        return Stream.concat(
            contact1.contacts(),
            contact2.contacts());
    }
        
    protected abstract ContactGroup createContact();
    protected abstract void populate();
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public static class Horizontal extends SwitchContact {
        public Horizontal(Module module) { super(module); }
        @Override
        protected ContactGroup createContact() {
            return new ContactGroup.Vertical(module);
        }
        @Override
        protected void populate() {
            Insets insets = settings.insets();
            setLayout(new GridBagLayout());
            add(contact1, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, WEST, NONE, insets, 0, 0));
            add(contact2, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, EAST, NONE, insets, 0, 0));
        }
    }
    
    public static class Vertical extends SwitchContact {
        public Vertical(Module module) { super(module); }
        @Override
        protected ContactGroup createContact() {
            return new ContactGroup.Horizontal(module);
        }
        @Override
        protected void populate() {
            setLayout(new GridLayout(0, 1));
            add(contact1);
            add(contact2);
        }
    }
}
