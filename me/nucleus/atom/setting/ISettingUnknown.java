// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.setting;

public interface ISettingUnknown
{
    String getName();
    
    Class getValueClass();
    
    String getValueAsString();
    
    boolean isVisible();
    
    void setValueFromString(final String p0);
}
