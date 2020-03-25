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
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class LogikusPanel extends JComponent implements Module {

    private final Settings settings = Settings.INSTANCE;
    
    private final Source source;
    private final List<Output> outputs;
    private final PushLane push;
    private final List<ToggleLane> toggles;
    
    private final List<Connection> connections;

    private final transient Object updateLock = new Object();
    private final transient List<Contact> contacts;

    public LogikusPanel() {
        source = new Source("Q", this);
        outputs = unmodifiableList(
            IntStream.range(0, settings.laneCount())
            .mapToObj(i -> new Output("L"+i, this))
            .collect(toList())
            );
        push = new PushLane("T", this);
        toggles = unmodifiableList(
            IntStream.range(0, settings.laneCount())
            .mapToObj(i -> new ToggleLane("S"+i, this))
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
        
        Insets insets = settings.insets();
        var y = 0;
        // Display
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
                            var contact = (Contact) ev.getComponent();
                            if (!contact.isConnected()) {
                                if (start == null) {
                                    start = contact;
                                    System.out.println("start " + start);
                                } else {
                                    Contact end = contact;
                                    mouseExited(ev);
                                    Connection connection = new Connection(start, end);
                                    connections.add(connection);
                                    start.connected(connection);
                                    end.connected(connection);
                                    start = null;
                                    System.out.println("end " + end);
                                    update();
                                }
                            }
                        }
                    }
                }
            }
        };
        contacts.forEach(c -> c.addMouseListener(adapter));
        
        var update = new JMenuItem("update");
        update.addActionListener(this::doUpdate);
        
        var popup = new JPopupMenu();
        popup.add(update);
        
        setComponentPopupMenu(popup);
    }
    
    private void doUpdate(ActionEvent ev) {
        update();
    }
        
    private void update() {
        synchronized (updateLock) {
            contacts.forEach(Contact::deactive);
            connections.forEach(Connection::deactive);
            var networks = new HashMap<Contact, Set<Contact>>();
            for (var connection : connections) {
                var set = new HashSet<Contact>();
                connection.connected().forEach(contact -> {
                    set.addAll(networks.getOrDefault(contact, Set.of(contact)));
                });
                set.forEach(contact -> networks.put(contact, set));
            }
            var active = source.contacts().map(networks::get).filter(Objects::nonNull).findAny().orElse(Set.of());
            active.forEach(Contact::active);
            connections.stream().filter(connection -> active.contains(connection.start())).forEach(Connection::active);
            
            networks.values().stream().distinct().map(s -> s.stream().map(Contact::id).collect(joining("  "))).forEach(System.out::println);
            System.out.println();
        }
        repaint();
    }
    
    @Override
    protected void paintChildren(Graphics g) {
        super.paintChildren(g);
        
        var gg = (Graphics2D) g.create();
        try {
            connections.forEach(c -> c.paintComponent(this, gg));
        } finally {
            gg.dispose();
        }
    }
    
    @Override
    public String id() {
        return "Logikus";
    }

    @Override
    public Stream<Contact> contacts() {
        return contacts.stream();
    }
    
    @Override
    public Stream<Contact> connected(Contact contact) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void changed(Module module) {
        update();
    }

    @Override
    public String toString() {
        return id();
    }
}
