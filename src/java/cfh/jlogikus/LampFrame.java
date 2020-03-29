package cfh.jlogikus;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.stream.Stream;

public class LampFrame extends Component {
    
    private Image image = null;
    private Rectangle imgBounds = null;
    
    public LampFrame(String id, Module parent) {
        super(id, parent);
        
        setPreferredSize(settings.lampSize());
    }
    
    public void clear() {
        image = null;
    }
    
    public void image(Image img, Rectangle bounds) {
        image = img;
        imgBounds = bounds;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(settings.displayFrame());
        g.fillRect(0, 0, getWidth(), getHeight());
        var x = 5;
        var y = 10;
        var w = getWidth()-10;
        var h = getHeight()-20;
        if (image == null) {
            if (parent.groups().anyMatch(ContactGroup::isActive)) {
                g.setColor(settings.lampLight());
                g.fillRect(x, y, w, h);
            }
        } else {
            if (parent.groups().anyMatch(ContactGroup::isActive)) {
                g.setColor(settings.imgBackgroundAct());
            } else {
                g.setColor(settings.imgBackgroundDeact());
            }
            g.fillRect(x, y, w, h);
            g.drawImage(image,
                    x, y, x+w, y+h,
                    imgBounds.x, imgBounds.y, imgBounds.x+imgBounds.width, imgBounds.y+imgBounds.height,
                    this);
        }
    }

    @Override
    public Stream<ContactGroup> groups() {
        return Stream.empty();
    }
    
    @Override
    public Stream<ContactGroup> connected(Contact contact) {
        return Stream.empty();
    }
}
