package cfh.jlogikus;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;

public enum Settings {
    
    DEFAULT,
    SMALL {
        @Override public Insets insets() { return new Insets(0, 0, 0, 0); }
//        @Override public int groupBorder() { return super.groupBorder()-1; }

        @Override public Dimension leftSize() { return shrink(super.leftSize(), 0.8F); }
        @Override public Dimension lampSize() { return shrink(super.lampSize(), 0.8F); }
        @Override public Dimension rightSize() { return shrink(super.rightSize(), 0.8F); }
        
        @Override public Dimension contactSize() { return shrink(super.contactSize(), 0.9F); }
//        @Override public int contactRadius() {return super.contactRadius()-1; }
        
        @Override public Dimension buttonSize() { return shrink(super.buttonSize(), 0.8F); }
        @Override public int buttonSlide() { return shrink(super.buttonSlide(), 0.8F); }

        @Override public BasicStroke connectionStroke() { return new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND); }
        @Override public BasicStroke connectionStrokeBorder() { return new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND); }
    };
    
    public static Settings get() { return DEFAULT; }
    
    public Insets insets() { return new Insets(0, 0, 0, 0); }
    
    public int laneCount() { return 10; }
    public int switchCount() { return 5; }
    
    public int groupCount() { return 3; }
    public int groupBorder() { return 3; }

    public Dimension leftSize() { return new Dimension(40, 150); }
    public Dimension lampSize() { return new Dimension(100, 150); }
    public Dimension rightSize() { return new Dimension(40, 150); }
    public Color displayFrame() { return new Color(0x40, 0x20, 0x00, 0xFF); }
    public Color lampLight() { return new Color(0xE0, 0xB0, 0x90, 0xFF); }
    public Color imgBackgroundAct() { return new Color(0xFF, 0xFF, 0xFF, 0xFF); }
    public Color imgBackgroundDeact() { return new Color(0x70, 0x68, 0x60, 0xFF); }
    
    public Dimension contactSize() { return new Dimension(10, 10); }
    public Color contactColor() { return Color.BLACK; }
    public int contactRadius() {return 3; }
    
    public Dimension buttonSize() { return new Dimension(30,60); }
    public int buttonSlide() { return 20; }
    public Color buttonColor() { return new Color(0xA0, 0x00, 0x00, 0xFF); }
    public Color buttonPressed() { return new Color(0xD0, 0x00, 0x00, 0xFF); }

    public Color connectionColorDeact() { return new Color(0x40, 0x40, 0xFF, 0xC0); }
    public Color connectionColorAct() { return new Color(0x20, 0x20, 0xFF, 0xC0); }
    public Color connectionBorderDeact() { return connectionColorDeact().darker().darker(); }
    public Color connectionBorderAct() { return new Color(0xA0, 0xA0, 0xFF, 0xE0); }
    public BasicStroke connectionStroke() { return new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND); }
    public BasicStroke connectionStrokeBorder() { return new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND); }
    
    public int pushContactGap() { return 40; }
    
    private static int shrink(int value, float rate) {
        return Math.round(value * rate);
    }
    
    private static Dimension shrink(Dimension dimension, float rate) {
        dimension.width = shrink(dimension.width, rate);
        dimension.height = shrink(dimension.height, rate);
        return dimension;
    }

}
