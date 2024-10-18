package com.example.structure.entity.ai;

import com.example.structure.entity.Projectile;
import net.minecraft.entity.EntityLivingBase;

public interface IActionProjectile {
    void performAction(Projectile actor, EntityLivingBase target);

    IAction NONE = (actor, target) -> {
    };
}
