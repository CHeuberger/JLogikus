package cfh.logikus;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PushButton extends Component {
    
    private boolean pressed = false;
    
    public PushButton() {
        setPreferredSize(settings.buttonSize());
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent ev) {
                if (ev.getButton() == ev.BUTTON1) {
                    pressed = true;
                    repaint();
                }
            }
            @Override
            public void mouseReleased(MouseEvent ev) {
                if (ev.getButton() == ev.BUTTON1) {
                    pressed = false;
                    repaint();
                }
            }
            @Override
            public void mouseClicked(MouseEvent ev) {
                if (ev.getButton() == ev.BUTTON3) {
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
        g.setColor(Color.BLACK);
        g.drawRoundRect(0, slide, getWidth()-1, h-1, 9, 9);
        g.setColor(pressed ? settings.buttonPressed() : settings.buttonColor());
        int gap = pressed ? 5 : 3;
        g.fillRoundRect(gap, slide+gap, getWidth()-gap-gap, h-gap-gap, gap+gap, gap+gap);
    }
}
