package com.example.structure.entity.lamentorUtil;

import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.ai.IAction;
import com.example.structure.entity.trader.EntityAOEArena;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class ActionWaveAOE implements IAction {

    public BlockPos arena_center;

    public ActionWaveAOE(BlockPos center) {
        this.arena_center = center;
    }
    @Override
    public void performAction(EntityModBase actor, EntityLivingBase target) {
        Vec3d centerPosM = new Vec3d(arena_center.getX(), arena_center.getY(), arena_center.getZ());

        ModUtils.circleCallback(0, 1, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(centerPosM);
            EntityLamentorWave spike = new EntityLamentorWave(actor.world);
            spike.setPosition(pos.x, pos.y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });

        ModUtils.circleCallback(1, 8, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(centerPosM);
            EntityLamentorWave spike = new EntityLamentorWave(actor.world);
            spike.setPosition(pos.x, pos.y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });

        ModUtils.circleCallback(2, 16, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(centerPosM);
            EntityLamentorWave spike = new EntityLamentorWave(actor.world);
            spike.setPosition(pos.x, pos.y, pos.z);
            actor.world.spawnEntity(spike);
        });

        ModUtils.circleCallback(3, 24, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(centerPosM);
            EntityLamentorWave spike = new EntityLamentorWave(actor.world);
            spike.setPosition(pos.x, pos.y, pos.z);
            actor.world.spawnEntity(spike);
        });

        ModUtils.circleCallback(4, 32, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(centerPosM);
            EntityLamentorWave spike = new EntityLamentorWave(actor.world);
            spike.setPosition(pos.x, pos.y, pos.z);
            actor.world.spawnEntity(spike);
        });

        ModUtils.circleCallback(5, 40, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(centerPosM);
            EntityLamentorWave spike = new EntityLamentorWave(actor.world);
            spike.setPosition(pos.x, pos.y, pos.z);
            actor.world.spawnEntity(spike);
        });

        ModUtils.circleCallback(6, 48, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(centerPosM);
            EntityLamentorWave spike = new EntityLamentorWave(actor.world);
            spike.setPosition(pos.x, pos.y, pos.z);
            actor.world.spawnEntity(spike);
        });

        ModUtils.circleCallback(7, 56, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(centerPosM);
            EntityLamentorWave spike = new EntityLamentorWave(actor.world);
            spike.setPosition(pos.x, pos.y, pos.z);
            actor.world.spawnEntity(spike);
        });


        ModUtils.circleCallback(8, 64, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(centerPosM);
            EntityLamentorWave spike = new EntityLamentorWave(actor.world);
            spike.setPosition(pos.x, pos.y, pos.z);
            actor.world.spawnEntity(spike);
        });

        ModUtils.circleCallback(9, 72, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(centerPosM);
            EntityLamentorWave spike = new EntityLamentorWave(actor.world);
            spike.setPosition(pos.x, pos.y, pos.z);
            actor.world.spawnEntity(spike);
        });

        ModUtils.circleCallback(10, 80, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(centerPosM);
            EntityLamentorWave spike = new EntityLamentorWave(actor.world);
            spike.setPosition(pos.x, pos.y, pos.z);
            actor.world.spawnEntity(spike);
        });

        ModUtils.circleCallback(11, 88, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(centerPosM);
            EntityLamentorWave spike = new EntityLamentorWave(actor.world);
            spike.setPosition(pos.x, pos.y, pos.z);
            actor.world.spawnEntity(spike);
        });

        ModUtils.circleCallback(12, 96, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(centerPosM);
            EntityLamentorWave spike = new EntityLamentorWave(actor.world);
            spike.setPosition(pos.x, pos.y, pos.z);
            actor.world.spawnEntity(spike);
        });
    }
}
