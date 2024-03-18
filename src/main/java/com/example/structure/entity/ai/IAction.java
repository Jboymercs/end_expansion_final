package com.example.structure.entity.ai;

import com.example.structure.entity.EntityModBase;
import net.minecraft.entity.EntityLivingBase;

public interface IAction {
    void performAction(EntityModBase actor, EntityLivingBase target);

    IAction NONE = (actor, target) -> {
    };
}
