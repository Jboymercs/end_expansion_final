package com.example.structure.entity.barrend.ultraparasite;

import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.ai.IAction;
import com.example.structure.init.ModBlocks;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ActionAOE implements IAction {


    private final int lengthOfAOE;

    public ActionAOE(int lengthOfAOE) {
        this.lengthOfAOE = lengthOfAOE;
    }

    @Override
    public void performAction(EntityModBase actor, EntityLivingBase target) {
        //this keeps the origin of the wave to this point even if the boss moves after the animation ends
        Vec3d savedPos = actor.getPositionVector();
        //Now we need to make this in a loop relative
        for(int t = 1; t < lengthOfAOE; t++ ) {
            int finalT = t;
            actor.addEvent(()-> {
                ModUtils.circleCallback(finalT, (4 * finalT), (pos) -> {
                    pos = new Vec3d(pos.x, 0, pos.y).add(savedPos);
                    EntityMoveTile tile = new EntityMoveTile(actor.world, actor, actor.getAttack() * 0.75F);
                    tile.setPosition(pos.x, pos.y, pos.z);
                    int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 3);
                    BlockPos posToo = new BlockPos(pos.x, y, pos.z);
                    tile.setOrigin(posToo, 5, posToo.getX() + 0.5D, posToo.getZ() + 0.5D);
                    tile.setLocationAndAngles(posToo.getX() + 0.5D, posToo.getY(), posToo.getZ() + 0.5D, 0.0f, 0.0F);
                    tile.setBlock(ModBlocks.BARE_SANS, 0);
                    actor.world.spawnEntity(tile);

                });
            }, 3 * t);
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
