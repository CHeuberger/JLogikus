package cfh.logikus;

import java.awt.Insets;

public abstract class Module {

    protected Settings settings = Settings.INSTANCE;
    protected final Insets insets = settings.insets();
}
