// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.gui;

import net.minecraft.client.Minecraft;
import java.util.ArrayList;
import net.minecraft.util.text.TextFormatting;
import me.nucleus.atom.module.modules.hidden.TimerSpeed;
import me.nucleus.atom.util.InfoCalculator;
import java.util.Locale;
import java.text.SimpleDateFormat;
import me.nucleus.atom.setting.Settings;
import me.nucleus.atom.setting.Setting;
import me.nucleus.atom.module.Module;

@Info(name = "InfoOverlay", category = Category.GUI, description = "Configures game information overlay", showOnArray = ShowOnArray.OFF)
public class InfoOverlay extends Module
{
    public Setting<Boolean> version;
    public Setting<Boolean> username;
    public Setting<Boolean> time;
    public Setting<Boolean> tps;
    public Setting<Boolean> fps;
    public Setting<Boolean> speed;
    public Setting<Boolean> timerSpeed;
    public Setting<Boolean> ping;
    public Setting<Boolean> durability;
    public Setting<Boolean> memory;
    private Setting<SpeedUnit> speedUnit;
    public Setting<ColourCode> firstColour;
    public Setting<ColourCode> secondColour;
    private Setting<TimeType> timeTypeSetting;
    public Setting<TimeUnit> timeUnitSetting;
    
    public InfoOverlay() {
        this.version = this.register(Settings.b("Version", true));
        this.username = this.register(Settings.b("Username", true));
        this.time = this.register(Settings.b("Time", true));
        this.tps = this.register(Settings.b("Ticks Per Second", false));
        this.fps = this.register(Settings.b("Frames Per Second", true));
        this.speed = this.register(Settings.b("Speed", true));
        this.timerSpeed = this.register(Settings.b("Timer Speed", false));
        this.ping = this.register(Settings.b("Latency", false));
        this.durability = this.register(Settings.b("Item Damage", false));
        this.memory = this.register(Settings.b("Memory Used", false));
        this.speedUnit = this.register(Settings.e("Speed Unit", SpeedUnit.KmH));
        this.firstColour = this.register(Settings.e("First Colour", ColourCode.WHITE));
        this.secondColour = this.register(Settings.e("Second Colour", ColourCode.BLUE));
        this.timeTypeSetting = this.register(Settings.e("Time Format", TimeType.HHMMSS));
        this.timeUnitSetting = this.register(Settings.e("Time Unit", TimeUnit.h12));
    }
    
    public boolean useUnitKmH() {
        return this.speedUnit.getValue().equals(SpeedUnit.KmH);
    }
    
    private String unitType(final SpeedUnit s) {
        switch (s) {
            case MpS: {
                return "m/s";
            }
            case KmH: {
                return "km/h";
            }
            default: {
                return "Invalid unit type (mps or kmh)";
            }
        }
    }
    
    public SimpleDateFormat dateFormatter(final TimeUnit timeUnit) {
        SimpleDateFormat formatter = null;
        switch (timeUnit) {
            case h12: {
                formatter = new SimpleDateFormat("HH" + formatTimeString(this.timeTypeSetting.getValue()), Locale.UK);
                break;
            }
            case h24: {
                formatter = new SimpleDateFormat("hh" + formatTimeString(this.timeTypeSetting.getValue()), Locale.UK);
                break;
            }
            default: {
                throw new IllegalStateException("Unexpected value: " + timeUnit);
            }
        }
        return formatter;
    }
    
    private static String formatTimeString(final TimeType timeType) {
        switch (timeType) {
            case HHMM: {
                return ":mm";
            }
            case HHMMSS: {
                return ":mm:ss";
            }
            default: {
                return "";
            }
        }
    }
    
    private String formatTimeColour() {
        final String formatted = this.textColour(this.secondColour.getValue()) + ":" + this.textColour(this.firstColour.getValue());
        return InfoCalculator.time(this.dateFormatter(this.timeUnitSetting.getValue())).replace(":", formatted);
    }
    
    private String formatTimerSpeed() {
        final String formatted = this.textColour(this.secondColour.getValue()) + "." + this.textColour(this.firstColour.getValue());
        return TimerSpeed.returnGui().replace(".", formatted);
    }
    
