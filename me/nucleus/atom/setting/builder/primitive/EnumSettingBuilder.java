// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.setting.builder.primitive;

import me.nucleus.atom.setting.impl.EnumSetting;
import me.nucleus.atom.setting.Setting;
import me.nucleus.atom.setting.builder.SettingBuilder;

public class EnumSettingBuilder<T extends Enum> extends SettingBuilder<T>
{
    Class<? extends Enum> clazz;
    
    public EnumSettingBuilder(final Class<? extends Enum> clazz) {
        this.clazz = clazz;
    }
    
    @Override
    public Setting<T> build() {
        return new EnumSetting<T>(this.initialValue, this.predicate(), this.consumer(), this.name, this.visibilityPredicate(), this.clazz);
    }
}
