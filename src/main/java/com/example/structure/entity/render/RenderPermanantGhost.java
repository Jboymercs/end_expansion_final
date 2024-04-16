package com.example.structure.entity.render;

import com.example.structure.entity.endking.ghosts.EntityPermanantGhost;
import com.example.structure.entity.model.ModelPermanantGhost;
import com.example.structure.renderer.ITarget;
import com.example.structure.util.ModUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;
import java.util.Optional;

public class RenderPermanantGhost extends RenderAbstractGeoEntity<EntityPermanantGhost>{
    public RenderPermanantGhost(RenderManager renderManager) {
        super(renderManager, new ModelPermanantGhost());
        this.shadowSize = 0.0f;
    }


    @Override
    public void doRender(EntityPermanantGhost entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }


    @Override
    public boolean shouldRender(@Nonnull EntityPermanantGhost livingEntity, @Nonnull ICamera camera, double camX, double camY, double camZ) {
        if (super.shouldRender(livingEntity, camera, camX, camY, camZ))
        {
            return true;
        }

        Optional<Vec3d> optional = ((ITarget) livingEntity).getTarget();
        if(optional.isPresent()) {
            Vec3d end = optional.get();
            Vec3d start = livingEntity.getPositionEyes(1);
            return camera.isBoundingBoxInFrustum(ModUtils.makeBox(start, end));
        }

        return false;
    }
}
