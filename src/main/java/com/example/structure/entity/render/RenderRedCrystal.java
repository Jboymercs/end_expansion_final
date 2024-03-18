package com.example.structure.entity.render;

import com.example.structure.entity.endking.EntityRedCrystal;
import com.example.structure.entity.model.ModelRedCrystal;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;

public class RenderRedCrystal extends RenderAbstractGeoEntity<EntityRedCrystal>{
    public RenderRedCrystal(RenderManager renderManager) {
        super(renderManager, new ModelRedCrystal());
    }


    @Override
    public void doRender(EntityRedCrystal entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }
}
