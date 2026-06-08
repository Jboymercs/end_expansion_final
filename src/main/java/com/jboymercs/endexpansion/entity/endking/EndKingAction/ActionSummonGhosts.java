package com.jboymercs.endexpansion.entity.endking.EndKingAction;

import com.jboymercs.endexpansion.entity.EntityModBase;
import com.jboymercs.endexpansion.entity.ai.IAction;
import com.jboymercs.endexpansion.entity.endking.ghosts.EntityGhostPhase;
import com.jboymercs.endexpansion.util.ModRand;
import com.jboymercs.endexpansion.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class ActionSummonGhosts implements IAction {

    protected int uniqueIDtoPerform = ModRand.range(1, 3);

    @Override
    public void performAction(EntityModBase actor, EntityLivingBase target) {


        //Far Away Dash Ghost
    if(uniqueIDtoPerform == 1) {

        ModUtils.circleCallback(9, 1, (pos) -> {
            pos = new Vec3d(pos.x, 0, pos.y).add(target.getPositionVector());
            EntityGhostPhase spike = new EntityGhostPhase(actor.world, 1);
            spike.setPosition(pos.x, pos.y, pos.z);
            actor.world.spawnEntity(spike);
        });
        // Close Attack
    } else if(uniqueIDtoPerform == 2) {

            ModUtils.circleCallback(3, 1, (pos) -> {
                pos = new Vec3d(pos.x, 0, pos.y).add(target.getPositionVector());
                EntityGhostPhase spike = new EntityGhostPhase(actor.world, 2);
                spike.setPosition(pos.x, pos.y, pos.z);
                actor.world.spawnEntity(spike);
            });

        }
    }
}
