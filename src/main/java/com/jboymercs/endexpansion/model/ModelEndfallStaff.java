package com.jboymercs.endexpansion.model;

import com.jboymercs.endexpansion.items.ItemEndfallStaff;
import com.jboymercs.endexpansion.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelEndfallStaff extends AnimatedGeoModel<ItemEndfallStaff> {
    @Override
    public ResourceLocation getModelLocation(ItemEndfallStaff object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.efstaff.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemEndfallStaff object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/item/efstaff.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemEndfallStaff animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.efstaff.json");
    }
}
