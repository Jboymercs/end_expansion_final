package com.example.structure.entity.model;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityCrystalKnight;
import com.example.structure.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelCrystalBoss extends AnimatedGeoModel<EntityCrystalKnight> {
    @Override
    public ResourceLocation getModelLocation(EntityCrystalKnight entityCrystalKnight) {
        if(ModConfig.lamenter_legacy_texture) {
            return new ResourceLocation(ModReference.MOD_ID, "geo/entity/crystalknight/geo.lamentor.json");
        }
        else {
            return new ResourceLocation(ModReference.MOD_ID, "geo/entity/crystalknight/geo.lamentoralt.json");
        }
    }

    @Override
    public ResourceLocation getTextureLocation(EntityCrystalKnight entityCrystalKnight) {
        if(ModConfig.lamenter_legacy_texture) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/entitylamentor.png");
        }
         else {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/entitylamentoralt.png");
        }
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityCrystalKnight entityCrystalKnight) {
        if(ModConfig.lamenter_legacy_texture) {
            return new ResourceLocation(ModReference.MOD_ID, "animations/animation.lamentor.json");
        }
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.lamentoralt.json");
    }

    @Override
    public void setLivingAnimations(EntityCrystalKnight entity, Integer uniqueID) {
        super.setLivingAnimations(entity, uniqueID);
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}
