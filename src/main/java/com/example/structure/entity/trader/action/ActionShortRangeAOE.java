package com.example.structure.entity.trader.action;

import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.ai.IAction;
import com.example.structure.entity.endking.EntityRedCrystal;
import com.example.structure.entity.trader.EntityAOEArena;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class ActionShortRangeAOE implements IAction {
    @Override
    public void performAction(EntityModBase actor, EntityLivingBase target) {
        ModUtils.circleCallback(0, 1, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            EntityAOEArena spike = new EntityAOEArena(actor.world);
            spike.setPosition(pos.x, pos.y -2, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(1, 8, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            EntityAOEArena spike = new EntityAOEArena(actor.world);
            spike.setPosition(pos.x, pos.y -2, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(2 ,16, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            EntityAOEArena spike = new EntityAOEArena(actor.world);
            spike.setPosition(pos.x, pos.y -2, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(3 ,24, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            EntityAOEArena spike = new EntityAOEArena(actor.world);
            spike.setPosition(pos.x, pos.y -2, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(4 ,32, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            EntityAOEArena spike = new EntityAOEArena(actor.world);
            spike.setPosition(pos.x, pos.y -2, pos.z);
            actor.world.spawnEntity(spike);
        });
    }
}
