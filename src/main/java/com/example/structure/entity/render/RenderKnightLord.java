package com.example.structure.entity.render;

import com.example.structure.entity.knighthouse.EntityEnderMage;
import com.example.structure.entity.knighthouse.EntityKnightLord;
import com.example.structure.entity.model.ModelKnightLord;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderKnightLord extends GeoEntityRenderer<EntityKnightLord> {
    public RenderKnightLord(RenderManager renderManager) {
        super(renderManager, new ModelKnightLord());
    }

    @Override
    public void doRender(EntityKnightLord entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }
}
