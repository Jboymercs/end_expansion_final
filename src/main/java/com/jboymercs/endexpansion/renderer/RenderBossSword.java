package com.jboymercs.endexpansion.renderer;

import com.jboymercs.endexpansion.items.tools.ToolBossSword;
import com.jboymercs.endexpansion.model.ModelSword;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderBossSword  extends GeoItemRenderer<ToolBossSword> {

    public RenderBossSword() {
        super(new ModelSword());
    }
}
