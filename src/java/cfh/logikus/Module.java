package cfh.logikus;

import java.util.stream.Stream;

public interface Module {

    public String id();
    public Stream<Contact> contacts();

}
