package com.example.structure.entity.shadowPlayer.action;

import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.ai.IAction;
import com.example.structure.entity.shadowPlayer.EntityShadowPlayer;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ModSoundHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

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

                boolean safeLanding = ModUtils.cubePoints(0, -2, 0, 1, 0, 1).stream()
                        .anyMatch(off -> actor.world.getBlockState(new BlockPos(pos.add(off)))
                                .isSideSolid(actor.world, new BlockPos(pos.add(off)).down(), EnumFacing.UP));
                boolean notOpen = ModUtils.cubePoints(-1, 1, -1, 1, 2, 1).stream()
                        .anyMatch(off -> actor.world.getBlockState(new BlockPos(pos.add(off)))
                                .causesSuffocation());

                if (safeLanding && !notOpen) {
                    teleportPos.set(pos);
                }
            });
           actor.chargeDir = teleportPos.get();
            actor.setPositionAndUpdate(actor.chargeDir.x, actor.chargeDir.y, actor.chargeDir.z);

    }
}
