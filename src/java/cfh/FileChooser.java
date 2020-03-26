package cfh;

import static javax.swing.JOptionPane.OK_CANCEL_OPTION;
import static javax.swing.JOptionPane.OK_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.File;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;

/**
 * @version 2.0, 26.03.2020
 */
public class FileChooser extends JFileChooser {

    private static final long serialVersionUID = 1L;

    private final String PREF_DIR = "directory";
    
    private final Preferences prefs;
    
    public FileChooser() {
        String classname = Thread.currentThread().getStackTrace()[2].getClassName();
        prefs = Preferences.userRoot().node("/" + classname.replace('.', '/'));
        String dir = prefs.get(PREF_DIR, ".");
        setCurrentDirectory(new File(dir));
        setMultiSelectionEnabled(false);
    }

    public File getFileToSave(Component parent) {
        if (showSaveDialog(getParent()) != APPROVE_OPTION) {
            return null;
        }
        File file = getSelectedFile();
        Object[] msg = { "File already exists!", file.getAbsolutePath(), "Overwrite?" };
        if (file.exists() && showConfirmDialog(getParent(), msg, "Confirm", OK_CANCEL_OPTION) != OK_OPTION)
            return null;
        if (file.exists()) {
            File bak = new File(file.getParentFile(), file.getName() + ".bak");
            if (bak.exists()) {
                bak.delete();
            }
            file.renameTo(bak);
        }
        return file;
    }
    
    public File getFileToLoad(Component parent) {
        if (showOpenDialog(parent) != APPROVE_OPTION) {
            return null;
        }
        return getSelectedFile();
    }

    @Override
    public int showOpenDialog(Component parent) throws HeadlessException {
        int option = super.showOpenDialog(parent);
        if (option == APPROVE_OPTION) {
            String dir = getCurrentDirectory().getAbsolutePath();
            prefs.put(PREF_DIR, dir);
        }
        return option;
    }

    @Override
    public int showSaveDialog(Component parent) throws HeadlessException {
        int option = super.showSaveDialog(parent);
        if (option == APPROVE_OPTION) {
            String dir = getCurrentDirectory().getAbsolutePath();
            prefs.put(PREF_DIR, dir);
        }
        return option;
    }

    @Override
    public int showDialog(Component parent, String approveButtonText) throws HeadlessException {
        int option = super.showDialog(parent, approveButtonText);
        if (option == APPROVE_OPTION) {
            String dir = getCurrentDirectory().getAbsolutePath();
            prefs.put(PREF_DIR, dir);
        }
        return option;
    }
}
