package cfh.logikus;

import java.awt.Color;
import java.awt.Dimension;

public enum Settings {

    INSTANCE;
    
    public Dimension contactSize() { return new Dimension(10, 10); }
    public Color contactColor() { return Color.BLACK; }
    public int contactRadius() {return 3; }
    
    public int groupBorder() { return 3; }
    public int groupCount() { return 3; }
    
    public Dimension outputSize() { return new Dimension(100, 150); }
    public Color displayFrame() { return new Color(0x40, 0x20, 0x00, 0xFF); }
    
    public Dimension buttonSize() { return new Dimension(30,60); }
    public Color buttonColor() { return new Color(0xA0, 0x00, 0x00, 0xFF); }
    public Color buttonPressed() { return new Color(0x40, 0x00, 0x00, 0xFF); }
    
    public int toggleCount() { return 5; }
    
    public int laneCount() { return 10; }
}
