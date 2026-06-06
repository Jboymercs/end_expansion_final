package com.jboymercs.endexpansion.model;

import com.jboymercs.endexpansion.items.ItemProjectileBomb;
import com.jboymercs.endexpansion.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelParasiteBomb extends AnimatedGeoModel<ItemProjectileBomb> {
    @Override
    public ResourceLocation getModelLocation(ItemProjectileBomb object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.parasitebomb.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemProjectileBomb object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/item/parasite_bomb.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemProjectileBomb animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.parasitebomb.json");
    }
}
