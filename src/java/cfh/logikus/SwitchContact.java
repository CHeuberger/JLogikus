package cfh.logikus;

import static java.awt.GridBagConstraints.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

public abstract class SwitchContact extends Component {

    protected final ContactGroup contact1;
    protected final ContactGroup contact2;
    
    private SwitchContact() {
        contact1 = createContact();
        contact2 = createContact();
        
        populate();
    }
    
    protected abstract ContactGroup createContact();
    protected abstract void populate();
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public static class Horizontal extends SwitchContact {
        @Override
        protected ContactGroup createContact() {
            return new ContactGroup.Vertical();
        }
        @Override
        protected void populate() {
            setLayout(new GridBagLayout());
            add(contact1, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, WEST, NONE, insets, 0, 0));
            add(contact2, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, EAST, NONE, insets, 0, 0));
        }
    }
    
    public static class Vertical extends SwitchContact {
        @Override
        protected ContactGroup createContact() {
            return new ContactGroup.Horizontal();
        }
        @Override
        protected void populate() {
            setLayout(new GridLayout(0, 1));
            add(contact1);
            add(contact2);
        }
    }
}
