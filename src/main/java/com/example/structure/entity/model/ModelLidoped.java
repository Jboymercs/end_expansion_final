package com.example.structure.entity.model;

import com.example.structure.entity.barrend.EntityLidoped;
import com.example.structure.entity.knighthouse.EntityEnderMage;
import com.example.structure.entity.model.geo.GeoModelExtended;
import com.example.structure.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelLidoped extends GeoModelExtended<EntityLidoped> {

    public ModelLidoped(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityLidoped entityEnderMage) {
        if(entityEnderMage.getSkin() == 1) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/lidoped/lidoped.png");
        }
        else if(entityEnderMage.getSkin() == 2) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/lidoped/lidoped_2.png");
        } else if(entityEnderMage.getSkin() == 3) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/lidoped/lidoped_3.png");
        } else if(entityEnderMage.getSkin() == 4) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/lidoped/lidoped_4.png");
        }
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/lidoped/lidoped.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityLidoped animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.lidoped.json");
    }

    @Override
    public void setLivingAnimations(EntityLidoped entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("Head");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));

    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}
