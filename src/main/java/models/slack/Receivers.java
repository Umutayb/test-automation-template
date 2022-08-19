package models.slack;

import java.util.List;

public class Receivers {

    List<Receiver> receivers;

    public List<Receiver> receivers() {return receivers;}

    public static class Receiver {
        String name;
        String userId;

        public String name() {return name;}

        public String userId() {return userId;}
    }
}
