package cfh.logikus;

import java.util.Objects;

import javax.swing.JComponent;

public abstract class Component extends JComponent {
    
    protected final Settings settings = Settings.INSTANCE;
    
    protected final Module module;
    
    protected Component(Module module) {
        this.module = Objects.requireNonNull(module);
    }
    
    public Module module() { return module; }
}
