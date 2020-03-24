package cfh.logikus;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;

public class ToggleButton extends Component {
    
    private boolean pressed = false;
    
    public ToggleButton() {
        setBorder(BorderFactory.createLoweredSoftBevelBorder());
        Dimension size = settings.buttonSize();
        size.height += settings.buttonSlide();
        setPreferredSize(size);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent ev) {
                if (ev.getButton() == ev.BUTTON1 || ev.getButton() == ev.BUTTON3) {
                    pressed = !pressed;
                    repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int slide = settings.buttonSlide();
        int h = getHeight() - slide;
        g.setColor(pressed ? settings.buttonPressed() : settings.buttonColor());
        if (pressed) {
            slide = 0;
        }
        g.fillRoundRect(3, slide+3, getWidth()-6, h-6, 6, 6);
    }
}
