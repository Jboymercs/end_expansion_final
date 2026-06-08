package com.jboymercs.endexpansion.entity.ai;

import com.jboymercs.endexpansion.entity.Projectile;
import net.minecraft.entity.EntityLivingBase;

public interface IActionProjectile {
    void performAction(Projectile actor, EntityLivingBase target);

    IAction NONE = (actor, target) -> {
    };
}
