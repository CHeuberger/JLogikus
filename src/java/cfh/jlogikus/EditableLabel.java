package cfh.jlogikus;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class EditableLabel extends JTextField {

    public EditableLabel() {
        super();
    }
    
    public EditableLabel(String text) {
        super(text);
        setToolTipText(text);
        setFont(getFont().deriveFont(Font.BOLD));
    }
    
    {
        setEditable(false);
        setHorizontalAlignment(CENTER);
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setEditable(false);
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent ev) {
                if (ev.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(ev)) {
                    setEditable(true);
                }
            }
        });
    }
}
