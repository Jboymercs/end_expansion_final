package com.example.structure.entity.render;

import com.example.structure.entity.knighthouse.EntityHealAura;
import com.example.structure.entity.model.ModelHealingAura;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;

public class RenderHealingAura extends RenderAbstractGeoEntity<EntityHealAura>{
    public RenderHealingAura(RenderManager renderManager) {
        super(renderManager, new ModelHealingAura());
        this.shadowSize = 0.0f;
    }

    @Override
    public void doRender(EntityHealAura entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }
}
