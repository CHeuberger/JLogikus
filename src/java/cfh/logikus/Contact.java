package cfh.logikus;

import java.awt.Graphics;

import javax.swing.JComponent;

public class Contact extends JComponent {
    
    private final Settings settings = Settings.INSTANCE;

    public Contact() {
        setPreferredSize(settings.contactSize());
        setOpaque(false);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        var r = settings.contactRadius();
        g.setColor(settings.contactColor());
        g.fillOval(getWidth()/2-r-1, getHeight()/2-r-1, r+r-1, r+r-1);
    }
}
