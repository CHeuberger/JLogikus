package cfh.jlogikus;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.stream.Stream;

import javax.swing.BorderFactory;

public abstract class Button extends Component {
    
    protected boolean pressed = false;
    
    private Button(String id, Module parent) {
        super(id, parent);
    }
    
    public boolean pressed() {
        return pressed;
    }
    
    @Override
    public Stream<Contact> contacts() {
        return Stream.empty();
    }
    
    @Override
    public Stream<Contact> connected(Contact contact) {
        return Stream.empty();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public static class Push extends Button {

        public Push(String id, Module parent) {
            super(id, parent);

            var unpressedBorder = BorderFactory.createRaisedBevelBorder();
            var pressedBorder = BorderFactory.createLoweredBevelBorder();

            setBorder(unpressedBorder);
            setPreferredSize(settings.buttonSize());
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent ev) {
                    if (ev.getButton() == ev.BUTTON1) {
                        change(true);
                    }
                }
                @Override
                public void mouseReleased(MouseEvent ev) {
                    if (ev.getButton() == ev.BUTTON1) {
                        change(false);
                    }
                }
                @Override
                public void mouseClicked(MouseEvent ev) {
                    if (ev.getButton() == ev.BUTTON3) {
                        change(!pressed);
                    }
                }
                private void change(boolean press) {
                    pressed = press;
                    setBorder(pressed ? pressedBorder : unpressedBorder);
                    parent.changed(Push.this);
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
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static class Toggle extends Button {

        public Toggle(String id, Module parent) {
            super(id, parent);

            setBorder(BorderFactory.createLoweredSoftBevelBorder());
            Dimension size = settings.buttonSize();
            size.height += settings.buttonSlide();
            setPreferredSize(size);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent ev) {
                    if (ev.getButton() == ev.BUTTON1 || ev.getButton() == ev.BUTTON3) {
                        pressed = !pressed;
                        parent.changed(Toggle.this);
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            var slide = settings.buttonSlide();
            var h = getHeight() - slide;
            g.setColor(pressed ? settings.buttonPressed() : settings.buttonColor());
            if (pressed) {
                slide = 0;
            }
            g.fillRoundRect(3, slide+3, getWidth()-6, h-6, 6, 6);
        }
    }
}
