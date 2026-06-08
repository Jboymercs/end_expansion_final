package com.jboymercs.endexpansion.renderer;

import com.jboymercs.endexpansion.items.DurableShield;
import com.jboymercs.endexpansion.model.ModelDurableShield;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderDurableShield extends GeoItemRenderer<DurableShield> {
    public RenderDurableShield() {
        super(new ModelDurableShield());
    }
}
