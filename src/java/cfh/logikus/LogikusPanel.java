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
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.invoke.StringConcatFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

public class LogikusPanel extends JComponent {

    private final Settings settings = Settings.INSTANCE;
    
    private final Source source;
    private final List<Output> outputs;
    private final PushLane push;
    private final List<ToggleLane> toggles;
    
    private final List<Connection> connections;

    private final transient Object updateLock = new Object();
    private final transient List<Contact> contacts;

    public LogikusPanel() {
        source = new Source("Q");
        outputs = unmodifiableList(
            IntStream.range(0, settings.laneCount())
            .mapToObj(i -> "L + i")
            .map(Output::new)
            .collect(toList())
            );
        push = new PushLane("T");
        toggles = unmodifiableList(
            IntStream.range(0, settings.laneCount())
            .mapToObj(String::valueOf)
            .map(ToggleLane::new)
            .collect(toList())
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
        Insets insets = settings.insets();
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
                                    System.out.println("start " + start);
                                } else {
                                    Contact end = contact;
                                    mouseExited(ev);
                                    connections.add(new Connection(start, end));
                                    start.connected();
                                    end.connected();
                                    start = null;
                                    System.out.println("end " + end);
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
    
    private void update() {
        synchronized (updateLock) {
            var networks = new HashMap<Contact, Set<Contact>>();
            for (var connection : connections) {
                connection.contacts();  // XXX
            }
        }
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
