package com.example.structure.entity.model;

import com.example.structure.entity.endking.EntityNuclearExplosion;
import com.example.structure.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelNuclearExplosion extends AnimatedGeoModel<EntityNuclearExplosion> {
    @Override
    public ResourceLocation getModelLocation(EntityNuclearExplosion entity) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/king/geo.nuke.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityNuclearExplosion entity) {
        if(entity.ticksExisted < 45) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/nuke.png");
        }
        if(entity.ticksExisted > 45 && entity.ticksExisted <= 55) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/nuke1.png");
        }
        if(entity.ticksExisted > 55 && entity.ticksExisted <= 65) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/nuke2.png");
        }
        if(entity.ticksExisted > 65 && entity.ticksExisted <= 75) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/nuke3.png");
        }
        if(entity.ticksExisted > 75 && entity.ticksExisted <= 85) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/nuke4.png");
        }
        if(entity.ticksExisted > 85) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/nuke5.png");
        }
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/nuke.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityNuclearExplosion entityNuclearExplosion) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.explosion.json");
    }
}
