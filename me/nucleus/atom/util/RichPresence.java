// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.util;

import me.nucleus.atom.AtomMod;
import java.io.Reader;
import java.io.InputStreamReader;
import com.google.gson.Gson;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class RichPresence
{
    public static RichPresence INSTANCE;
    public CustomUser[] customUsers;
    
    public RichPresence() {
        RichPresence.INSTANCE = this;
        try {
            final HttpsURLConnection connection = (HttpsURLConnection)new URL("https://raw.githubusercontent.com/S-B99/kamiblue/assets/assets/donators.json").openConnection();
            connection.connect();
            this.customUsers = (CustomUser[])new Gson().fromJson((Reader)new InputStreamReader(connection.getInputStream()), (Class)CustomUser[].class);
            connection.disconnect();
        }
        catch (Exception e) {
            AtomMod.log.error("Failed to load donators");
            e.printStackTrace();
        }
    }
    
    public static class CustomUser
    {
        public String uuid;
        public String type;
    }
}
