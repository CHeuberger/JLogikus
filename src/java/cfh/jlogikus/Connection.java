package cfh.jlogikus;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public class Connection {

    private Settings settings = Settings.get();
    
    private final Contact start;
    private final Contact end;
    private final List<Point2D> points = new ArrayList<>();
    
    private transient boolean active = false;

    public Connection(Contact start, Contact end) {
        this.start = start;
        this.end = end;
    }
    
    public void add(float x, float y) {
        points.add(new Point2D.Float(x, y));
    }

    public void paintComponent(JComponent panel, Graphics2D gg) {
        var w = gg.getClipBounds().width;
        var h = gg.getClipBounds().height;
        var path = new Path2D.Float();
        var x = start.getWidth() / 2 - 1;
        var y = start.getHeight() / 2 - 1;
        var point = SwingUtilities.convertPoint(start, x, y, panel);
        path.moveTo(point.x, point.y);
        for (var p : points) {
            path.lineTo(p.getX() * w, p.getY() * h);
        }
        point = SwingUtilities.convertPoint(end, x, y, panel);
        path.lineTo(point.x, point.y);
        
        gg.setColor(active ? settings.connectionBorderAct() : settings.connectionBorderDeact());
        gg.setStroke(settings.connectionStrokeBorder());
        gg.draw(path);
        gg.setColor(active ? settings.connectionColorAct() : settings.connectionColorDeact());
        gg.setStroke(settings.connectionStroke());
        gg.draw(path);
    }

    public Stream<ContactGroup> connected() {
        return Stream.concat(
            start.group().connected(start),
            end.group().connected(end)
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
