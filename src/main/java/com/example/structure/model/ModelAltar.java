package com.example.structure.model;

import com.example.structure.entity.tileentity.TileEntityAltar;
import com.example.structure.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelAltar extends AnimatedGeoModel<TileEntityAltar> {
    @Override
    public ResourceLocation getModelLocation(TileEntityAltar tileEntityAltar) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/block/geo.altar.json");
    }

    @Override
    public ResourceLocation getTextureLocation(TileEntityAltar tileEntityAltar) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/blocks/altar.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(TileEntityAltar tileEntityAltar) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.altar.json");
    }
}
