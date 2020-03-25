package cfh.logikus;

import java.util.Objects;

public abstract class ModuleImpl implements Module {

    protected Settings settings = Settings.INSTANCE;
    
    protected final String id;
    protected final Module parent;
    
    protected ModuleImpl(String id, Module parent) {
        this.id = Objects.requireNonNull(id);
        this.parent = parent;
    }
    
    @Override
    public String id() { 
        return id;
    }
    
    @Override
    public void changed(Module module) {
        parent.changed(module);
    }
    
    @Override
    public String toString() {
        return parent != null ? parent + "." + id : id;
    }
}
