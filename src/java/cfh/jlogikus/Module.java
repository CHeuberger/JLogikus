package cfh.jlogikus;

import java.util.stream.Stream;

public interface Module {

    /** The ID of this module. */
    public String id();
    
    /** Return all contacts of this module, including submodules. */
    public Stream<ContactGroup> groups();
    
    /** Return all contacts connected to the given contact. */
    public Stream<ContactGroup> connected(Contact contact);
    
    /** Indicates that a submodule was changed regarding its connections. */
    public void changed(Module module);
}
