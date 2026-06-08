package com.jboymercs.endexpansion.renderer;

import com.jboymercs.endexpansion.items.SpinSwordItem;
import com.jboymercs.endexpansion.model.ModelSpinSword;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderSpinSword extends GeoItemRenderer<SpinSwordItem> {

    public RenderSpinSword() {
        super(new ModelSpinSword());
    }
}
