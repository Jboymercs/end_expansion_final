package com.example.structure.renderer;

import com.example.structure.items.tools.ToolEndFallSword;
import com.example.structure.model.ModelEndFallSword;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderEndFallSword extends GeoItemRenderer<ToolEndFallSword> {
    public RenderEndFallSword() {
        super(new ModelEndFallSword());
    }
}
