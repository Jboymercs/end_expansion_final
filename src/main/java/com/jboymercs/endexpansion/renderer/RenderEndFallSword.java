package com.jboymercs.endexpansion.renderer;

import com.jboymercs.endexpansion.items.tools.ToolEndFallSword;
import com.jboymercs.endexpansion.model.ModelEndFallSword;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderEndFallSword extends GeoItemRenderer<ToolEndFallSword> {
    public RenderEndFallSword() {
        super(new ModelEndFallSword());
    }
}