    public String textColour(final ColourCode c) {
        switch (c) {
            case BLACK: {
                return TextFormatting.BLACK.toString();
            }
            case DARK_BLUE: {
                return TextFormatting.DARK_BLUE.toString();
            }
            case DARK_GREEN: {
                return TextFormatting.DARK_GREEN.toString();
            }
            case DARK_AQUA: {
                return TextFormatting.DARK_AQUA.toString();
            }
            case DARK_RED: {
                return TextFormatting.DARK_RED.toString();
            }
            case DARK_PURPLE: {
                return TextFormatting.DARK_PURPLE.toString();
            }
            case GOLD: {
                return TextFormatting.GOLD.toString();
            }
            case GREY: {
                return TextFormatting.GRAY.toString();
            }
            case DARK_GREY: {
                return TextFormatting.DARK_GRAY.toString();
            }
            case BLUE: {
                return TextFormatting.BLUE.toString();
            }
            case GREEN: {
                return TextFormatting.GREEN.toString();
            }
            case AQUA: {
                return TextFormatting.AQUA.toString();
            }
            case RED: {
                return TextFormatting.RED.toString();
            }
            case LIGHT_PURPLE: {
                return TextFormatting.LIGHT_PURPLE.toString();
            }
            case YELLOW: {
                return TextFormatting.YELLOW.toString();
            }
            case WHITE: {
                return TextFormatting.WHITE.toString();
            }
            default: {
                return "";
            }
        }
    }
    
    public ArrayList<String> infoContents() {
        final ArrayList<String> infoContents = new ArrayList<String>();
        if (this.version.getValue()) {
            infoContents.add(this.textColour(this.firstColour.getValue()) + "ATOM" + this.textColour(this.secondColour.getValue()) + " " + "v0.1");
        }
        if (this.username.getValue()) {
            infoContents.add(this.textColour(this.firstColour.getValue()) + "Welcome" + this.textColour(this.secondColour.getValue()) + " " + InfoOverlay.mc.field_71439_g.func_70005_c_() + "!");
        }
        if (this.time.getValue()) {
            infoContents.add(this.textColour(this.firstColour.getValue()) + this.formatTimeColour() + TextFormatting.RESET);
        }
        if (this.tps.getValue()) {
            infoContents.add(this.textColour(this.firstColour.getValue()) + InfoCalculator.tps() + this.textColour(this.secondColour.getValue()) + " tps");
        }
        if (this.fps.getValue()) {
            infoContents.add(this.textColour(this.firstColour.getValue()) + Minecraft.field_71470_ab + this.textColour(this.secondColour.getValue()) + " fps");
        }
        if (this.speed.getValue()) {
            infoContents.add(this.textColour(this.firstColour.getValue()) + InfoCalculator.speed() + this.textColour(this.secondColour.getValue()) + " " + this.unitType(this.speedUnit.getValue()));
        }
        if (this.timerSpeed.getValue()) {
            infoContents.add(this.textColour(this.firstColour.getValue()) + this.formatTimerSpeed() + this.textColour(this.secondColour.getValue()) + "t");
        }
        if (this.ping.getValue()) {
            infoContents.add(this.textColour(this.firstColour.getValue()) + InfoCalculator.ping() + this.textColour(this.secondColour.getValue()) + " ms");
        }
        if (this.durability.getValue()) {
            infoContents.add(this.textColour(this.firstColour.getValue()) + InfoCalculator.dura() + this.textColour(this.secondColour.getValue()) + " dura");
        }
        if (this.memory.getValue()) {
            infoContents.add(this.textColour(this.firstColour.getValue()) + InfoCalculator.memory() + this.textColour(this.secondColour.getValue()) + "mB free");
        }
        return infoContents;
    }
    
    public void onDisable() {
        this.enable();
    }
    
    private enum SpeedUnit
    {
        MpS, 
        KmH;
    }
    
    private enum TimeType
    {
        HHMM, 
        HHMMSS, 
        HH;
    }
    
    public enum TimeUnit
    {
        h24, 
        h12;
    }
    
    public enum ColourCode
    {
        BLACK, 
        DARK_BLUE, 
        DARK_GREEN, 
        DARK_AQUA, 
        DARK_RED, 
        DARK_PURPLE, 
        GOLD, 
        GREY, 
        DARK_GREY, 
        BLUE, 
        GREEN, 
        AQUA, 
        RED, 
        LIGHT_PURPLE, 
        YELLOW, 
        WHITE;
    }
}
