package com.example.structure.entity.render;

import com.example.structure.entity.endking.EntityFireBall;
import com.example.structure.entity.endking.EntityNuclearExplosion;
import com.example.structure.entity.model.ModelNuclearExplosion;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class RenderNuclearExplosion extends RenderAbstractGeoEntity<EntityNuclearExplosion>{
    public RenderNuclearExplosion(RenderManager renderManager) {
        super(renderManager, new ModelNuclearExplosion());
    }

    @Override
    public void doRender(EntityNuclearExplosion entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        if(entity.damageWorld) {
            GlStateManager.scale(3.0, 3.0, 3.0);
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }
}
