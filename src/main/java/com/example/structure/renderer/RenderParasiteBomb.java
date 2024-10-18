package com.example.structure.renderer;

import com.example.structure.items.CrystalBallItem;
import com.example.structure.items.ItemProjectileBomb;
import com.example.structure.model.ModelCrystalBall;
import com.example.structure.model.ModelParasiteBomb;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderParasiteBomb extends GeoItemRenderer<ItemProjectileBomb> {

    public RenderParasiteBomb() {
        super(new ModelParasiteBomb());
    }
}
