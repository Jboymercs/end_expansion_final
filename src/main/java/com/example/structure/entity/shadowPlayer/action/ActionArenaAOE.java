package com.example.structure.entity.shadowPlayer.action;

import com.example.structure.entity.barrend.ultraparasite.EntityParasiteBombAOE;
import com.example.structure.entity.shadowPlayer.EntityShadowPlayer;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ActionArenaAOE implements IActionShadow{
    @Override
    public void performAction(EntityShadowPlayer actor, EntityLivingBase target) {
        ModUtils.circleCallback(2, 16, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 5, (int) actor.posY + 2);
            EntityParasiteBombAOE spike = new EntityParasiteBombAOE(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(3, 20, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 5, (int) actor.posY + 2);
            EntityParasiteBombAOE spike = new EntityParasiteBombAOE(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(4, 24, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 5, (int) actor.posY + 2);
            EntityParasiteBombAOE spike = new EntityParasiteBombAOE(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(5, 28, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 5, (int) actor.posY + 2);
            EntityParasiteBombAOE spike = new EntityParasiteBombAOE(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(6, 32, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 5, (int) actor.posY + 2);
            EntityParasiteBombAOE spike = new EntityParasiteBombAOE(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(7, 36, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 5, (int) actor.posY + 2);
            EntityParasiteBombAOE spike = new EntityParasiteBombAOE(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(8, 40, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 5, (int) actor.posY + 2);
            EntityParasiteBombAOE spike = new EntityParasiteBombAOE(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(9, 48, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 5, (int) actor.posY + 2);
            EntityParasiteBombAOE spike = new EntityParasiteBombAOE(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });

        ModUtils.circleCallback(10, 52, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 5, (int) actor.posY + 2);
            EntityParasiteBombAOE spike = new EntityParasiteBombAOE(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(11, 58, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 5, (int) actor.posY + 2);
            EntityParasiteBombAOE spike = new EntityParasiteBombAOE(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(12, 66, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 5, (int) actor.posY + 2);
            EntityParasiteBombAOE spike = new EntityParasiteBombAOE(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(13, 72, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 5, (int) actor.posY + 2);
            EntityParasiteBombAOE spike = new EntityParasiteBombAOE(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });

    }

    public int getSurfaceHeight(World world, BlockPos pos, int min, int max)
    {
        int currentY = max;

        while(currentY >= min)
        {
            if(!world.isAirBlock(pos.add(0, currentY, 0)) && !world.isRemote && world.getBlockState(pos.add(0, currentY, 0)).isFullBlock()) {
                return currentY;
            }

            currentY--;
        }

        return 0;
    }
}
