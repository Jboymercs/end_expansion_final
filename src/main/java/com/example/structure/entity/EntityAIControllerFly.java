package com.example.structure.entity;

import com.example.structure.init.ModBlocks;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.time.Year;
import java.util.Random;

public class EntityAIControllerFly extends EntityAIBase {

    private final EntityController parentEntity;

    protected boolean resetTest = false;
    int tickDelay = 300;
    public EntityAIControllerFly(EntityController e) {
        this.parentEntity = e;
        this.setMutexBits(3);
    }
    @Override
    public boolean shouldExecute() {
        EntityLivingBase target = parentEntity.getAttackTarget();
        if(parentEntity.isInteract() || parentEntity.isHuntingBlock || target != null) {
            return false;
        }

        if(resetTest) {
            if(tickDelay < 0) {
                resetTest = false;
                tickDelay = 300;
            } else {
                tickDelay--;
            }
        }

        return parentEntity.world.rand.nextInt(15) == 0 && parentEntity.getNavigator().noPath() && !resetTest;
    }



    @Override
    public void startExecuting() {
        Random random = this.parentEntity.getRNG();
        BlockPos Loc = parentEntity.getPosition();
        int yHeight = getGroundFromAbove(parentEntity.world, Loc.getX(), Loc.getZ());
        if(yHeight != 0) {
            if((Loc.getY() - yHeight) > 5) {
                double d0 = this.parentEntity.posX + ModRand.range(-10, 10);
                double d1 = (this.parentEntity.posY + ModRand.range(-5, -1));
                double d2 = this.parentEntity.posZ + ModRand.range(-10, 10);
                this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);
                if((Loc.getY() - yHeight) < 3) {
                    resetTest = true;
                }
            } else {
                double d0 = this.parentEntity.posX + ModRand.range(-10, 10);
                double d1 = (this.parentEntity.posY + ModRand.range(2, 4));
                double d2 = this.parentEntity.posZ + ModRand.range(-10, 10);
                this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);
                if((Loc.getY() - yHeight) > 8) {
                    resetTest = true;
                }
            }
        } else if (Loc.getY() < 65){
            double d0 = this.parentEntity.posX + ModRand.range(-10, 10);
            double d1 = (this.parentEntity.posY + ModRand.range(-5, -1));
            double d2 = this.parentEntity.posZ + ModRand.range(-10, 10);
            this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);
            if(Loc.getY() < 35) {
                resetTest = true;
            }
        } else if(Loc.getY() > 30) {
            double d0 = this.parentEntity.posX + ModRand.range(-10, 10);
            double d1 = (this.parentEntity.posY + ModRand.range(3, 5));
            double d2 = this.parentEntity.posZ + ModRand.range(-10, 10);
            this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);
            if(Loc.getY() > 60) {
                resetTest = true;
            }
        }
    }

    @Override
    public void updateTask() {
        Vec3d pos = ModUtils.getEntityVelocity(parentEntity).normalize().scale(0.1).add(parentEntity.getPositionVector());
        ModUtils.facePosition(pos, parentEntity, 45, 45);
        parentEntity.getLookHelper().setLookPosition(pos.x, pos.y, pos.z, 3, 3);
        super.updateTask();
    }

    public static int getGroundFromAbove(World world, int x, int z) {
        int y = 255;
        boolean foundGround = false;
        while (!foundGround && y-- >= 31) {
            Block blockAt = world.getBlockState(new BlockPos(x, y, z)).getBlock();
            foundGround = blockAt == ModBlocks.END_ASH || blockAt == ModBlocks.BROWN_END_STONE || blockAt == Blocks.END_STONE || blockAt == Blocks.GRASS || blockAt == Blocks.SAND;
        }

        return y;
    }
}
