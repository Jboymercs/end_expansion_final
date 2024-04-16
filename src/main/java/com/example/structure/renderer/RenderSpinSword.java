package com.example.structure.renderer;

import com.example.structure.items.SpinSwordItem;
import com.example.structure.model.ModelSpinSword;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderSpinSword extends GeoItemRenderer<SpinSwordItem> {

    public RenderSpinSword() {
        super(new ModelSpinSword());
    }
}
