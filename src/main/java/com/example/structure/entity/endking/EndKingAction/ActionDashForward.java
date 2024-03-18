package com.example.structure.entity.endking.EndKingAction;

import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.ai.IAction;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.concurrent.atomic.AtomicReference;

public class ActionDashForward implements IAction {
    @Override
    public void performAction(EntityModBase actor, EntityLivingBase target) {
        actor.addEvent(()-> {
            int randomDeterminedDistance = ModRand.range(4, 6);
            Vec3d enemyPos = target.getPositionVector().add(ModUtils.yVec(1));

            Vec3d posThisSet = actor.getPositionVector();

            Vec3d startPos = actor.getPositionVector().add(ModUtils.yVec(actor.getEyeHeight()));

            Vec3d dir = enemyPos.subtract(startPos).normalize();

            AtomicReference<Vec3d> teleportPos = new AtomicReference<>(enemyPos);

            ModUtils.lineCallback(enemyPos.add(dir),enemyPos.scale(randomDeterminedDistance), randomDeterminedDistance * 2, (pos, r) -> {
                boolean safeLanding = ModUtils.cubePoints(0, -2, 0, 1, 0, 1).stream()
                        .anyMatch(off -> actor.world.getBlockState(new BlockPos(pos.add(off)))
                                .isSideSolid(actor.world, new BlockPos(pos.add(off)).down(), EnumFacing.UP));
                boolean notOpen = ModUtils.cubePoints(0, 1, 0, 1, 3, 1).stream()
                        .anyMatch(off -> actor.world.getBlockState(new BlockPos(pos.add(off)))
                                .causesSuffocation());

                if (safeLanding && !notOpen) {
                    teleportPos.set(pos);
                }
            });
        }, 10);
    }
}
