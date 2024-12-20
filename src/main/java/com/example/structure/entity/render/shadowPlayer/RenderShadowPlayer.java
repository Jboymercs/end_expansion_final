package com.example.structure.entity.render.shadowPlayer;

import com.example.structure.entity.model.shadowPlayer.ModelShadowPlayer;
import com.example.structure.entity.shadowPlayer.EntityShadowPlayer;
import com.example.structure.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderShadowPlayer extends RenderShadowExtended<EntityShadowPlayer> {

    public static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/shadow/geo.shadow_player.json");

    public static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/shadow_player.png");

    public RenderShadowPlayer(RenderManager renderManager) {
        super(renderManager, new ModelShadowPlayer(MODEL_RESLOC, TEXTURE, "shadow_player"));
    }

    @Override
    public void doRender(EntityShadowPlayer entity, double x, double y, double z, float entityYaw, float partialTicks) {
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
    protected ItemStack getHeldItemForBone(String boneName, EntityShadowPlayer currentEntity) {
        ItemStack stackInhand = currentEntity.getItemFromKnightHand(EntityShadowPlayer.SHADOW_HAND.getFromBoneName(boneName));
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
    protected IBlockState getHeldBlockForBone(String boneName, EntityShadowPlayer currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityShadowPlayer currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityShadowPlayer currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityShadowPlayer currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityShadowPlayer currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityShadowPlayer currentEntity) {
        return null;
    }
}
