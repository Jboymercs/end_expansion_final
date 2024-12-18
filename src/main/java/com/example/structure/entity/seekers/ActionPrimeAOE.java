package com.example.structure.entity.seekers;

import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.ai.IAction;
import com.example.structure.entity.trader.EntityControllerLift;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ActionPrimeAOE implements IAction {

    private final int lengthOfAOE;

    public ActionPrimeAOE(int lengthOfAOE) {
        this.lengthOfAOE = lengthOfAOE;
    }

    @Override
    public void performAction(EntityModBase actor, EntityLivingBase target) {
        Vec3d savedPos = actor.getPositionVector();
        //Now we need to make this in a loop relative
        if(lengthOfAOE > 10) {
            for(int t = 1; t < 10; t++ ) {
                int finalT = t;
                actor.addEvent(()-> {
                    ModUtils.circleCallback(finalT, (4 * finalT), (pos) -> {
                        pos = new Vec3d(pos.x, 0, pos.y).add(savedPos);
                        EntityControllerLift tile = new EntityControllerLift(actor.world);
                        tile.setPosition(pos.x, pos.y, pos.z);
                        int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 6, (int) actor.posY + 4);
                        BlockPos posToo = new BlockPos(pos.x, y + 1, pos.z);
                        tile.setLocationAndAngles(posToo.getX() + 0.5D, posToo.getY(), posToo.getZ() + 0.5D, 0.0f, 0.0F);
                        actor.world.spawnEntity(tile);
                    });
                }, t * 3);
            }

        } else {
            for(int t = 1; t < lengthOfAOE; t++ ) {
                int finalT = t;
                actor.addEvent(()-> {
                    ModUtils.circleCallback(finalT, (4 * finalT), (pos) -> {
                        pos = new Vec3d(pos.x, 0, pos.y).add(savedPos);
                        EntityControllerLift tile = new EntityControllerLift(actor.world);
                        tile.setPosition(pos.x, pos.y, pos.z);
                        int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 6, (int) actor.posY + 4);
                        BlockPos posToo = new BlockPos(pos.x, y + 1, pos.z);
                        tile.setLocationAndAngles(posToo.getX() + 0.5D, posToo.getY(), posToo.getZ() + 0.5D, 0.0f, 0.0F);
                        actor.world.spawnEntity(tile);
                    });
                }, t * 3);
            }
        }
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
