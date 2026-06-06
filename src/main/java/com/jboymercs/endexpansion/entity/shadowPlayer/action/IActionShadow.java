package com.jboymercs.endexpansion.entity.shadowPlayer.action;


import com.jboymercs.endexpansion.entity.ai.IAction;
import com.jboymercs.endexpansion.entity.shadowPlayer.EntityShadowPlayer;
import net.minecraft.entity.EntityLivingBase;

public interface IActionShadow {
    void performAction(EntityShadowPlayer actor, EntityLivingBase target);

    IAction NONE = (actor, target) -> {
    };
}
