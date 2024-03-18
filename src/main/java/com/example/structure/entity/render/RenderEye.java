package com.example.structure.entity.render;

import com.example.structure.entity.EntityEye;
import com.example.structure.entity.endking.ghosts.EntityGhostPhase;
import com.example.structure.entity.model.ModelEye;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class RenderEye extends RenderAbstractGeoEntity<EntityEye>{

    public RenderEye(RenderManager renderManager) {
        super(renderManager, new ModelEye());
    }

    @Override
    public void doRender(EntityEye entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }
}
