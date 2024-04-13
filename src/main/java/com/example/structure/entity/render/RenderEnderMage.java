package com.example.structure.entity.render;

import com.example.structure.entity.EntityEnderKnight;
import com.example.structure.entity.knighthouse.EntityEnderMage;
import com.example.structure.entity.model.ModelEnderMage;
import com.example.structure.entity.render.geo.GeoGlowingLayer;
import com.example.structure.entity.render.geo.RenderGeoExtended;
import com.example.structure.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

public class RenderEnderMage extends RenderGeoExtended<EntityEnderMage> {

    public static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/endknight/geo.endknight.json");

    public static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/endmage_1.png");
    public RenderEnderMage(RenderManager renderManager) {
        super(renderManager, new ModelEnderMage(MODEL_RESLOC, TEXTURE, "end_mage"));
        this.addLayer(new GeoGlowingLayer<EntityEnderMage>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Override
    public void doRender(EntityEnderMage entity, double x, double y, double z, float entityYaw, float partialTicks) {
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
    protected ItemStack getHeldItemForBone(String boneName, EntityEnderMage currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityEnderMage currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityEnderMage currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityEnderMage currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityEnderMage currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityEnderMage currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityEnderMage currentEntity) {
        return null;
    }
}
