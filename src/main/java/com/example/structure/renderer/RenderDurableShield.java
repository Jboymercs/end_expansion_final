package com.example.structure.renderer;

import com.example.structure.items.DurableShield;
import com.example.structure.model.ModelDurableShield;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderDurableShield extends GeoItemRenderer<DurableShield> {
    public RenderDurableShield() {
        super(new ModelDurableShield());
    }
}
