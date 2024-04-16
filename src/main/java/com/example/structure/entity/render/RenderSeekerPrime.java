package com.example.structure.entity.render;

import com.example.structure.entity.model.ModelEndSeekerPrime;
import com.example.structure.entity.render.geo.GeoGlowingLayer;
import com.example.structure.entity.render.geo.RenderGeoExtended;
import com.example.structure.entity.seekers.EndSeekerPrime;
import com.example.structure.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderSeekerPrime extends RenderGeoExtended<EndSeekerPrime> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/seeker_prime.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/seeker/geo.seeker_prime.json");
    public RenderSeekerPrime(RenderManager renderManager) {
        super(renderManager, new ModelEndSeekerPrime(MODEL_RESLOC, TEXTURE, "end_seeker_prime"));
        this.addLayer(new GeoGlowingLayer<EndSeekerPrime>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
        this.shadowSize = 0.6f;
    }

    @Override
    public void doRender(EndSeekerPrime entity, double x, double y, double z, float entityYaw, float partialTicks) {
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
    protected ItemStack getHeldItemForBone(String boneName, EndSeekerPrime currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EndSeekerPrime currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EndSeekerPrime currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EndSeekerPrime currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EndSeekerPrime currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EndSeekerPrime currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EndSeekerPrime currentEntity) {
        return null;
    }
}
