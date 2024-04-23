package com.example.structure.entity.render;

import com.example.structure.entity.EntityChomper;
import com.example.structure.entity.model.ModelAvalon;
import com.example.structure.entity.render.geo.GeoGlowingLayer;
import com.example.structure.entity.render.geo.RenderGeoExtended;
import com.example.structure.entity.trader.EntityAvalon;
import com.example.structure.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import scala.reflect.internal.Mode;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class RenderAvalon extends RenderGeoExtended<EntityAvalon> {

    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/avalon/geo.avalon.json");

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/avalon.png");

    public RenderAvalon(RenderManager renderManager) {
        super(renderManager, new ModelAvalon(MODEL_RESLOC, TEXTURE, "avalon"));
        this.addLayer(new GeoGlowingLayer<EntityAvalon>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }


    @Override
    public void doRender(EntityAvalon entity, double x, double y, double z, float entityYaw, float partialTicks) {
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
    protected ItemStack getHeldItemForBone(String boneName, EntityAvalon currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityAvalon currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityAvalon currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityAvalon currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityAvalon currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityAvalon currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityAvalon currentEntity) {
        return null;
    }
}
