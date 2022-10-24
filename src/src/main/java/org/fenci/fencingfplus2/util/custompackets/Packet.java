package org.fenci.fencingfplus2.util.custompackets;

import java.io.Serializable;

public class Packet implements Serializable {
    private final String name;

    public Packet(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
