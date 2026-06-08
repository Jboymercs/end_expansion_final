package com.jboymercs.endexpansion.renderer;

import com.jboymercs.endexpansion.items.CrystalBallItem;
import com.jboymercs.endexpansion.model.ModelPurpleProjectile;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderPurple extends GeoItemRenderer<CrystalBallItem> {
    public RenderPurple() {
        super(new ModelPurpleProjectile());
    }
}
