package cfh.logikus;

import static java.awt.GridBagConstraints.*;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class TestGUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new TestGUI()::initGUI);
    }
    
    private Source source;
    private List<Output> outputs;
    private Push push;
    private TestGUI() {
    }
    
    private void initGUI() {
        source = new Source(Orient.HORIZONTAL);
        outputs = Collections.unmodifiableList(
            IntStream.range(0, 10).mapToObj(i -> new Output(Orient.HORIZONTAL)).collect(Collectors.toList()));
        outputs.get(outputs.size()-1).add(new RightFrame());
        push = new Push();
        
        var insets = new Insets(0, 0, 0, 0);
        
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        frame.add(source, new GridBagConstraints(0, 0, 1, 1, 0.5, 0.0, CENTER, BOTH, insets, 0, 0));
        for (Output output : outputs) {
            frame.add(output, new GridBagConstraints(RELATIVE, 0, 1, 1, 1.0, 1.0, CENTER, BOTH, insets, 0, 0));
        }
        frame.add(push, new GridBagConstraints(0, 1, 1, 1, 0.0, 1.0, CENTER, BOTH, insets, 0, 0));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
