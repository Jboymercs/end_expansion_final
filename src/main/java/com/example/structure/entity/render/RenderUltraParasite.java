package com.example.structure.entity.render;

import com.example.structure.entity.barrend.EntityUltraParasite;
import com.example.structure.entity.barrend.EntityVoidTripod;
import com.example.structure.entity.model.ModelUltraParasite;
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

public class RenderUltraParasite extends RenderGeoExtended<EntityUltraParasite> {

    public static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/ultraparasite/geo.ultra_parasite.json");

    public static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/ultra_parasite.png");

    public RenderUltraParasite(RenderManager renderManager) {
        super(renderManager, new ModelUltraParasite(MODEL_RESLOC, TEXTURE, "big_rick"));
    }

    @Override
    public void doRender(EntityUltraParasite entity, double x, double y, double z, float entityYaw, float partialTicks) {
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
    protected ItemStack getHeldItemForBone(String boneName, EntityUltraParasite currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityUltraParasite currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityUltraParasite currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityUltraParasite currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityUltraParasite currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityUltraParasite currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityUltraParasite currentEntity) {
        return null;
    }
}
