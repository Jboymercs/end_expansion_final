package com.example.structure.entity.render;

import com.example.structure.entity.EntityChomper;
import com.example.structure.entity.barrend.guard.EntityBarrendGuard;
import com.example.structure.entity.model.ModelBarrendGuard;
import com.example.structure.entity.render.geo.GeoGlowingLayer;
import com.example.structure.entity.render.geo.RenderGeoExtended;
import com.example.structure.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class RenderBarrendGuard extends RenderGeoExtended<EntityBarrendGuard> {

    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/guard/geo.barrend_guard.json");

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/barrend_guard.png");

    public RenderBarrendGuard(RenderManager renderManager) {
        super(renderManager, new ModelBarrendGuard(MODEL_RESLOC, TEXTURE, "barrend_guard"));
        this.addLayer(new GeoGlowingLayer<EntityBarrendGuard>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Override
    public void doRender(EntityBarrendGuard entity, double x, double y, double z, float entityYaw, float partialTicks) {
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
    protected ItemStack getHeldItemForBone(String boneName, EntityBarrendGuard currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityBarrendGuard currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityBarrendGuard currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityBarrendGuard currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityBarrendGuard currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityBarrendGuard currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityBarrendGuard currentEntity) {
        return null;
    }
}
