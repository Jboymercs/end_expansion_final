package com.example.structure.entity.trader.action;

import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.ai.IAction;
import com.example.structure.entity.trader.EntityAOEArena;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class ActionMediumRangeAOE implements IAction {


    @Override
    public void performAction(EntityModBase actor, EntityLivingBase target) {
        ModUtils.circleCallback(5 ,40, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            EntityAOEArena spike = new EntityAOEArena(actor.world);
            spike.setPosition(pos.x, pos.y -2, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(6 ,48, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            EntityAOEArena spike = new EntityAOEArena(actor.world);
            spike.setPosition(pos.x, pos.y -2, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(7 ,56, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            EntityAOEArena spike = new EntityAOEArena(actor.world);
            spike.setPosition(pos.x, pos.y -2, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(8 ,64, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            EntityAOEArena spike = new EntityAOEArena(actor.world);
            spike.setPosition(pos.x, pos.y -2, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(9 ,72, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            EntityAOEArena spike = new EntityAOEArena(actor.world);
            spike.setPosition(pos.x, pos.y -2, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(10 ,80, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            EntityAOEArena spike = new EntityAOEArena(actor.world);
            spike.setPosition(pos.x, pos.y -2, pos.z);
            actor.world.spawnEntity(spike);
        });
    }
}
