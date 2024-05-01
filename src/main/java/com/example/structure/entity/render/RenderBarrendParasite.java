package com.example.structure.entity.render;

import com.example.structure.entity.EntityBarrendParasite;
import com.example.structure.entity.model.ModelBarrendParasite;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderBarrendParasite extends GeoEntityRenderer<EntityBarrendParasite> {
    public RenderBarrendParasite(RenderManager renderManager) {
        super(renderManager, new ModelBarrendParasite());
    }

    @Override
    public void doRender(EntityBarrendParasite entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();


    }
}
