package cfh.logikus;

public class Output extends Module {
    
    private final LampFrame lamp;
    private final ContactGroup contact;
    
    public Output() {
        lamp = new LampFrame();
        contact = new ContactGroup.Horizontal();
    }
    
    public LampFrame lamp() {
        return lamp;
    }
    
    public ContactGroup contact() {
        return contact;
    }
}
