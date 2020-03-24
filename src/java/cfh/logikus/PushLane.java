package cfh.logikus;

public class PushLane extends Module {

    private final SwitchContact contact;
    private final PushButton button;
    
    public PushLane() {
        contact = new SwitchContact.Vertical();
        button = new PushButton();
    }
    
    public SwitchContact contact() {
        return contact;
    }
    
    public PushButton button() {
        return button;
    }
}
