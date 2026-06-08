package com.jboymercs.endexpansion.entity.trader.action.player;

import com.jboymercs.endexpansion.entity.trader.EntityAOEArena;
import com.jboymercs.endexpansion.util.ModUtils;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class ActionWaveFromPlayer implements IActionPlayer {

    protected int timeSustainedInAir;

    public ActionWaveFromPlayer(int timeSustainedInAir) {
        this.timeSustainedInAir = timeSustainedInAir;

    }
    @Override
    public void performAction(Entity actor, Entity target) {
        ModUtils.circleCallback(1, 8, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            EntityAOEArena spike = new EntityAOEArena(actor.world, target);
            spike.setPosition(pos.x, pos.y, pos.z);
            actor.world.spawnEntity(spike);
        });
        int expandedLayers = timeSustainedInAir / 5;

        for(int y = 2; y <= expandedLayers; y++) {
            //Too a max of 10 AOE circles to prevent things going crazy
            if(y <= 10) {
                ModUtils.circleCallback(y, (y * 8), (pos) -> {
                    pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
                    EntityAOEArena spike = new EntityAOEArena(actor.world, target);
                    spike.setPosition(pos.x, pos.y, pos.z);
                    actor.world.spawnEntity(spike);
                });
            }
        }

    }
}
