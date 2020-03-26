package cfh.jlogikus;

import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.swing.BorderFactory;

public abstract class ContactGroup extends Component {
    
    private final List<Contact> contacts;
    
    private ContactGroup(String id, Module parent) {
        super(id, parent);
        
        this.contacts = unmodifiableList(
            IntStream
            .range(0, settings.groupCount())
            .mapToObj(i -> new Contact(id + i, this))
            .collect(toList())
            );

        var border = settings.groupBorder();
        setBorder(BorderFactory.createEmptyBorder(border, border, border, border));
        setLayout(createLayout());
        contacts.forEach(this::add);
    }

    @Override
    public Stream<Contact> contacts() {
        return contacts.stream();
    }
    
    @Override
    public Stream<Contact> connected(Contact contact) {
        if (contacts.contains(contact)) {
            return parent.connected(contact);
        } else {
            return Stream.empty();
        }
    }
    
    protected abstract LayoutManager createLayout();
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public static class Horizontal extends ContactGroup {
        public Horizontal(String id, Module parent) {
            super(id, parent);
        }
        @Override
        public LayoutManager createLayout() {
            return new GridLayout(1, 0);
        }
    }
    
    public static class Vertical extends ContactGroup {
        public Vertical(String id, Module parent) {
            super(id, parent);
        }
        @Override
        public LayoutManager createLayout() {
            return new GridLayout(0, 1);
        }
    }
}
