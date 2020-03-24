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
        g.setColor(pressed ? settings.buttonPressed() : settings.buttonColor());
        g.fillRoundRect(3, 3, getWidth()-6, getHeight()-6, 6, 6);
        g.setColor(Color.BLACK);
        g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 9, 9);
    }
}
