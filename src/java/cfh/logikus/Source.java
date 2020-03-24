package cfh.logikus;

public class Source extends Module {

    private final ContactGroup contact;
    
    public Source() {
        contact = new ContactGroup.Horizontal();
    }
    
    public ContactGroup contact() {
        return contact;
    }
}
