package com.example.structure.entity.endking.EndKingAction;

import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.ai.IAction;
import com.example.structure.entity.endking.ghosts.EntityGhostPhase;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

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
