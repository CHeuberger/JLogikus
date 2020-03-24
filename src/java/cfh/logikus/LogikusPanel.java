package cfh.logikus;

import static java.awt.GridBagConstraints.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.BorderFactory;

public class LogikusPanel extends Component {

    private final Source source;
    private final List<Output> outputs;
    private final PushLane push;
    private final List<ToggleLane> toggles;

    public LogikusPanel() {
        source = new Source();
        outputs = unmodifiableList(
            Stream.generate(Output::new).limit(settings.laneCount()).collect(toList())
            );
        push = new PushLane();
        toggles = unmodifiableList(
            Stream.generate(ToggleLane::new).limit(settings.laneCount()).collect(toList())
            );
        
        initGUI();
    }
    
    private void initGUI() {
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 2),
            BorderFactory.createEmptyBorder(4, 4, 4, 4)
            ));
        setLayout(new GridBagLayout());
        
        int y;
        // Display
        y = 0;
        add(new LeftFrame(), new GridBagConstraints(0, y, 1, 1, 0.0, 0.0, SOUTHEAST, HORIZONTAL, insets, 0, 0));
        for (var output : outputs) {
            add(output.lamp(), new GridBagConstraints(RELATIVE, y, 1, 1, 1.0, 0.5, SOUTH, HORIZONTAL, insets, 0, 0));
        }
        add(new RightFrame(), new GridBagConstraints(RELATIVE, y, REMAINDER, 1, 0.0, 0.0, SOUTHWEST, HORIZONTAL, insets, 0, 0));
        
        // Source, Output
        y = 1;
        add(source.contacts(), new GridBagConstraints(0, y, 1, 1, 0.0, 0.0, NORTH, NONE, insets, 0, 0));
        for (var output : outputs) {
            add(output.contact(), new GridBagConstraints(RELATIVE, y, 1, 1, 1.0, 0.0, NORTH, NONE, insets, 0, 0));
        }
        
        // contacts
        y = 2;
        add(push.contact(), new GridBagConstraints(0, y, 1, 1, 0.0, 1.0, NORTH, NONE, insets, 0, 0));
        for (var toggle : toggles) {
            add(toggle.contactPanel(), new GridBagConstraints(RELATIVE, y, 1, 1, 0.0, 0.0, SOUTH, BOTH, insets , 0, 0));
        }
        
        // buttons
        y = 3;
        add(push.button(), new GridBagConstraints(0, y, 1, 1, 0.0, 0.0, SOUTH, NONE, insets, 0, 0));
        for (var toggle : toggles) {
            add(toggle.button(), new GridBagConstraints(RELATIVE, y, 1, 1, 0.0, 0.0, SOUTH, NONE, insets , 0, 0));
        }
    }
}
