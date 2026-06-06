package com.jboymercs.endexpansion.renderer;

import com.jboymercs.endexpansion.items.CrystalBallItem;
import com.jboymercs.endexpansion.model.ModelCrystalBall;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderCrystalBall extends GeoItemRenderer<CrystalBallItem> {
    public RenderCrystalBall() {
        super(new ModelCrystalBall());
    }

}
