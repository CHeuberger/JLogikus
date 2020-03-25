package cfh.logikus;

import static java.awt.GridBagConstraints.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.swing.BorderFactory;

public class LogikusPanel extends Component {

    private final Source source;
    private final List<Output> outputs;
    private final PushLane push;
    private final List<ToggleLane> toggles;
    
    private final transient List<Contact> contacts;
    
    private final List<Connection> connections;

    public LogikusPanel() {
        source = new Source();
        outputs = unmodifiableList(
            Stream.generate(Output::new).limit(settings.laneCount()).collect(toList())
            );
        push = new PushLane();
        toggles = unmodifiableList(
            Stream.generate(ToggleLane::new).limit(settings.laneCount()).collect(toList())
            );
        contacts = unmodifiableList(
            Stream.of(
                source.contacts(),
                outputs.stream().flatMap(Output::contacts),
                push.contacts(),
                toggles.stream().flatMap(ToggleLane::contacts)
                )
            .flatMap(Function.identity())
            .collect(toList()));
        connections = new ArrayList<>();
        
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
        add(source.group(), new GridBagConstraints(0, y, 1, 1, 0.0, 0.0, NORTH, NONE, insets, 0, 0));
        for (var output : outputs) {
            add(output.group(), new GridBagConstraints(RELATIVE, y, 1, 1, 1.0, 0.0, NORTH, NONE, insets, 0, 0));
        }
        
        // switches
        y = 2;
        add(push.group(), new GridBagConstraints(0, y, 1, 1, 0.0, 1.0, NORTH, NONE, insets, 0, 0));
        for (var toggle : toggles) {
            add(toggle.contactPanel(), new GridBagConstraints(RELATIVE, y, 1, 1, 0.0, 0.0, CENTER, VERTICAL, insets , 0, 0));
        }
        
        // buttons
        y = 3;
        add(push.button(), new GridBagConstraints(0, y, 1, 1, 0.0, 0.0, SOUTH, NONE, insets, 0, 0));
        for (var toggle : toggles) {
            add(toggle.button(), new GridBagConstraints(RELATIVE, y, 1, 1, 0.0, 0.0, SOUTH, NONE, insets , 0, 0));
        }
        
        var adapter = new MouseAdapter() {
            private final transient Cursor cursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
            private Cursor previous = null;
            private Contact start = null;
            @Override
            public void mouseEntered(MouseEvent ev) {
                if (ev.getComponent() instanceof Contact && !((Contact)ev.getComponent()).isConnected()) {
                    previous = getCursor();
                    setCursor(cursor);
                }
            }
            @Override
            public void mouseExited(MouseEvent ev) {
                if (previous != null) {
                    setCursor(previous);
                    previous = null;
                }
            }
            @Override
            public void mouseClicked(MouseEvent ev) {
                if (ev.getButton() == ev.BUTTON1) {
                    if (ev.getClickCount() == 1) {
                        if (ev.getComponent() instanceof Contact) {
                            Contact contact = (Contact) ev.getComponent();
                            if (!contact.isConnected()) {
                                if (start == null) {
                                    start = contact;
                                } else {
                                    Contact end = contact;
                                    mouseExited(ev);
                                    connections.add(new Connection(start, end));
                                    start.connected();
                                    end.connected();
                                    start = null;
                                    repaint();
                                }
                            }
                        }
                    }
                }
            }
        };
        contacts.forEach(c -> c.addMouseListener(adapter));
    }
    
    @Override
    protected void paintChildren(Graphics g) {
        super.paintChildren(g);
        
        Graphics2D gg = (Graphics2D) g.create();
        try {
            connections.forEach(c -> c.paintComponent(this, gg));
        } finally {
            gg.dispose();
        }
    }
}
