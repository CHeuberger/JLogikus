package cfh.logikus;

import java.util.stream.Stream;

public interface Module {

    public String id();
    
    public Stream<Contact> contacts();
    
    public Stream<Contact> connected(Contact contact);
    
    public void changed(Module module);
}
