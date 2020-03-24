package cfh.logikus;

import static java.util.stream.Collectors.*;

import java.awt.GridLayout;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

public abstract class ContactGroup extends JComponent {

    private final Settings settings = Settings.INSTANCE;
    
    private final List<Contact> contacts;
    
    protected ContactGroup() {
        this.contacts = Collections.unmodifiableList(
            Stream.generate(Contact::new).limit(settings.groupCount()).collect(toList())
            );

        var border = settings.groupBorder();
        setBorder(BorderFactory.createEmptyBorder(border, border, border, border));
        contacts.forEach(this::add);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public static class Horizontal extends ContactGroup {
        public Horizontal() {
            setLayout(new GridLayout(1, 0));
        }
    }
}
