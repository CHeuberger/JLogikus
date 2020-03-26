package cfh.jlogikus;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.stream.Stream;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public class Connection {

    private Settings settings = Settings.get();
    
    private final Contact start;
    private final Contact end;
    
    private transient boolean active = false;

    public Connection(Contact start, Contact end) {
        this.start = start;
        this.end = end;
    }

    public void paintComponent(JComponent panel, Graphics2D gg) {
        gg.setColor(active ? settings.connectionBorderAct() : settings.connectionBorderDeact());
        gg.setStroke(settings.connectionStrokeBorder());
        int x = start.getWidth() / 2 - 1;
        int y = start.getHeight() / 2 - 1;
        Point ps = SwingUtilities.convertPoint(start, x, y, panel);
        Point pe = SwingUtilities.convertPoint(end, x, y, panel);
        gg.drawLine(ps.x, ps.y, pe.x, pe.y);
        gg.setColor(settings.connectionColor());
        gg.setStroke(settings.connectionStroke());
        gg.drawLine(ps.x, ps.y, pe.x, pe.y);
    }

    public Stream<Contact> connected() {
        return Stream.concat(
            start.connected(start),
            end.connected(end)
            );
    }
    
    public Contact start() {
        return start;
    }
    
    public Contact end() {
        return end;
    }
    
    public void deactive() {
        active = false;
    }
    
    public void active() {
        active = true;
    }
    
    public boolean isActive() {
        return active;
    }

    @Override
    public String toString() {
        return start + "--" + end;
    }
}
