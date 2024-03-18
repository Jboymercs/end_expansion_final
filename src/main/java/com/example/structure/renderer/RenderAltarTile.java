package com.example.structure.renderer;

import com.example.structure.entity.tileentity.TileEntityAltar;
import com.example.structure.model.ModelAltar;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class RenderAltarTile extends GeoBlockRenderer<TileEntityAltar> {



    public RenderAltarTile() {
        super(new ModelAltar());
    }

    public void render(TileEntityAltar te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        this.render(te, x, y, z, partialTicks, destroyStage);
        GlStateManager.translate(x, y, z);
        renderItem(te);
    }


    private void renderItem(TileEntityAltar altar) {
        if(!altar.getStackInSlot(0).isEmpty()) {
            renderItemInSlotZero(altar, altar.getStackInSlot(0), 0.25F, 1.05F, 0.5F, 0.4F);
        }
        if(!altar.getStackInSlot(1).isEmpty()) {

            renderItemInSlot(altar, altar.getStackInSlot(1), 0.5F, 1.3F, 0.5F, 0.4F);
        }
        if(!altar.getStackInSlot(2).isEmpty()) {
            renderItemInSlotTwo(altar, altar.getStackInSlot(2), 0.75F, 1.05F, 0.5F, 0.4F);
        }
        if(!altar.getStackInSlot(3).isEmpty()) {
            renderItemInSlotOutput(altar, altar.getStackInSlot(3), 0.5F, 1.3F, 0.5F, 0.4F);
        }

    }
    //static Item Left
    public void renderItemInSlotZero(TileEntityAltar altar, ItemStack stack, float x, float y, float z, float scale) {
        if(!stack.isEmpty()) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            GlStateManager.scale(scale, scale, scale);
            GlStateManager.rotate(20F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-90F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(0F, 0.0F, 0.0F, 1.0F);
            bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            ITextureObject tex = Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            tex.setBlurMipmap(false, false);
            //RenderHelper.disableStandardItemLighting();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            Minecraft.getMinecraft().getRenderItem().renderItem(stack, Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, (World) null, (EntityLivingBase) null));
            //GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
            bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            tex.restoreLastBlurMipmap();

        }
    }
    //Static Item Right
    public void renderItemInSlotTwo(TileEntityAltar altar, ItemStack stack, float x, float y, float z, float scale) {
        if(!stack.isEmpty()) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            GlStateManager.scale(scale, scale, scale);
            GlStateManager.rotate(-20F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-90F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(0F, 0.0F, 0.0F, 1.0F);
            bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            ITextureObject tex = Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            tex.setBlurMipmap(false, false);
            //RenderHelper.disableStandardItemLighting();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            Minecraft.getMinecraft().getRenderItem().renderItem(stack, Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, (World) null, (EntityLivingBase) null));
            //GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
            bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            tex.restoreLastBlurMipmap();
        }
    }
    //Infusion Core item
    public void renderItemInSlot(TileEntityAltar altar, ItemStack stack, float x, float y, float z, float scale) {
        if(!stack.isEmpty()) {
            double hoverHeight =  MathHelper.sin((float) ((2.0D) / 10.0F)) * 0.1F + 0.1F;
            GlStateManager.pushMatrix();
                GlStateManager.translate(x, y + hoverHeight, z);
            GlStateManager.scale(scale, scale, scale);
            float angle = (System.currentTimeMillis() / 40) % 360;
            GlStateManager.rotate(angle, 0, 1, 0);
            bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            ITextureObject tex = Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            tex.setBlurMipmap(false, false);
            //RenderHelper.disableStandardItemLighting();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            Minecraft.getMinecraft().getRenderItem().renderItem(stack, Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, (World) null, (EntityLivingBase) null));
            //GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
            bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            tex.restoreLastBlurMipmap();
        }
    }


    public void renderItemInSlotOutput(TileEntityAltar altar, ItemStack stack, float x, float y, float z, float scale) {
        if(!stack.isEmpty()) {
            double hoverHeight =  MathHelper.sin((float) ((2.0D) / 10.0F)) * 0.1F + 0.1F;
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y + hoverHeight, z);
            GlStateManager.scale(scale, scale, scale);
            float angle = (System.currentTimeMillis() / 40) % 360;
            GlStateManager.rotate(angle, 0, 1, 0);
            bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            ITextureObject tex = Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            tex.setBlurMipmap(false, false);
            //RenderHelper.disableStandardItemLighting();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            Minecraft.getMinecraft().getRenderItem().renderItem(stack, Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, (World) null, (EntityLivingBase) null));
            //GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
            bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            tex.restoreLastBlurMipmap();
        }
    }

}
