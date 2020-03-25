package cfh.logikus;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.stream.Stream;

import javax.swing.BorderFactory;

public class PushButton extends Component {
    
    private boolean pressed = false;
    
    public PushButton(String id, Module parent) {
        super(id, parent);
        
        var unpressedBorder = BorderFactory.createRaisedBevelBorder();
        var pressedBorder = BorderFactory.createLoweredBevelBorder();
        
        setBorder(unpressedBorder);
        setPreferredSize(settings.buttonSize());
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent ev) {
                if (ev.getButton() == ev.BUTTON1) {
                    pressed = true;
                    changed();
                }
            }
            @Override
            public void mouseReleased(MouseEvent ev) {
                if (ev.getButton() == ev.BUTTON1) {
                    pressed = false;
                    changed();
                }
            }
            @Override
            public void mouseClicked(MouseEvent ev) {
                if (ev.getButton() == ev.BUTTON3) {
                    pressed = !pressed;
                    changed();
                }
            }
            private void changed() {
                setBorder(pressed ? pressedBorder : unpressedBorder);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        var h = getHeight();
        g.setColor(pressed ? settings.buttonPressed() : settings.buttonColor());
        g.fillRoundRect(3, 3, getWidth()-6, h-6, 6, 6);
    }
    
    @Override
    public Stream<Contact> contacts() {
        return Stream.empty();
    }
}
