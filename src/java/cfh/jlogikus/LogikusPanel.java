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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import cfh.FileChooser;

public class LogikusPanel extends JComponent implements Module {
    
    private static final int META_KEYS = CTRL_DOWN_MASK + SHIFT_DOWN_MASK + ALT_DOWN_MASK;
    
    private final transient Object updateLock = new Object();
    
    private final Settings settings = Settings.get();
    
    private final Source source;
    private final List<Output> outputs;
    private final PushLane push;
    private final List<ToggleLane> toggles;
    
    private final List<Connection> connections;
    private final List<ContactGroup> groups;
    
    private BufferedImage image = null;
    
    private transient volatile Point startTrack = null;
    private transient volatile Point endTrack = null;
    
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
        
        groups = unmodifiableList(
            Stream.of(
                source.groups(),
                outputs.stream().flatMap(Output::groups),
                push.groups(),
                toggles.stream().flatMap(ToggleLane::groups)
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
            add(output.lamp(), new GridBagConstraints(RELATIVE, y, 1, 1, 1.0, 0.0, SOUTH, HORIZONTAL, insets, 0, 0));
        }
        add(new RightFrame(), new GridBagConstraints(RELATIVE, y, REMAINDER, 1, 0.0, 0.0, SOUTHWEST, HORIZONTAL, insets, 0, 0));
        
        // labels
        y = 1;
        add(source.label(), new GridBagConstraints(0, y, 1, 1, 0.0, 0.0, SOUTH, HORIZONTAL, insets, 0, 0));
        for (var output : outputs) {
            add(output.label(), new GridBagConstraints(RELATIVE, y, 1, 1, 0.0, 0.0, SOUTH, HORIZONTAL, insets, 0, 0));
        }
        
        // Source, Output
        y = 2;
        add(source.group(), new GridBagConstraints(0, y, 1, 1, 0.0, 1.0, NORTH, NONE, insets, 0, 0));
        for (var output : outputs) {
            add(output.group(), new GridBagConstraints(RELATIVE, y, 1, 1, 0.0, 1.0, NORTH, NONE, insets, 0, 0));
        }
        
        // switches
        y = 3;
        var gap = settings.pushContactGap();
        add(push.group(), new GridBagConstraints(0, y, 1, 1, 0.0, 0.0, NORTH, NONE, insets, 0, gap));
        for (var toggle : toggles) {
            add(toggle.contactPanel(), new GridBagConstraints(RELATIVE, y, 1, 1, 0.0, 0.0, CENTER, VERTICAL, insets , 0, 0));
        }
        
        // labels
        y = 4;
        add(push.label(), new GridBagConstraints(0, y, 1, 1, 0.0, 1.0, SOUTH, HORIZONTAL, insets, 0, 0));
        for (var toggle : toggles) {
            add(toggle.label(), new GridBagConstraints(RELATIVE, y, 1, 1, 0.0, 1.0, SOUTH, HORIZONTAL, insets , 0, 0));
        }
        
        // buttons
        y = 5;
        add(push.button(), new GridBagConstraints(0, y, 1, 1, 0.0, 0.0, SOUTH, NONE, insets, 0, 0));
        for (var toggle : toggles) {
            add(toggle.button(), new GridBagConstraints(RELATIVE, y, 1, 1, 0.0, 0.0, SOUTH, NONE, insets , 0, 0));
        }
        
        var adapter = new MouseAdapter() {
            private Contact start = null;
            private final transient Cursor def = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
            private final transient Cursor cross = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
            private final transient Cursor hand = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
            @Override
            public void mouseEntered(MouseEvent ev) {
                if (ev.getComponent() instanceof Contact && !((Contact)ev.getComponent()).isConnected()) {
                    setCursor(start==null ? hand : hand);
                }
            }
            @Override
            public void mouseExited(MouseEvent ev) {
                setCursor(start==null ? def : cross);
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
                                        setStart(contact, ev.getPoint());
                                    } else if (contact == start) {
                                        setStart(null, ev.getPoint());
                                    } else {
                                        mouseExited(ev);
                                        Connection connection = new Connection(start, contact);
                                        connections.add(connection);
                                        start.connected(connection);
                                        contact.connected(connection);
                                        setStart(null, ev.getPoint());
                                        update();
                                    }
                                }
                            } else if (metaKeys == CTRL_DOWN_MASK) {
                                if (start == null) {
                                    setStart(contact, ev.getPoint());
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
                                    setStart(null, ev.getPoint());
                                }
                            }
                        }
                    }
                }
            }
            @Override
            public void mouseMoved(MouseEvent ev) {
                if (start != null) {
                    endTrack = SwingUtilities.convertPoint(ev.getComponent(), ev.getPoint(), LogikusPanel.this);
                    repaint();
                }
            }
            private void setStart(Contact contact, Point point) {
                start = contact;
                if (start != null) {
                    startTrack = SwingUtilities.convertPoint(contact, contact.getWidth()/2-1, contact.getHeight()/2-1, LogikusPanel.this);
                } else {
                    startTrack = null;
                }
                endTrack = null;
            }
        };
        groups
        .stream()
        .flatMap(ContactGroup::contacts)
        .peek(c -> c.addMouseListener(adapter))
        .forEach(c -> c.addMouseMotionListener(adapter));
        
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent ev) {
                if (startTrack != null) {
                    endTrack = ev.getPoint();
                    repaint();
                }
            }
        });
        
        var popup = new JPopupMenu();
        popup.add(createMenu("load", this::doLoad, "Load *program* from file; CTRL remove connections"));
        popup.add(createMenu("save", this::doSave, "Save current *program* to file"));
        popup.addSeparator();
        popup.add(createMenu("image", this::doImage, "Read image from file; CTRL remove image"));
        popup.addSeparator();
        popup.add(createMenu("clear label", this::doClearLabel, "Remove labels"));
        popup.add(createMenu("RESET", this::doReset, "Remove image, ALL labels and ALL connections"));
        popup.addSeparator();
        popup.add(createMenu("update", this::doUpdate, "Update status for debugging"));
        
        setComponentPopupMenu(popup);
    }

    private void doSave(ActionEvent ev) {
        var file = new FileChooser("file").getFileToSave(this);
        if (file == null) {
            return;
        }

        try (var out = new PrintWriter(file)) {
            out
            .printf("JLogikus\n")
            .printf("103\n")
            .printf("%s\n", LocalDateTime.now(ZoneOffset.UTC));

            // 101
            out.printf("outputs: %d\n", outputs.size());
            outputs.stream().map(Output::label).map(EditableLabel::getText).forEach(s -> out.printf("%s\n", s));
            
            // 103
            out.printf("push: %d\n", 1);
            out.printf("%s\n", push.label().getText());
            
            // 101
            out.printf("toggles: %d\n", toggles.size());
            toggles.stream().map(ToggleLane::label).map(EditableLabel::getText).forEach(s -> out.printf("%s\n", s));

            // 101,100
            out.printf("connections: %d\n", connections.size());
            for (var connection : connections) {
                out.printf("%s ~ %s\n", connection.start().id(), connection.end().id());
            }
            
            // 102
            if (image == null) {
                out.printf("image: 0\n");
            } else {
                var data = new ByteArrayOutputStream(1024);
                ImageIO.write(image, "png", data);
                var encoded = Base64.getEncoder().encodeToString(data.toByteArray());
                out.printf("image: %d\n", (encoded.length()+99)/100);
                for (var i = 0; i < encoded.length(); i+=100) {
                    out.printf("%s\n", encoded.substring(i, Math.min(i+100, encoded.length())));
                }
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
        if ((ev.getModifiers() & ev.CTRL_MASK) != 0) {
            if (showConfirmDialog(this, "Remove ALL connections?", "Confirm", OK_CANCEL_OPTION) == OK_OPTION) {
                clearConnections();
            }
        } else {
            var file = new FileChooser("file").getFileToLoad(this);
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
                if (!line.equals("JLogikus"))
                    throw new IOException(input.getLineNumber() + ": unrecognized file header");
                line = input.readLine();
                if (!List.of("103", "102", "101", "100").contains(line))
                    throw new IOException(input.getLineNumber() + ": unrecognized version \"" + line + "\"");
                var version = Integer.parseInt(line);

                line = input.readLine();  // date time

                List<String> outputLabels = null;
                if (version >= 101) {
                    outputLabels = loadList(input, "outputs:", outputs.size());
                }

                String pushLabel = null;
                if (version >= 103) {
                    pushLabel = loadList(input, "push:", 1).get(0);
                }
                
                List<String> toggleLabels = null;
                if (version >= 101) {
                    toggleLabels = loadList(input, "toggles:", toggles.size());
                }

                List<Connection> connectList = null;
                if (version >= 100) {
                    connectList = loadConnections(input);
                }
                
                BufferedImage img = null;
                if (version >= 102) {
                    var count = loadCount(input, "image:");
                    if (count > 0) {
                        var builder = new StringBuilder();
                        for (var i = 0; i < count; i++) {
                            builder.append(input.readLine());
                        }
                        var data = Base64.getDecoder().decode(builder.toString());
                        img = ImageIO.read(new ByteArrayInputStream(data));
                    }
                }
                
                if (outputLabels != null) {
                    var iter = outputLabels.listIterator();
                    outputs.forEach(o -> o.label().setText(iter.next()));
                }
                if (pushLabel != null) {
                    push.label().setText(pushLabel);
                }
                if (toggleLabels != null) {
                    var iter = toggleLabels.listIterator();
                    toggles.forEach(t -> t.label().setText(iter.next()));
                }
                if (connectList != null) {
                    clearConnections();
                    for (Connection connection : connectList) {
                        connections.add(connection);
                        connection.start().connected(connection);
                        connection.end().connected(connection);
                    }
                }
                if (img != null) {
                    setImage(img);
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
    }
    
    private void doImage(ActionEvent ev) {
        if ((ev.getModifiers() & ev.CTRL_MASK) == 0) {
            var file = new FileChooser("image").getFileToLoad(this);
            if (file == null) {
                return;
            }
            BufferedImage img;
            try {
                img = ImageIO.read(file);
                if (img == null) {
                    showMessageDialog(this, "Unable to read image", "Unknown Error", ERROR_MESSAGE);
                    return;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                Object[] message = {
                        ex.getClass().getSimpleName(),
                        ex.getMessage(),
                };
                showMessageDialog(this, message, ex.getClass().getSimpleName(), ERROR_MESSAGE);
                return;
            }
            setImage(img);
        } else {
            setImage(null);
        }
        repaint();
    }

    private void setImage(BufferedImage img) {
        if (img != null) {
            int rest = outputs.size();
            int x = 0;
            int width = img.getWidth();
            for (var output : outputs) {
                var w = width / rest;
                output.lamp().image(img, new Rectangle(x, 0, w, img.getHeight()));
                x += w;
                width -= w;
                rest -= 1;
            }
        } else {
            outputs.stream().map(Output::lamp).forEach(LampFrame::clear);
        }
        image = img;
    }
    
    private List<String> loadList(LineNumberReader input, String key, int expected) throws IOException {
        var count = loadCount(input, key);
        if (count != expected)
            throw new IOException(String.format("%d: invalid count for %s: %d, expected %s", input.getLineNumber(), key, count, expected));
        var list = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            list.add(input.readLine());
        }
        return list;
    }
    
    private List<Connection> loadConnections(LineNumberReader input) throws IOException {
        var count = loadCount(input, "connections:");
        if (count < 0)
            throw new IOException(input.getLineNumber() + ": invalid number of connections");
        var list = new ArrayList<Connection>();
        for (var i = 0; i < count; i++) {
            var line = input.readLine();
            var tokens = line.split(" *~ *", 2);
            if (tokens.length < 2)
                throw new IOException(input.getLineNumber() + ": connection unparseable \"" + line + "\"");
            var start = contactForId(tokens[0], input.getLineNumber());
            var end = contactForId(tokens[1], input.getLineNumber());
            if (start == end) {
                throw new IOException(input.getLineNumber() + ": connection to same contact not allowed \"" + line + "\"");
            }
            list.add(new Connection(start, end));
        }
        return list;
    }
    private int loadCount(LineNumberReader input, String key) throws IOException {
        var line = input.readLine();
        if (!line.startsWith(key))
            throw new IOException(String.format("%d: expected \"%s\" but got \"%s\"", input.getLineNumber(), key, line));
        var count = Integer.parseInt(line.substring(key.length()).trim());
        return count;
    }
    
    private void doClearLabel(ActionEvent ev) {
        if (showConfirmDialog(this, "Remove ALL labels?", "Confirm", OK_CANCEL_OPTION) == OK_OPTION) {
            clearLabels();
        }
    }
    
    private void doReset(ActionEvent ev) {
        if (showConfirmDialog(this, "Remove image, ALL labels and ALL connections?", "Confirm", OK_CANCEL_OPTION) == OK_OPTION) {
            clearImage();
            clearConnections();
            clearLabels();
            push.button().reset();
            toggles.stream().map(ToggleLane::button).forEach(Button::reset);
        }
    }
    
    private void doUpdate(ActionEvent ev) {
        update();
    }

    private Contact contactForId(String id, int lineNumber) throws NoSuchElementException {
        return groups
            .stream()
            .flatMap(ContactGroup::contacts)
            .filter(c -> c.id().equals(id))
            .findAny()
            .orElseThrow(() -> new NoSuchElementException(lineNumber + ": unknown contact \"" + id + "\""));
    }
    
    private void update() {
        synchronized (updateLock) {
            groups.forEach(ContactGroup::deactive);
            connections.forEach(Connection::deactive);
            var networks = new HashMap<ContactGroup, Set<ContactGroup>>();
            for (var connection : connections) {
                var set = new HashSet<ContactGroup>();
                connection.connected().forEach(g -> {
                    set.addAll(networks.getOrDefault(g, Set.of(g)));
                });
                set.forEach(group -> networks.put(group, set));
            }
            var active = networks.get(source.group());
            if (active != null) {
                active.forEach(ContactGroup::active);
                active.stream()
                .flatMap(ContactGroup::contacts)
                .map(Contact::connection)
                .filter(Objects::nonNull)
                .forEach(Connection::active);
            }
        }
        repaint();
    }
    
    private void clearImage() {
        outputs.stream().map(Output::lamp).forEach(LampFrame::clear);
        repaint();
    }
    
    private void clearConnections() {
        connections.clear();
        groups().forEach(ContactGroup::clear);
        update();
    }
    
    private void clearLabels() {
        outputs.forEach(o -> o.label().setText(o.id()));
        push.label().setText(push.id());
        toggles.forEach(t -> t.label().setText(t.id()));
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
            
            if (startTrack != null && endTrack != null) {
                g.setColor(settings.connectionColorDeact());
                g.drawLine(startTrack.x, startTrack.y, endTrack.x, endTrack.y);
            }
        } finally {
            gg.dispose();
        }
    }
    
    @Override
    public String id() {
        return "Logikus";
    }

    @Override
    public Stream<ContactGroup> groups() {
        return groups.stream();
    }
    
    @Override
    public Stream<ContactGroup> connected(Contact contact) {
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
