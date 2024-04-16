package com.example.structure.entity.render;

import com.example.structure.entity.barrend.EntityBarrendGolem;
import com.example.structure.entity.model.ModelBarrendGolem;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderBarrendGolem extends GeoEntityRenderer<EntityBarrendGolem> {
    public RenderBarrendGolem(RenderManager renderManager) {
        super(renderManager, new ModelBarrendGolem());
    }

    @Override
    public void doRender(EntityBarrendGolem entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }
}
