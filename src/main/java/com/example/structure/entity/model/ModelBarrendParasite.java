package com.example.structure.entity.model;

import com.example.structure.entity.EntityBarrendParasite;
import com.example.structure.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelBarrendParasite extends AnimatedGeoModel<EntityBarrendParasite> {
    @Override
    public ResourceLocation getModelLocation(EntityBarrendParasite object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/barrend/geo.parasite.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityBarrendParasite object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/parasite.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityBarrendParasite animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.parasite.json");
    }
}
