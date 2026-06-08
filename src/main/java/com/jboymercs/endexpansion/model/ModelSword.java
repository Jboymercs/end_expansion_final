package com.jboymercs.endexpansion.model;

import com.jboymercs.endexpansion.config.MobConfig;
import com.jboymercs.endexpansion.items.tools.ToolBossSword;
import com.jboymercs.endexpansion.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelSword extends AnimatedGeoModel<ToolBossSword> {
    @Override
    public ResourceLocation getModelLocation(ToolBossSword toolBossSword) {
        if(MobConfig.lamenter_legacy_texture) {
            return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.sword.json");
        }
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.swordalt.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ToolBossSword toolBossSword) {
        if(MobConfig.lamenter_legacy_texture) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/item/sword.png");
        }
        return new ResourceLocation(ModReference.MOD_ID, "textures/item/swordalt.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ToolBossSword toolBossSword) {
        if(MobConfig.lamenter_legacy_texture) {
            return new ResourceLocation(ModReference.MOD_ID, "animations/animation.sword.json");
        }
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.swordalt.json");
    }
}
