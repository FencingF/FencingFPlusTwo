package org.fenci.fencingfplus2.manager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CapeManager {

    private final List<UUID> capes = new ArrayList<>();

    public CapeManager() {
        try {
//            URL capesList = new URL("https://pastebin.com/qz1LcRzc");
//            BufferedReader in = new BufferedReader(new InputStreamReader(capesList.openStream()));
//            String inputLine;
//            while ((inputLine = in.readLine()) != null) {
//                capes.add(UUID.fromString(inputLine));
//            }
            capes.add(UUID.fromString("92180b46-89a0-4557-95f5-c16dae821a6c")); //nuxvomika
            capes.add(UUID.fromString("976bd8e3-8379-4ad5-8dba-4fe20e42ee6e")); //FencingF
            capes.add(UUID.fromString("8f834fe2-a0be-4e74-9154-e0031103cec3")); //AndrewMC12
            capes.add(UUID.fromString("6413e0df-fd5f-4973-acf4-afb43329f995")); //flixzit
            capes.add(UUID.fromString("03899aba-db86-40fa-96cc-33c7a0da529f")); //_FencingF_
            capes.add(UUID.fromString("873c0367-7ba9-4a9a-96ae-fa312ae756cb")); //Aestheticall
            capes.add(UUID.fromString("4f0e3c55-3a13-46f8-a330-71fe96b148f1")); //PFTpancake
            capes.add(UUID.fromString("ccb9d467-1967-4b85-8176-9edd977ad603")); //vNorth
        } catch (Exception ignored) {
        }
    }

    public boolean hasCape(UUID uuid) {
        return this.capes.contains(uuid);
    }
}
