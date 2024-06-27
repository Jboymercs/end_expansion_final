package com.example.structure.model;

import com.example.structure.items.gecko.AmberArmorSet;
import com.example.structure.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelAmberArmorSet extends AnimatedGeoModel<AmberArmorSet> {

    public ModelAmberArmorSet() {

    }


    @Override
    public ResourceLocation getModelLocation(AmberArmorSet object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/armor/geo.amber_armor.json");
    }

    @Override
    public ResourceLocation getTextureLocation(AmberArmorSet object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/models/geo/amber_armor.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(AmberArmorSet animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "");
    }
}
