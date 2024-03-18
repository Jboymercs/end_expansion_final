package com.example.structure.entity.model;

import com.example.structure.entity.knighthouse.EntityHealAura;
import com.example.structure.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelHealingAura extends AnimatedGeoModel<EntityHealAura> {
    @Override
    public ResourceLocation getModelLocation(EntityHealAura entityHealAura) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/endknight/geo.haura.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityHealAura entityHealAura) {
        if(entityHealAura.ticksExisted < 30) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/haura/haura_1.png");
        }
        if(entityHealAura.ticksExisted > 30 && entityHealAura.ticksExisted <= 40) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/haura/haura_2.png");
        }
        if(entityHealAura.ticksExisted > 40 && entityHealAura.ticksExisted <= 50) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/haura/haura_3.png");
        }
        if(entityHealAura.ticksExisted > 50) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/haura/haura_4.png");
        }
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/haura/haura_1.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityHealAura entityHealAura) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.haura.json");
    }
}
