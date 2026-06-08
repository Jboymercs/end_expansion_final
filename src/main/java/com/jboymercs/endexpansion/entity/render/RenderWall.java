package com.jboymercs.endexpansion.entity.render;

import com.jboymercs.endexpansion.entity.EntityWall;
import com.jboymercs.endexpansion.entity.model.ModelWall;
import net.minecraft.client.renderer.entity.RenderManager;

import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderWall extends GeoEntityRenderer<EntityWall> {
    public RenderWall(RenderManager renderManager) {
        super(renderManager, new ModelWall());
    }
}
