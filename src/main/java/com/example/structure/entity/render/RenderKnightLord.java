package com.example.structure.entity.render;

import com.example.structure.entity.knighthouse.EntityEnderMage;
import com.example.structure.entity.knighthouse.EntityEnderShield;
import com.example.structure.entity.knighthouse.EntityKnightLord;
import com.example.structure.entity.model.ModelKnightLord;
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
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

public class RenderKnightLord extends RenderGeoExtended<EntityKnightLord> {

    public static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/endlord/geo.endlord.json");

    public static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/endlord.png");
    public RenderKnightLord(RenderManager renderManager) {
        super(renderManager, new ModelKnightLord(MODEL_RESLOC, TEXTURE, "end_lord"));

        this.addLayer(new GeoGlowingLayer<EntityKnightLord>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Override
    public void doRender(EntityKnightLord entity, double x, double y, double z, float entityYaw, float partialTicks) {
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
    protected ItemStack getHeldItemForBone(String boneName, EntityKnightLord currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityKnightLord currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityKnightLord currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityKnightLord currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityKnightLord currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityKnightLord currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityKnightLord currentEntity) {
        return null;
    }
}
