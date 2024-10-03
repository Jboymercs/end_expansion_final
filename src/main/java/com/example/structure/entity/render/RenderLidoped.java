package com.example.structure.entity.render;

import com.example.structure.entity.EntityChomper;
import com.example.structure.entity.barrend.EntityLidoped;
import com.example.structure.entity.model.ModelLidoped;
import com.example.structure.entity.render.geo.GeoGlowingLayer;
import com.example.structure.entity.render.geo.RenderGeoExendedLidoped;
import com.example.structure.entity.render.geo.RenderGeoExtended;
import com.example.structure.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class RenderLidoped extends RenderGeoExendedLidoped<EntityLidoped> {

    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/lidoped/geo.lidoped.json");

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/lidoped/lidoped.png");

    public RenderLidoped(RenderManager renderManager) {
        super(renderManager, new ModelLidoped(MODEL_RESLOC, TEXTURE, "lidoped"));
        this.addLayer(new GeoGlowingLayer<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Override
    public void doRender(EntityLidoped entity, double x, double y, double z, float entityYaw, float partialTicks) {
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
    protected ItemStack getHeldItemForBone(String boneName, EntityLidoped currentEntity) {
        ItemStack stackInhand = currentEntity.getItemFromBugBack(EntityLidoped.LIDOPED_BACK.getFromBoneName(boneName));
        if(stackInhand != null) {
            return stackInhand;
        }
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityLidoped currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityLidoped currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityLidoped currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityLidoped currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityLidoped currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityLidoped currentEntity) {
        return null;
    }

    @Override
    public void renderCube(BufferBuilder builder, GeoCube cube, float red, float green, float blue, float alpha) {
        super.renderCube(builder, cube, red, green, blue, alpha);
    }

    @Override
    public void renderAfter(EntityLidoped animatable, float ticks, float red, float green, float blue, float partialTicks) {
        super.renderAfter(animatable, ticks, red, green, blue, partialTicks);
    }

    @Override
    public Color getRenderColor(EntityLidoped animatable, float partialTicks) {
        return super.getRenderColor(animatable, partialTicks);
    }
}
