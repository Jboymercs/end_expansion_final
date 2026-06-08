package com.jboymercs.endexpansion.entity.endking.EndKingAction;

import com.jboymercs.endexpansion.entity.EntityModBase;
import com.jboymercs.endexpansion.entity.ai.IAction;
import com.jboymercs.endexpansion.util.ModRand;
import com.jboymercs.endexpansion.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.concurrent.atomic.AtomicReference;

public class ActionDashBack implements IAction {
    @Override
    public void performAction(EntityModBase actor, EntityLivingBase target) {
        actor.addEvent(()-> {
            int randomDeterminedDistance = ModRand.range(4, 6);
            Vec3d enemyPos = target.getPositionVector().add(ModUtils.yVec(1));

            Vec3d posThisSet = actor.getPositionVector();

            Vec3d startPos = actor.getPositionVector().add(ModUtils.yVec(actor.getEyeHeight()));

            Vec3d dir = startPos.subtract(enemyPos).normalize();

            AtomicReference<Vec3d> teleportPos = new AtomicReference<>(enemyPos);

            ModUtils.lineCallback(enemyPos.add(dir),enemyPos.scale(randomDeterminedDistance), randomDeterminedDistance * 2, (pos, r) -> {
                boolean safeLanding = ModUtils.anyBlocksMatch(actor.world, pos, 0, -2, 0, 1, 0, 1,
                        blockPos -> actor.world.getBlockState(blockPos).isSideSolid(actor.world, blockPos.down(), EnumFacing.UP));
                boolean notOpen = ModUtils.anyBlocksMatch(actor.world, pos, 0, 1, 0, 1, 3, 1,
                        blockPos -> actor.world.getBlockState(blockPos).causesSuffocation());

                if (safeLanding && !notOpen) {
                    teleportPos.set(pos);
                }
            });

        }, 10);
    }
}
