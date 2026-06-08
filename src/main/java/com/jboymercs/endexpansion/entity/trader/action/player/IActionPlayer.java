package com.jboymercs.endexpansion.entity.trader.action.player;


import com.jboymercs.endexpansion.entity.ai.IAction;
import net.minecraft.entity.Entity;


public interface IActionPlayer {
    void performAction(Entity actor, Entity target);

    IAction NONE = (actor, target) -> {
    };
}
