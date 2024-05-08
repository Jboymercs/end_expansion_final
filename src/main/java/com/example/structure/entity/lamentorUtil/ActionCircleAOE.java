package com.example.structure.entity.lamentorUtil;

import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.ai.IAction;
import com.example.structure.entity.trader.EntityAOEArena;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class ActionCircleAOE implements IAction {
    @Override
    public void performAction(EntityModBase actor, EntityLivingBase target) {
        Vec3d targetPos = target.getPositionVector();
        BlockPos targetBlock = target.getPosition();
        if(actor.world.isAirBlock(targetBlock.add(0, -1, 0))) {
            ModUtils.circleCallback(0, 1, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                EntityLamentorWave spike = new EntityLamentorWave(actor.world);
                spike.setPosition(pos.x, pos.y -1, pos.z);
                actor.world.spawnEntity(spike);
            });

            ModUtils.circleCallback(1, 8, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                EntityLamentorWave spike = new EntityLamentorWave(actor.world);
                spike.setPosition(pos.x, pos.y -1, pos.z);
                actor.world.spawnEntity(spike);
            });

            ModUtils.circleCallback(2, 16, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                EntityLamentorWave spike = new EntityLamentorWave(actor.world);
                spike.setPosition(pos.x, pos.y -1, pos.z);
                actor.world.spawnEntity(spike);
            });

            ModUtils.circleCallback(3, 24, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                EntityLamentorWave spike = new EntityLamentorWave(actor.world);
                spike.setPosition(pos.x, pos.y -1, pos.z);
                actor.world.spawnEntity(spike);
            });

            ModUtils.circleCallback(4, 32, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                EntityLamentorWave spike = new EntityLamentorWave(actor.world);
                spike.setPosition(pos.x, pos.y -1, pos.z);
                actor.world.spawnEntity(spike);
            });

            ModUtils.circleCallback(5, 40, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                EntityLamentorWave spike = new EntityLamentorWave(actor.world);
                spike.setPosition(pos.x, pos.y -1, pos.z);
                actor.world.spawnEntity(spike);
            });

            ModUtils.circleCallback(6, 48, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                EntityLamentorWave spike = new EntityLamentorWave(actor.world);
                spike.setPosition(pos.x, pos.y -1, pos.z);
                actor.world.spawnEntity(spike);
            });

            ModUtils.circleCallback(7, 56, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                EntityLamentorWave spike = new EntityLamentorWave(actor.world);
                spike.setPosition(pos.x, pos.y -1, pos.z);
                actor.world.spawnEntity(spike);
            });

            ModUtils.circleCallback(8, 64, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                EntityLamentorWave spike = new EntityLamentorWave(actor.world);
                spike.setPosition(pos.x, pos.y, pos.z);
                actor.world.spawnEntity(spike);
            });

        } else {
            ModUtils.circleCallback(0, 1, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                EntityLamentorWave spike = new EntityLamentorWave(actor.world);
                spike.setPosition(pos.x, pos.y, pos.z);
                actor.world.spawnEntity(spike);
            });

            ModUtils.circleCallback(1, 8, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                EntityLamentorWave spike = new EntityLamentorWave(actor.world);
                spike.setPosition(pos.x, pos.y, pos.z);
                actor.world.spawnEntity(spike);
            });

            ModUtils.circleCallback(2, 16, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                EntityLamentorWave spike = new EntityLamentorWave(actor.world);
                spike.setPosition(pos.x, pos.y, pos.z);
                actor.world.spawnEntity(spike);
            });

            ModUtils.circleCallback(3, 24, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                EntityLamentorWave spike = new EntityLamentorWave(actor.world);
                spike.setPosition(pos.x, pos.y, pos.z);
                actor.world.spawnEntity(spike);
            });

            ModUtils.circleCallback(4, 32, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                EntityLamentorWave spike = new EntityLamentorWave(actor.world);
                spike.setPosition(pos.x, pos.y, pos.z);
                actor.world.spawnEntity(spike);
            });

            ModUtils.circleCallback(5, 40, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                EntityLamentorWave spike = new EntityLamentorWave(actor.world);
                spike.setPosition(pos.x, pos.y, pos.z);
                actor.world.spawnEntity(spike);
            });

            ModUtils.circleCallback(6, 48, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                EntityLamentorWave spike = new EntityLamentorWave(actor.world);
                spike.setPosition(pos.x, pos.y, pos.z);
                actor.world.spawnEntity(spike);
            });

            ModUtils.circleCallback(7, 56, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                EntityLamentorWave spike = new EntityLamentorWave(actor.world);
                spike.setPosition(pos.x, pos.y, pos.z);
                actor.world.spawnEntity(spike);
            });

            ModUtils.circleCallback(8, 64, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                EntityLamentorWave spike = new EntityLamentorWave(actor.world);
                spike.setPosition(pos.x, pos.y, pos.z);
                actor.world.spawnEntity(spike);
            });
        }
    }
}
