package com.example.structure.entity.render;

import com.example.structure.entity.EntityMiniNuke;
import com.example.structure.entity.model.ModelMiniNuke;
import net.minecraft.client.renderer.entity.RenderManager;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderMiniNuke extends GeoEntityRenderer<EntityMiniNuke> {
    public RenderMiniNuke(RenderManager renderManager) {
        super(renderManager, new ModelMiniNuke());
    }


}
