package com.jboymercs.endexpansion.entity.shadowPlayer.action;

import com.jboymercs.endexpansion.entity.shadowPlayer.EntityShadowPlayer;
import com.jboymercs.endexpansion.util.ModUtils;
import com.jboymercs.endexpansion.util.handlers.ModSoundHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.concurrent.atomic.AtomicReference;

public class ActionForwardTeleport implements IActionShadow {

    private Vec3d savedPos;
    public ActionForwardTeleport(Vec3d savedPos) {
        this.savedPos = savedPos;
    }
    @Override
    public void performAction(EntityShadowPlayer actor, EntityLivingBase target) {
        Vec3d enemyPosToo = savedPos;

            actor.playSound(ModSoundHandler.BOSS_DASH, 2.0f, 1.0f / (actor.world.rand.nextFloat() * 0.4f + 0.4f));
            int randomDeterminedDistance = 8;

        Vec3d startPos = actor.getPositionVector().add(ModUtils.yVec(actor.getEyeHeight()));

            Vec3d dir = enemyPosToo.subtract(startPos).normalize();

            AtomicReference<Vec3d> teleportPos = new AtomicReference<>(enemyPosToo);

            ModUtils.lineCallback(enemyPosToo.add(dir), enemyPosToo.scale(randomDeterminedDistance), randomDeterminedDistance * 2, (pos, r) -> {

                boolean safeLanding = ModUtils.anyBlocksMatch(actor.world, pos, 0, -2, 0, 1, 0, 1,
                        blockPos -> actor.world.getBlockState(blockPos).isSideSolid(actor.world, blockPos.down(), EnumFacing.UP));
                boolean notOpen = ModUtils.anyBlocksMatch(actor.world, pos, -1, 1, -1, 1, 2, 1,
                        blockPos -> actor.world.getBlockState(blockPos).causesSuffocation());

                if (safeLanding && !notOpen) {
                    teleportPos.set(pos);
                }
            });
           actor.chargeDir = teleportPos.get();
            actor.setPositionAndUpdate(actor.chargeDir.x, actor.chargeDir.y, actor.chargeDir.z);

    }
}
