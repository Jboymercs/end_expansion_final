package com.example.structure.renderer;

import com.example.structure.items.CrystalBallItem;
import com.example.structure.model.ModelPurpleProjectile;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderPurple extends GeoItemRenderer<CrystalBallItem> {
    public RenderPurple() {
        super(new ModelPurpleProjectile());
    }
}
