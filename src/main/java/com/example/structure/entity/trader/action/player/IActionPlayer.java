package com.example.structure.entity.trader.action.player;


import com.example.structure.entity.ai.IAction;
import net.minecraft.entity.Entity;


public interface IActionPlayer {
    void performAction(Entity actor, Entity target);

    IAction NONE = (actor, target) -> {
    };
}
