package com.example.structure.renderer;

import com.example.structure.items.CrystalBallItem;
import com.example.structure.model.ModelCrystalBall;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderCrystalBall extends GeoItemRenderer<CrystalBallItem> {
    public RenderCrystalBall() {
        super(new ModelCrystalBall());
    }

}
