package cfh.logikus;

import java.awt.GridLayout;
import java.awt.LayoutManager;

public abstract class SwitchContact extends Component {

    private final ContactGroup contact1;
    private final ContactGroup contact2;
    
    private SwitchContact() {
        contact1 = new ContactGroup.Horizontal();
        contact2 = new ContactGroup.Horizontal();
        
        setLayout(createLayout());
        add(contact1);
        add(contact2);
    }
    
    protected abstract LayoutManager createLayout();
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public static class Horizontal extends SwitchContact {
        @Override
        public LayoutManager createLayout() {
            return new GridLayout(1, 0);
        }
    }
    
    public static class Vertical extends SwitchContact {
        @Override
        public LayoutManager createLayout() {
            return new GridLayout(0, 1);
        }
    }
}
