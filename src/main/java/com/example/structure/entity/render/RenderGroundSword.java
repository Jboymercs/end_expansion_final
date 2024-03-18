package com.example.structure.entity.render;

import com.example.structure.entity.EntitySnatcher;
import com.example.structure.entity.endking.EntityGroundSword;
import com.example.structure.entity.model.ModelGroundSword;
import com.example.structure.entity.model.ModelSnatcher;
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

public class RenderGroundSword extends RenderGeoExtended<EntityGroundSword> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/swordattack.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/util/geo.sword.json");

    public RenderGroundSword(RenderManager renderManager) {
        super(renderManager, new ModelGroundSword(MODEL_RESLOC, TEXTURE, "sword_attack"));
        this.addLayer(new GeoGlowingLayer<EntityGroundSword>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
        this.shadowSize = 0.0f;
    }


    @Override
    public void doRender(EntityGroundSword entity, double x, double y, double z, float entityYaw, float partialTicks) {
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
    protected ItemStack getHeldItemForBone(String boneName, EntityGroundSword currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityGroundSword currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityGroundSword currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityGroundSword currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityGroundSword currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityGroundSword currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityGroundSword currentEntity) {
        return null;
    }


}
