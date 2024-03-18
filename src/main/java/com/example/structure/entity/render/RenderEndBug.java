package com.example.structure.entity.render;

import com.example.structure.Main;
import com.example.structure.entity.EntityCrystalKnight;
import com.example.structure.entity.EntityEndBug;
import com.example.structure.entity.model.ModelEndBug;
import com.example.structure.entity.render.geo.GeoGlowingLayer;
import com.example.structure.entity.render.geo.RenderGeoExtended;
import com.example.structure.entity.render.geo.RenderTamaebleGeoExtend;
import com.example.structure.entity.util.LayerGenericGlow;
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
import java.util.function.Function;

public class RenderEndBug extends RenderTamaebleGeoExtend<EntityEndBug> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/bug.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/endbug/geo.bug.json");

    public RenderEndBug(RenderManager renderManager) {
        super(renderManager, new ModelEndBug(MODEL_RESLOC, TEXTURE, "bug"));

        this.addLayer(new GeoGlowingLayer<EntityEndBug>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }


    @Override
    public void doRender(EntityEndBug entity, double x, double y, double z, float entityYaw, float partialTicks) {
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
    protected ItemStack getHeldItemForBone(String boneName, EntityEndBug currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityEndBug currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityEndBug currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityEndBug currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityEndBug currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityEndBug currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityEndBug currentEntity) {
        return null;
    }


}
