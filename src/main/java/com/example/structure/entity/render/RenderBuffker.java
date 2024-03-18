package com.example.structure.entity.render;

import com.example.structure.entity.EntityBuffker;
import com.example.structure.entity.EntityCrystalKnight;
import com.example.structure.entity.model.ModelBuffker;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderBuffker extends GeoEntityRenderer<EntityBuffker> {

    public RenderBuffker(RenderManager renderManager) {
        super(renderManager, new ModelBuffker());
    }

    @Override
    public void doRender(EntityBuffker entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }
}
