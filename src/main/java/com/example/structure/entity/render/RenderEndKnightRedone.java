package com.example.structure.entity.render;

import com.example.structure.entity.EntityEnderKnight;
import com.example.structure.entity.model.ModelEnderKnightRedone;
import com.example.structure.entity.model.ModelSnatcher;
import com.example.structure.entity.render.geo.GeoGlowingLayer;
import com.example.structure.entity.render.geo.RenderGeoExtended;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class RenderEndKnightRedone extends RenderGeoExtended<EntityEnderKnight> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/endknight_1.png");

    public static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/endknight/geo.endknight.json");


    public RenderEndKnightRedone(RenderManager renderManager) {
        super(renderManager, new ModelEnderKnightRedone(MODEL_RESLOC, TEXTURE, "end_knight"));

        this.addLayer(new GeoGlowingLayer<EntityEnderKnight>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }


    @Override
    public void doRender(EntityEnderKnight entity, double x, double y, double z, float entityYaw, float partialTicks) {
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
    protected ItemStack getHeldItemForBone(String boneName, EntityEnderKnight currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityEnderKnight currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityEnderKnight currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityEnderKnight currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityEnderKnight currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityEnderKnight currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityEnderKnight currentEntity) {
        return null;
    }
}
