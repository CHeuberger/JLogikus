package cfh.jlogikus;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;

public enum Settings {
    
    DEFAULT;
    
    public static Settings get() { return DEFAULT; }
    
    public Insets insets() { return new Insets(2, 2, 2, 2); }
    
    public int laneCount() { return 10; }
    public int switchCount() { return 5; }
    
    public int groupCount() { return 3; }
    public int groupBorder() { return 3; }

    public Dimension leftSize() { return new Dimension(40, 150); }
    public Dimension lampSize() { return new Dimension(100, 150); }
    public Dimension rightSize() { return new Dimension(40, 150); }
    public Color displayFrame() { return new Color(0x40, 0x20, 0x00, 0xFF); }
    public Color lampLight() { return new Color(0xC0, 0x60, 0x20, 0xFF); }
    
    
    public Dimension contactSize() { return new Dimension(10, 10); }
    public Color contactColor() { return Color.BLACK; }
    public int contactRadius() {return 3; }
    
    public Dimension buttonSize() { return new Dimension(30,60); }
    public int buttonSlide() { return 20; }
    public Color buttonColor() { return new Color(0xA0, 0x00, 0x00, 0xFF); }
    public Color buttonPressed() { return new Color(0xD0, 0x00, 0x00, 0xFF); }

    public Color connectionColor() { return new Color(0x50, 0x50, 0xFF, 0xD0); }
    public Color connectionBorderDeact() { return connectionColor().darker(); }
    public Color connectionBorderAct() { return connectionColor().brighter(); }  // new Color(0x90, 0x90, 0xFF, 0xE0)
    public BasicStroke connectionStroke() { return new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND); }
    public BasicStroke connectionStrokeBorder() { return new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND); }
}