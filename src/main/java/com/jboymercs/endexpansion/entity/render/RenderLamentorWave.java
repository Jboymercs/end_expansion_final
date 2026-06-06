package com.jboymercs.endexpansion.entity.render;

import com.jboymercs.endexpansion.entity.lamentorUtil.EntityLamentorWave;
import com.jboymercs.endexpansion.entity.model.ModelLamentorWave;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;

public class RenderLamentorWave extends RenderAbstractGeoEntity<EntityLamentorWave>{

    public RenderLamentorWave(RenderManager renderManager) {
        super(renderManager, new ModelLamentorWave());
    }

    @Override
    public void doRender(EntityLamentorWave entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }
}
