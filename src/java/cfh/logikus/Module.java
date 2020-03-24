package cfh.logikus;

import java.awt.Insets;
import java.util.stream.Stream;

public abstract class Module {

    protected Settings settings = Settings.INSTANCE;
    protected final Insets insets = settings.insets();

    public abstract Stream<Contact> contacts();
}
