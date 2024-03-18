package com.example.structure.model;

import com.example.structure.items.tools.ToolEndFallSword;
import com.example.structure.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelEndFallSword extends AnimatedGeoModel<ToolEndFallSword> {
    @Override
    public ResourceLocation getModelLocation(ToolEndFallSword object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.endfallsword.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ToolEndFallSword object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/item/endfallsword.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ToolEndFallSword animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.endfallsword.json");
    }
}
