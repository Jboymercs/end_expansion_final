package com.example.structure.entity.model;

import com.example.structure.entity.barrend.EntityUltraParasite;
import com.example.structure.entity.barrend.ultraparasite.EntityParasiteBombAOE;
import com.example.structure.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelParasiteAOE extends AnimatedGeoModel<EntityParasiteBombAOE> {
    @Override
    public ResourceLocation getModelLocation(EntityParasiteBombAOE emtityGenericWave) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/wave/wave.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityParasiteBombAOE emtityGenericWave) {
        if(emtityGenericWave.ticksExisted > 1 && emtityGenericWave.ticksExisted < 4) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/bomb_aoe/wave1.png");
        }
        if(emtityGenericWave.ticksExisted > 4 && emtityGenericWave.ticksExisted < 8) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/bomb_aoe/wave2.png");
        }
        if(emtityGenericWave.ticksExisted > 8 && emtityGenericWave.ticksExisted < 12) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/bomb_aoe/wave3.png");
        }

        if(emtityGenericWave.ticksExisted > 12 && emtityGenericWave.ticksExisted < 16) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/bomb_aoe/wave4.png");
        }

        if(emtityGenericWave.ticksExisted > 16 && emtityGenericWave.ticksExisted < 20) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/bomb_aoe/wave5.png");
        }
        if(emtityGenericWave.ticksExisted > 20 && emtityGenericWave.ticksExisted < 25) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/bomb_aoe/wave2.png");
        }
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/bomb_aoe/wave.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityParasiteBombAOE emtityGenericWave) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.wave.json");
    }
}
