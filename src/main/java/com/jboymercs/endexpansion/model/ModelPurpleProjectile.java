package com.jboymercs.endexpansion.model;

import com.jboymercs.endexpansion.items.CrystalBallItem;
import com.jboymercs.endexpansion.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelPurpleProjectile extends AnimatedGeoModel<CrystalBallItem> {
    @Override
    public ResourceLocation getModelLocation(CrystalBallItem object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.crystalball.json");
    }

    @Override
    public ResourceLocation getTextureLocation(CrystalBallItem object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/item/projpurp.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(CrystalBallItem animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.crystalball.json");
    }
}
