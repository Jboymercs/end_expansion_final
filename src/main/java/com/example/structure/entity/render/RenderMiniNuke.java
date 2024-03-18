package com.example.structure.entity.render;

import com.example.structure.entity.EntityMiniNuke;
import com.example.structure.entity.model.ModelMiniNuke;
import com.example.structure.init.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderMiniNuke extends GeoEntityRenderer<EntityMiniNuke> {
    public RenderMiniNuke(RenderManager renderManager) {
        super(renderManager, new ModelMiniNuke());
    }


}
