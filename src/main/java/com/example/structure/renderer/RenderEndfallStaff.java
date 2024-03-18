package com.example.structure.renderer;

import com.example.structure.items.ItemEndfallStaff;
import com.example.structure.model.ModelEndfallStaff;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderEndfallStaff extends GeoItemRenderer<ItemEndfallStaff> {
    public RenderEndfallStaff() {
        super(new ModelEndfallStaff());
    }
}
