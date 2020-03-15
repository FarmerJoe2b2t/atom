// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.setting.builder.primitive;

import me.nucleus.atom.setting.Setting;
import me.nucleus.atom.setting.impl.StringSetting;
import me.nucleus.atom.setting.builder.SettingBuilder;

public class StringSettingBuilder extends SettingBuilder<String>
{
    @Override
    public StringSetting build() {
        return new StringSetting((String)this.initialValue, this.predicate(), this.consumer(), this.name, this.visibilityPredicate());
    }
}
