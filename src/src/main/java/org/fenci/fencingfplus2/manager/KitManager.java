package org.fenci.fencingfplus2.manager;

import org.fenci.fencingfplus2.FencingFPlus2;
import org.fenci.fencingfplus2.util.Globals;

import java.util.ArrayList;
import java.util.Hashtable;

public class KitManager implements Globals {
    public Hashtable<String, String> serverIpKits = new Hashtable<String, String>();
    ArrayList<String> kits = new ArrayList<>();
    String kit;

    public void addAnyKit(String name) {
        this.kits.add(name);
    }

    public void removeAnyKit() {
        this.kits.clear();
    }

    public ArrayList<String> getAnyKit() {
        return this.kits;
    }

    public String getAnyManagedKitName() {
        kit = FencingFPlus2.INSTANCE.kitManager.getAnyKit().toString();
        kit = kit.replaceAll("[\\p{Ps}\\p{Pe}]", "");
        return kit;
    }

    public void addIpKit(String ip, String kitName) {
        serverIpKits.put(ip, kitName);
    }

    public void removeIpKit(String ip) {
        serverIpKits.remove(ip);
    }

    public String getKitFromIp(String ip) {
        return serverIpKits.get(ip);
    }
}