package cfh.logikus;

import java.awt.Insets;

import javax.swing.JComponent;

public abstract class Component extends JComponent {
    
    protected final Settings settings = Settings.INSTANCE;
    protected final Insets insets = new Insets(2, 2, 2, 2);
    
    protected Insets expand(Insets base, int vertical, int horizontal) {
        return new Insets(
            base.top + (vertical+1)/2,
            base.left + (horizontal+1)/2,
            base.bottom + vertical/2,
            base.right + horizontal
            );
    }
}
