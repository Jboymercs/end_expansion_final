package com.example.structure.entity.endking.EndKingAction;

import com.example.structure.Main;
import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.ai.IAction;
import com.example.structure.entity.ai.IMultiAction;
import com.example.structure.packets.MessageDirectionForRender;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import java.util.function.Consumer;

public class ActionShootLazer implements IMultiAction {

    private EntityModBase entity;
    private boolean isShootingLazer;

    double maxLaserDistance;
    private final float laserExplosionSize;
    int beamLag;
    private final byte stopLaserByte;
    private final Consumer<Vec3d> onLaserImpact;

    public ActionShootLazer(EntityModBase actorIn, byte stopLaserByte, Consumer<Vec3d> onLaserImpact) {
    this.entity = actorIn;
    this.laserExplosionSize = ModConfig.lazer_explosion_size;
    this.maxLaserDistance = ModConfig.lazer_distance;
    this.beamLag = ModConfig.lazer_beam_lag;
    this.stopLaserByte = stopLaserByte;
    this.onLaserImpact = onLaserImpact;
    }



    @Override
    public void doAction() {
        int chargeUpTime = 25;
        int lazerEndTime = 90;
        for (int i = 0; i < chargeUpTime; i++) {
            entity.addEvent(() -> entity.world.setEntityState(entity, ModUtils.PARTICLE_BYTE), i);
        }
        entity.addEvent(()-> this.isShootingLazer = true, chargeUpTime);

        entity.addEvent(()-> {
            this.isShootingLazer = false;

            entity.addEvent(() -> entity.world.setEntityState(entity, stopLaserByte), beamLag + 1);
        }, lazerEndTime);
    }

    @Override
    public void update() {
        if(this.isShootingLazer) {
            if(entity.getAttackTarget() != null) {
                Vec3d laserShootPos = entity.getAttackTarget().getPositionVector();
                entity.addEvent(() -> {

                    // Extend shooting beyond the target position up to 40 blocks
                    Vec3d laserDirection = laserShootPos.subtract(entity.getPositionEyes(1)).normalize();
                    Vec3d lazerPos = laserShootPos.add(laserDirection.scale(maxLaserDistance));
                    // Ray trace both blocks and entities
                    RayTraceResult raytraceresult = entity.world.rayTraceBlocks(entity.getPositionEyes(1), lazerPos, false, true, false);
                    if (raytraceresult != null) {
                        lazerPos = onLaserImpact(raytraceresult);
                    }

                    for (Entity target : ModUtils.findEntitiesInLine(entity.getPositionEyes(1), lazerPos, entity.world, entity)) {
                        DamageSource source = ModDamageSource.builder()
                                .directEntity(entity)
                                .stoppedByArmorNotShields()
                                .type(ModDamageSource.MAGIC)
                                .build();
                        target.attackEntityFrom(source, (float) (entity.getAttack() * ModConfig.lazer_damage_multiplier));
                    }

                    ModUtils.addEntityVelocity(entity, laserDirection.scale(-0.03f));

                    Main.network.sendToAllTracking(new MessageDirectionForRender(entity, lazerPos), entity);
                }, beamLag);
            }
            else {
                // Prevent the gauntlet from instantly locking onto other targets with the lazer.
                isShootingLazer = false;
                entity.addEvent(() -> entity.world.setEntityState(entity, stopLaserByte), beamLag + 1);
            }

        }
    }

    private Vec3d onLaserImpact(RayTraceResult raytraceresult) {
        Vec3d hitPos = raytraceresult.hitVec;
        if(laserExplosionSize > 0) {
            entity.world.createExplosion(entity, hitPos.x, hitPos.y, hitPos.z, laserExplosionSize, ModUtils.mobGriefing(entity.world, entity));
        }

        onLaserImpact.accept(hitPos);

        if(entity.ticksExisted % 2 == 0) {
            ModUtils.destroyBlocksInAABB(ModUtils.vecBox(hitPos, hitPos).grow(0.1), entity.world, entity);
        }
        return hitPos;
    }

    @Override
    public int attackLength() {
        return 90;
    }
}
