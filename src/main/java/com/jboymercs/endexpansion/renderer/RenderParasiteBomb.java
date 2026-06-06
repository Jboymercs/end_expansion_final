package com.jboymercs.endexpansion.renderer;

import com.jboymercs.endexpansion.items.ItemProjectileBomb;
import com.jboymercs.endexpansion.model.ModelParasiteBomb;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderParasiteBomb extends GeoItemRenderer<ItemProjectileBomb> {

    public RenderParasiteBomb() {
        super(new ModelParasiteBomb());
    }
}
