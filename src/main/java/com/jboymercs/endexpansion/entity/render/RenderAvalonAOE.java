package com.jboymercs.endexpansion.entity.render;

import com.jboymercs.endexpansion.entity.model.ModelAvalonAOE;
import com.jboymercs.endexpansion.entity.trader.EntityAOEArena;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;

public class RenderAvalonAOE extends RenderAbstractGeoEntity<EntityAOEArena>{

    public RenderAvalonAOE(RenderManager renderManager) {
        super(renderManager, new ModelAvalonAOE());
        this.shadowSize = 0.0F;
    }

    @Override
    public void doRender(EntityAOEArena entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }
}
