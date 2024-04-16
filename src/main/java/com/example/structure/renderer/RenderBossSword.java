package com.example.structure.renderer;

import com.example.structure.items.tools.ToolBossSword;
import com.example.structure.model.ModelSword;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderBossSword  extends GeoItemRenderer<ToolBossSword> {

    public RenderBossSword() {
        super(new ModelSword());
    }
}
