package com.example.structure.entity.render;

import com.example.structure.entity.EntityController;
import com.example.structure.entity.EntityEndBug;
import com.example.structure.entity.model.ModelController;
import com.example.structure.entity.render.geo.GeoGlowingLayer;
import com.example.structure.entity.render.geo.RenderGeoExtended;
import com.example.structure.init.ModItems;
import com.example.structure.util.ModReference;
import com.google.common.base.Optional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import javax.annotation.Nullable;

public class RenderController extends RenderGeoExtended<EntityController> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/controller.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/control/geo.controller.json");
    public RenderController(RenderManager renderManager) {
        super(renderManager, new ModelController(MODEL_RESLOC, TEXTURE, "controller"));

        this.addLayer(new GeoGlowingLayer<EntityController>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Override
    public void doRender(EntityController entity, double x, double y, double z, float entityYaw, float partialTicks) {
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
    protected ItemStack getHeldItemForBone(String boneName, EntityController currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityController currentEntity) {
        Optional<IBlockState> optional = currentEntity.getBlockFromHead(EntityController.CONTROLLER_HEAD.getFromBoneName(boneName));
        if(optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityController currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityController currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityController currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityController currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityController currentEntity) {
        return null;
    }


    private class LayerItem extends GeoLayerRenderer<EntityController> {

        public LayerItem(IGeoRenderer<EntityController> entityRendererIn) {
            super(entityRendererIn);
        }

        @Override
        public void render(EntityController entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, Color renderColor) {
            //Rendering of Item


        }





        @Override
        public boolean shouldCombineTextures() {
            return false;
        }
    }
}
