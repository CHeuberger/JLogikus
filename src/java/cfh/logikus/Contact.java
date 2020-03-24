package cfh.logikus;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Contact extends Component {

    private final transient Cursor cursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
    
    public Contact() {
        setPreferredSize(settings.contactSize());
        addMouseListener(new MouseAdapter() {
            private Cursor previous = null;
            @Override
            public void mouseEntered(MouseEvent ev) {
                previous = getCursor();
                setCursor(cursor);
            }
            @Override
            public void mouseExited(MouseEvent ev) {
                setCursor(previous);
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        var r = settings.contactRadius();
        g.setColor(settings.contactColor());
        g.fillOval(getWidth()/2-r-1, getHeight()/2-r-1, r+r-1, r+r-1);
    }
}
