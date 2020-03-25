package cfh.logikus;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.SwingUtilities;

public class Connection {

    private Settings settings = Settings.INSTANCE;
    
    private final Contact start;
    private final Contact end;

    public Connection(Contact start, Contact end) {
        this.start = start;
        this.end = end;
    }

    public void paintComponent(Component panel, Graphics2D gg) {
        gg.setColor(settings.connectionColor2());
        gg.setStroke(settings.connectionStroke2());
        int x = start.getWidth() / 2 - 1;
        int y = start.getHeight() / 2 - 1;
        Point ps = SwingUtilities.convertPoint(start, x, y, panel);
        Point pe = SwingUtilities.convertPoint(end, x, y, panel);
        gg.drawLine(ps.x, ps.y, pe.x, pe.y);
        gg.setColor(settings.connectionColor1());
        gg.setStroke(settings.connectionStroke1());
        gg.drawLine(ps.x, ps.y, pe.x, pe.y);
    }
}
