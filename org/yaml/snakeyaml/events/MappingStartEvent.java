// 
// Decompiled by Procyon v0.5.36
// 

package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.error.Mark;

public final class MappingStartEvent extends CollectionStartEvent
{
    public MappingStartEvent(final String anchor, final String tag, final boolean implicit, final Mark startMark, final Mark endMark, final Boolean flowStyle) {
        super(anchor, tag, implicit, startMark, endMark, flowStyle);
    }
    
    @Override
    public boolean is(final ID id) {
        return ID.MappingStart == id;
    }
}
