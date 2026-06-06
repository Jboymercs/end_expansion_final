package com.jboymercs.endexpansion.entity.trader.action;

import com.jboymercs.endexpansion.entity.EntityModBase;
import com.jboymercs.endexpansion.entity.ai.IAction;
import com.jboymercs.endexpansion.entity.trader.EntityAOEArena;
import com.jboymercs.endexpansion.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class ActionFarRange implements IAction {
    @Override
    public void performAction(EntityModBase actor, EntityLivingBase target) {
        ModUtils.circleCallback(11 ,88, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            EntityAOEArena spike = new EntityAOEArena(actor.world);
            spike.setPosition(pos.x, pos.y -2, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(12 ,96, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            EntityAOEArena spike = new EntityAOEArena(actor.world);
            spike.setPosition(pos.x, pos.y -2, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(13 ,104, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            EntityAOEArena spike = new EntityAOEArena(actor.world);
            spike.setPosition(pos.x, pos.y -2, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(14 ,112, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            EntityAOEArena spike = new EntityAOEArena(actor.world);
            spike.setPosition(pos.x, pos.y -2, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(15 ,120, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            EntityAOEArena spike = new EntityAOEArena(actor.world);
            spike.setPosition(pos.x, pos.y -2, pos.z);
            actor.world.spawnEntity(spike);
        });
    }
}
