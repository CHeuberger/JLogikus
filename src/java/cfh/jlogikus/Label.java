package cfh.jlogikus;

import javax.swing.JLabel;

public class Label extends JLabel {

    public Label(String text) {
        super(text);
        setToolTipText(text);
        setHorizontalAlignment(CENTER);
    }
}
