package com.example.structure.entity.render;

import com.example.structure.entity.model.ModelControllerLift;
import com.example.structure.entity.trader.EntityControllerLift;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;

public class RenderControllerLift extends RenderAbstractGeoEntity<EntityControllerLift>{

    public RenderControllerLift(RenderManager renderManager) {
        super(renderManager, new ModelControllerLift());
        this.shadowSize = 0.0F;
    }

    @Override
    public void doRender(EntityControllerLift entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }
}
