package cfh.logikus;

public class Source extends Module {

    private final ContactGroup contacts;
    
    public Source() {
        contacts = new ContactGroup.Horizontal();
    }
    
    public ContactGroup contacts() {
        return contacts;
    }
}
