package com.example.structure.entity.trader.action.player;

import com.example.structure.entity.trader.EntityAOEArena;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ActionShortAOE implements IActionPlayer{


    @Override
    public void performAction(Entity actor, Entity target) {
        Vec3d pos = actor.getPositionVector();
        int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 2);

        EntityAOEArena arena = new EntityAOEArena(actor.world, actor);
        arena.setPosition(pos.x, y + 1, pos.z);
        actor.world.spawnEntity(arena);

        EntityAOEArena arena1 = new EntityAOEArena(actor.world, actor);
        arena1.setPosition(pos.x + 1, y + 1, pos.z);
        actor.world.spawnEntity(arena1);

        EntityAOEArena arena2 = new EntityAOEArena(actor.world, actor);
        arena2.setPosition(pos.x - 1, y + 1, pos.z);
        actor.world.spawnEntity(arena2);

        EntityAOEArena arena3 = new EntityAOEArena(actor.world, actor);
        arena3.setPosition(pos.x, y + 1, pos.z + 1);
        actor.world.spawnEntity(arena3);

        EntityAOEArena arena4 = new EntityAOEArena(actor.world, actor);
        arena4.setPosition(pos.x, y + 1, pos.z - 1);
        actor.world.spawnEntity(arena4);
    }


    private int getSurfaceHeight(World world, BlockPos pos, int min, int max)
    {
        int currentY = max;

        while(currentY >= min)
        {
            if(!world.isAirBlock(pos.add(0, currentY, 0)) && !world.isRemote) {
                return currentY;
            }

            currentY--;
        }

        return 0;
    }
}
