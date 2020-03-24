package cfh.logikus;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class PushButton extends Component {
    
    private boolean pressed = false;
    
    private final Border unpressedBorder;
    private final Border pressedBorder;
    
    public PushButton() {
        unpressedBorder = BorderFactory.createRaisedBevelBorder();
        pressedBorder = BorderFactory.createLoweredBevelBorder();
        
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
        int h = getHeight();
        g.setColor(pressed ? settings.buttonPressed() : settings.buttonColor());
        g.fillRoundRect(3, 3, getWidth()-6, h-6, 6, 6);
    }
}
