package com.example.structure.entity.shadowPlayer.action;


import com.example.structure.entity.ai.IAction;
import com.example.structure.entity.shadowPlayer.EntityShadowPlayer;
import net.minecraft.entity.EntityLivingBase;

public interface IActionShadow {
    void performAction(EntityShadowPlayer actor, EntityLivingBase target);

    IAction NONE = (actor, target) -> {
    };
}
