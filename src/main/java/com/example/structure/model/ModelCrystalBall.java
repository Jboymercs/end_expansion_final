package com.example.structure.model;


import com.example.structure.items.CrystalBallItem;
import com.example.structure.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelCrystalBall extends AnimatedGeoModel<CrystalBallItem> {


    @Override
    public ResourceLocation getModelLocation(CrystalBallItem entityCrystalSpikeSmall) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.crystalball.json");
    }

    @Override
    public ResourceLocation getTextureLocation(CrystalBallItem entityCrystalSpikeSmall) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/item/crystalball.png");
    }

@Override
    public ResourceLocation getAnimationFileLocation(CrystalBallItem entityCrystalSpikeSmall) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.crystalball.json");
    }
}
