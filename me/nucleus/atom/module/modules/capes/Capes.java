// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.capes;

import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.awt.Image;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import java.io.File;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import me.nucleus.atom.util.Wrapper;
import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.entity.AbstractClientPlayer;
import me.nucleus.atom.AtomMod;
import java.io.Reader;
import java.io.InputStreamReader;
import com.google.gson.Gson;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class Capes
{
    public static Capes INSTANCE;
    public CapeUser[] capeUser;
    
    public Capes() {
        Capes.INSTANCE = this;
        try {
            final HttpsURLConnection connection = (HttpsURLConnection)new URL("https://raw.githubusercontent.com/S-B99/kamiblue/assets/assets/capes.json").openConnection();
            connection.connect();
            this.capeUser = (CapeUser[])new Gson().fromJson((Reader)new InputStreamReader(connection.getInputStream()), (Class)CapeUser[].class);
            connection.disconnect();
        }
        catch (Exception e) {
            AtomMod.log.error("Failed to load capes");
        }
        if (this.capeUser != null) {
            for (final CapeUser user : this.capeUser) {
                this.bindTexture(user.url, "capes/kami/" + formatUUID(user.uuid));
            }
        }
    }
    
    public static ResourceLocation getCapeResource(final AbstractClientPlayer player) {
        for (final CapeUser user : Capes.INSTANCE.capeUser) {
            if (player.func_110124_au().toString().equalsIgnoreCase(user.uuid)) {
                return new ResourceLocation("capes/kami/" + formatUUID(user.uuid));
            }
        }
        return null;
    }
    
    public void bindTexture(final String url, final String resource) {
        final IImageBuffer iib = (IImageBuffer)new IImageBuffer() {
            public BufferedImage func_78432_a(final BufferedImage image) {
                return Capes.this.parseCape(image);
            }
            
            public void func_152634_a() {
            }
        };
        final ResourceLocation rl = new ResourceLocation(resource);
        final TextureManager textureManager = Wrapper.getMinecraft().func_110434_K();
        textureManager.func_110581_b(rl);
        final ThreadDownloadImageData textureCape = new ThreadDownloadImageData((File)null, url, (ResourceLocation)null, iib);
        textureManager.func_110579_a(rl, (ITextureObject)textureCape);
    }
    
    private BufferedImage parseCape(final BufferedImage img) {
        int imageWidth = 64;
        int imageHeight = 32;
        for (int srcWidth = img.getWidth(), srcHeight = img.getHeight(); imageWidth < srcWidth || imageHeight < srcHeight; imageWidth *= 2, imageHeight *= 2) {}
        final BufferedImage imgNew = new BufferedImage(imageWidth, imageHeight, 2);
        final Graphics g = imgNew.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return imgNew;
    }
    
    private static String formatUUID(final String uuid) {
        return uuid.replaceAll("-", "");
    }
    
    public class CapeUser
    {
        public String uuid;
        public String url;
    }
}
