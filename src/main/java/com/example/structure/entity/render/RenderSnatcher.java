package com.example.structure.entity.render;

import com.example.structure.entity.EntityController;
import com.example.structure.entity.EntitySnatcher;
import com.example.structure.entity.knighthouse.EntityEnderShield;
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
import scala.reflect.internal.Mode;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

public class RenderSnatcher extends RenderGeoExtended<EntitySnatcher> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/snatcher/snatcher.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/snatcher/geo.snatcher.json");
    public RenderSnatcher(RenderManager renderManager) {
        super(renderManager, new ModelSnatcher(MODEL_RESLOC, TEXTURE, "snatcher"));

        this.addLayer(new GeoGlowingLayer<EntitySnatcher>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Override
    public void doRender(EntitySnatcher entity, double x, double y, double z, float entityYaw, float partialTicks) {
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
    protected ItemStack getHeldItemForBone(String boneName, EntitySnatcher currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntitySnatcher currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntitySnatcher currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntitySnatcher currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntitySnatcher currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntitySnatcher currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntitySnatcher currentEntity) {
        return null;
    }
}
