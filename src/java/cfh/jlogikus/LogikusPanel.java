package cfh.jlogikus;

import static java.awt.GridBagConstraints.*;
import static java.awt.event.InputEvent.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static javax.swing.JOptionPane.*;
import static javax.swing.SwingUtilities.isLeftMouseButton;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import cfh.FileChooser;

public class LogikusPanel extends JComponent implements Module {

    private static final int META_KEYS = CTRL_DOWN_MASK + SHIFT_DOWN_MASK + ALT_DOWN_MASK;
    
    private final Settings settings = Settings.get();
    
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
                if (isLeftMouseButton(ev)) {
                    if (ev.getClickCount() == 1) {
                        if (ev.getComponent() instanceof Contact) {
                            var contact = (Contact) ev.getComponent();
                            var metaKeys = ev.getModifiersEx() & META_KEYS;
                            if (metaKeys == 0) {
                                if (!contact.isConnected()) {
                                    if (start == null) {
                                        start = contact;
                                    } else if (contact == start) {
                                        start = null;
                                    } else {
                                        mouseExited(ev);
                                        Connection connection = new Connection(start, contact);
                                        connections.add(connection);
                                        start.connected(connection);
                                        contact.connected(connection);
                                        start = null;
                                        update();
                                    }
                                }
                            } else if (metaKeys == CTRL_DOWN_MASK) {
                                if (start == null) {
                                    start = contact;
                                } else {
                                    var connection = contact.connection();
                                    if (connection != null) {
                                        if (connection.start() == start || connection.end() == start) {
                                            if (connections.remove(connection)) {
                                                connection.start().disconnected();
                                                connection.end().disconnected();
                                                update();
                                            }
                                        }
                                    }
                                    start = null;
                                }
                            }
                        }
                    }
                }
            }
        };
        contacts.forEach(c -> c.addMouseListener(adapter));
        
        var popup = new JPopupMenu();
        popup.add(createMenu("save", this::doSave, "Save current *program* to file"));
        popup.add(createMenu("load", this::doLoad, "Load *program* from file"));
        popup.addSeparator();
        popup.add(createMenu("clear", this::doClear, "Remove all connections"));
        popup.add(createMenu("update", this::doUpdate, "Update status for debugging"));
        
        setComponentPopupMenu(popup);
    }
    
    private void doSave(ActionEvent ev) {
        var file = new FileChooser().getFileToSave(this);
        if (file == null) {
            return;
        }

        try (var out = new BufferedWriter(new FileWriter(file))) {
            out
            .append("JLogikus\n")
            .append("100\n")
            .append(LocalDateTime.now(ZoneOffset.UTC).toString() + "\n")
            .append("connections:" + connections.size() + "\n");
            
            for (var connection : connections) {
                out.append(connection.start().id()).append("--").append(connection.end().id()).append("\n");
            }            
        } catch (IOException ex) {
            ex.printStackTrace();
            Object[] message = {
                    ex.getClass().getSimpleName(),
                    ex.getMessage(),
            };
            showMessageDialog(this, message, ex.getClass().getSimpleName(), ERROR_MESSAGE);
        }
    }

    private void doLoad(ActionEvent ev) {
        var file = new FileChooser().getFileToLoad(this);
        if (file == null) {
            return;
        }
        
        LineNumberReader input;
        try {
            input = new LineNumberReader(new FileReader(file));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            Object[] message = {
                    ex.getClass().getSimpleName(),
                    ex.getMessage(),
            };
            showMessageDialog(this, message, ex.getClass().getSimpleName(), ERROR_MESSAGE);
            return;
        }
        String line = "";
        try (input) {
            line = input.readLine();
            if (!line.equals("JLogikus")) {
                throw new IOException(input.getLineNumber() + ": unrecognized file header");
            }
            line = input.readLine();
            if (!line.equals("100")) {
                throw new IOException(input.getLineNumber() + ": unrecognized version \"" + line + "\"");
            }
            line = input.readLine();  // date time
            line = input.readLine();
            if (!line.startsWith("connections:")) {
                throw new IOException(input.getLineNumber() + ": expected \"connections:\" but got \"" + line + "\"");
            }
            var count = Integer.parseInt(line.substring(12));
            if (count < 0) {
                throw new IOException(input.getLineNumber() + ": invalid number of connections \"" + line + "\"");
            }
            var list = new ArrayList<Connection>();
            for (var i = 0; i < count; i++) {
                line = input.readLine();
                var tokens = line.split("--", 2);
                if (tokens.length < 2) {
                    throw new IOException(input.getLineNumber() + ": connection unparseable \"" + line + "\"");
                }
                var start = contactForId(tokens[0], input.getLineNumber());
                var end = contactForId(tokens[1], input.getLineNumber());
                if (start == end) {
                    throw new IOException(input.getLineNumber() + ": connection to same contact not allowed \"" + line + "\"");
                }
                list.add(new Connection(start, end));
            }
            clear();
            for (Connection connection : list) {
                connections.add(connection);
                connection.start().connected(connection);
                connection.end().connected(connection);
            }
            update();
        } catch (IOException | NumberFormatException | NoSuchElementException ex) {
            ex.printStackTrace();
            Object[] message = {
                    ex.getClass().getSimpleName(),
                    ex.getMessage(),
                    input.getLineNumber() + ": " + line
            };
            showMessageDialog(this, message, ex.getClass().getSimpleName(), ERROR_MESSAGE);
        }
    }
    
    private void doClear(ActionEvent ev) {
        if (showConfirmDialog(this, "Remove ALL connections?", "Confirm", OK_CANCEL_OPTION) == OK_OPTION) {
            clear();
        }
    }
    
    private void doUpdate(ActionEvent ev) {
        update();
    }

    private Contact contactForId(String id, int lineNumber) throws NoSuchElementException {
        return contacts
            .stream()
            .filter(c -> c.id().equals(id))
            .findAny()
            .orElseThrow(() -> new NoSuchElementException(lineNumber + ": unknown contact \"" + id + "\""));
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
        }
        repaint();
    }
    
    private void clear() {
        connections.clear();
        contacts().forEach(Contact::clear);
        update();
    }
    
    private JMenuItem createMenu(String text, ActionListener listener, String tooltip) {
        var item = new JMenuItem(text);
        item.setToolTipText(tooltip);
        item.addActionListener(listener);
        return item;
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
