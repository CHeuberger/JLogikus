package cfh.logikus;

import java.awt.Insets;
import java.util.Objects;
import java.util.stream.Stream;

public abstract class Module {

    protected Settings settings = Settings.INSTANCE;
    protected final Insets insets = settings.insets();
    
    public final String id;
    
    protected Module(String id) {
        this.id = Objects.requireNonNull(id);
    }
    
    public String id() { return id; }
    
    @Override
    public String toString() { return id; }

    public abstract Stream<Contact> contacts();
}
