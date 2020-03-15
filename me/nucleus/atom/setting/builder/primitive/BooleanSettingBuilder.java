// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.setting.builder.primitive;

import me.nucleus.atom.setting.Setting;
import me.nucleus.atom.setting.impl.BooleanSetting;
import me.nucleus.atom.setting.builder.SettingBuilder;

public class BooleanSettingBuilder extends SettingBuilder<Boolean>
{
    @Override
    public BooleanSetting build() {
        return new BooleanSetting((Boolean)this.initialValue, this.predicate(), this.consumer(), this.name, this.visibilityPredicate());
    }
    
    @Override
    public BooleanSettingBuilder withName(final String name) {
        return (BooleanSettingBuilder)super.withName(name);
    }
}
