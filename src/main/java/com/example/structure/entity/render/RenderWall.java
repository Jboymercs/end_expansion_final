package com.example.structure.entity.render;

import com.example.structure.entity.EntityWall;
import com.example.structure.entity.model.ModelWall;
import net.minecraft.client.renderer.entity.RenderManager;

import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderWall extends GeoEntityRenderer<EntityWall> {
    public RenderWall(RenderManager renderManager) {
        super(renderManager, new ModelWall());
    }
}
