package com.jboymercs.endexpansion.entity.render;

import com.jboymercs.endexpansion.entity.EntityMiniNuke;
import com.jboymercs.endexpansion.entity.model.ModelMiniNuke;
import net.minecraft.client.renderer.entity.RenderManager;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderMiniNuke extends GeoEntityRenderer<EntityMiniNuke> {
    public RenderMiniNuke(RenderManager renderManager) {
        super(renderManager, new ModelMiniNuke());
    }


}
