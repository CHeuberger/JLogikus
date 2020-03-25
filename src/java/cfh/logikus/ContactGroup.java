package cfh.logikus;

import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.BorderFactory;

public abstract class ContactGroup extends Component {
    
    private final List<Contact> contacts;
    
    private ContactGroup(Module module) {
        super(module);
        
        this.contacts = unmodifiableList(
            Stream.generate(() -> this).map(Contact::new).limit(settings.groupCount()).collect(toList())
            );

        var border = settings.groupBorder();
        setBorder(BorderFactory.createEmptyBorder(border, border, border, border));
        setLayout(createLayout());
        contacts.forEach(this::add);
    }

    public Stream<Contact> contacts() {
        return contacts.stream();
    }
    
    @Override
    public String toString() {
        return module.toString();
    }
    
    protected abstract LayoutManager createLayout();
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public static class Horizontal extends ContactGroup {
        public Horizontal(Module module) { super(module); }
        @Override
        public LayoutManager createLayout() {
            return new GridLayout(1, 0);
        }
    }
    
    public static class Vertical extends ContactGroup {
        public Vertical(Module module) { super(module); }
        @Override
        public LayoutManager createLayout() {
            return new GridLayout(0, 1);
        }
    }
}
