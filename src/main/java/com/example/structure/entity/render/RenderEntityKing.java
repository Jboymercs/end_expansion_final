package com.example.structure.entity.render;

import com.example.structure.entity.endking.EntityEndKing;
import com.example.structure.entity.model.ModelEndKing;
import com.example.structure.entity.render.geo.GeoGlowingLayer;
import com.example.structure.entity.render.geo.RenderGeoExtended;
import com.example.structure.renderer.ITarget;
import com.example.structure.util.ModColors;
import com.example.structure.util.ModReference;
import com.example.structure.util.ModUtils;
import com.example.structure.util.RenderUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderDragon;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class RenderEntityKing extends RenderGeoExtended<EntityEndKing> {

    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/king/geo.king.json");

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/king.png");
    public RenderEntityKing(RenderManager renderManager) {
        super(renderManager, new ModelEndKing(MODEL_RESLOC, TEXTURE, "end_king"));

        this.addLayer(new GeoGlowingLayer<EntityEndKing>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }
    @Override
    public void doRender(EntityEndKing entity, double x, double y, double z, float entityYaw, float partialTicks) {
        //Help from the Gauntlet in Maelstrom for Rendering the Lazer
        if (entity.renderLazerPos != null) {
            // This sort of jenky way of binding the wrong texture to the original guardian beam creates quite a nice particle beam visual
            renderManager.renderEngine.bindTexture(RenderDragon.ENDERCRYSTAL_BEAM_TEXTURES);
            // We must interpolate between positions to make the move smoothly
            Vec3d interpolatedPos = entity.renderLazerPos.subtract(entity.prevRenderLazerPos).scale(partialTicks).add(entity.prevRenderLazerPos);
            RenderUtil.drawBeam(renderManager, entity.getPositionEyes(1), interpolatedPos, new Vec3d(x, y, z), ModColors.RED, entity, partialTicks);
        }
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityEndKing currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityEndKing currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityEndKing currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityEndKing currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityEndKing currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityEndKing currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityEndKing currentEntity) {
        return null;
    }

    //Used for Lazer and V Rendering
    @Override
    public boolean shouldRender(@Nonnull EntityEndKing livingEntity, @Nonnull ICamera camera, double camX, double camY, double camZ) {
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
