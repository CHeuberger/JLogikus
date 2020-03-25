package cfh.logikus;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.stream.Stream;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public class Connection {

    private Settings settings = Settings.INSTANCE;
    
    private final Contact start;
    private final Contact end;
    
    private transient boolean active = false;

    public Connection(Contact start, Contact end) {
        this.start = start;
        this.end = end;
    }

    public void paintComponent(JComponent panel, Graphics2D gg) {
        gg.setColor(settings.connectionColor2());
        gg.setStroke(settings.connectionStroke2());
        int x = start.getWidth() / 2 - 1;
        int y = start.getHeight() / 2 - 1;
        Point ps = SwingUtilities.convertPoint(start, x, y, panel);
        Point pe = SwingUtilities.convertPoint(end, x, y, panel);
        gg.drawLine(ps.x, ps.y, pe.x, pe.y);
        gg.setColor(active ? Color.RED : settings.connectionColor1());  // XXX
        gg.setStroke(settings.connectionStroke1());
        gg.drawLine(ps.x, ps.y, pe.x, pe.y);
    }

    public boolean update() {
        active = start.isActive() || end.isActive();
        boolean changed;
        if (active) {
            changed = !start.isActive() || !end.isActive();
            start.active();
            end.active();
        } else {
            changed = false;
        }
        return changed;
    }
    
    public Stream<Contact> contacts() {
        return Stream.concat(
            start.contacts(),
            end.contacts()
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
