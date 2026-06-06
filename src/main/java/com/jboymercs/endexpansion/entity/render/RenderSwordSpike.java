package com.jboymercs.endexpansion.entity.render;

import com.jboymercs.endexpansion.entity.EntitySwordSpike;
import com.jboymercs.endexpansion.entity.model.ModelSwordSpike;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderSwordSpike extends GeoEntityRenderer<EntitySwordSpike> {
    public RenderSwordSpike(RenderManager renderManager) {
        super(renderManager, new ModelSwordSpike());
    }

    @Override
    public void doRender(EntitySwordSpike entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }
}
