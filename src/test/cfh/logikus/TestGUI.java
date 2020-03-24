package cfh.logikus;

import static java.awt.GridBagConstraints.*;
import static java.util.stream.Collectors.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class TestGUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new TestGUI()::initGUI);
    }
    
    private final Settings settings = Settings.INSTANCE;
    
    private final Source source;
    private final List<Output> outputs;
    private final PushLane push;
    private final List<ToggleLane> toggles;
    
    private TestGUI() {
        source = new Source(Orient.HORIZONTAL);
        outputs = Collections.unmodifiableList(
            Stream
            .iterate(Orient.HORIZONTAL, UnaryOperator.identity())
            .limit(settings.laneCount())
            .map(Output::new)
            .collect(toList())
            );
        outputs.get(outputs.size()-1).add(new RightFrame());
        push = new PushLane();
        toggles = Collections.unmodifiableList(
            Stream.generate(ToggleLane::new).limit(settings.laneCount()).collect(toList())
            );
    }
    
    private void initGUI() {
        var insets = new Insets(0, 0, 0, 0);
        
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        frame.add(source, new GridBagConstraints(0, 0, 1, 1, 0.5, 0.5, CENTER, BOTH, insets, 0, 0));
        for (Output output : outputs) {
            frame.add(output, new GridBagConstraints(RELATIVE, 0, 1, 1, 1.0, 0.5, CENTER, BOTH, insets, 0, 0));
        }
        frame.add(push, new GridBagConstraints(0, 1, 1, 1, 0.0, 1.0, CENTER, BOTH, insets, 0, 0));
        for (ToggleLane lane : toggles) {
            frame.add(lane, new GridBagConstraints(RELATIVE, 1, 1, 1, 1.0, 0.5, CENTER, BOTH, insets, 0, 0));
        }
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
