package com.jboymercs.endexpansion.entity.ai;

import com.jboymercs.endexpansion.entity.EntityModBase;
import net.minecraft.entity.EntityLivingBase;

public interface IAction {
    void performAction(EntityModBase actor, EntityLivingBase target);

    IAction NONE = (actor, target) -> {
    };
}
