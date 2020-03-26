package cfh.jlogikus;

import java.util.Objects;

import javax.swing.JComponent;

public abstract class Component extends JComponent implements Module {
    
    protected final Settings settings = Settings.get();
    
    protected final String id;
    protected final Module parent;
    
    protected Component(String id, Module parent) {
        this.id = Objects.requireNonNull(id);
        this.parent = Objects.requireNonNull(parent);
    }
    
    @Override
    public String id() {
        return id;
    }
    
    @Override
    public void changed(Module module) {
        parent.changed(this);
    }

    @Override
    public String toString() {
        return parent != null ? parent + "." + id : id;
    }
}
