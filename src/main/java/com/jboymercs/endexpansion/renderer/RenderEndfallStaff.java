package com.jboymercs.endexpansion.renderer;

import com.jboymercs.endexpansion.items.ItemEndfallStaff;
import com.jboymercs.endexpansion.model.ModelEndfallStaff;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderEndfallStaff extends GeoItemRenderer<ItemEndfallStaff> {
    public RenderEndfallStaff() {
        super(new ModelEndfallStaff());
    }
}
