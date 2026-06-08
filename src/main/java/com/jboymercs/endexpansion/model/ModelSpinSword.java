package com.jboymercs.endexpansion.model;

import com.jboymercs.endexpansion.items.SpinSwordItem;
import com.jboymercs.endexpansion.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelSpinSword extends AnimatedGeoModel<SpinSwordItem> {
    @Override
    public ResourceLocation getModelLocation(SpinSwordItem spinSwordItem) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.spinsword.json");
    }

    @Override
    public ResourceLocation getTextureLocation(SpinSwordItem spinSwordItem) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/item/spinsword.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(SpinSwordItem spinSwordItem) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.spinsword.json");
    }
}
