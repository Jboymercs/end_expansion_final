package com.example.structure.entity.render;

import com.example.structure.entity.barrend.EntityMadSpirit;
import com.example.structure.entity.model.ModelMadSpirit;
import com.example.structure.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderMadSpirit extends RenderAbstractGeoEntity<EntityMadSpirit> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/madspirit/madspirit.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/madspirit/geo.madspirit.json");
    public RenderMadSpirit(RenderManager renderManager) {
        super(renderManager, new ModelMadSpirit(MODEL_RESLOC, TEXTURE, "mad_spirit"));
        //this.addLayer(new GeoGlowingLayer<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
        //this.addLayer(new RenderSpiritLayer(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
        this.shadowSize = 0.8f;
    }



    @Override
    public void doRender(EntityMadSpirit entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }

}
