package com.example.structure.entity.trader.action;

import com.example.structure.Main;
import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.ai.IAction;
import com.example.structure.entity.ai.IMultiAction;
import com.example.structure.packets.MessageDirectionForRender;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import java.util.function.Consumer;

public class ActionMiniValonLazer implements IMultiAction {

    private EntityModBase entity;
    private boolean isShootingLazer;

    double maxLaserDistance;
    private final float laserExplosionSize;
    int beamLag;
    private final byte stopLaserByte;
    private final Consumer<Vec3d> onLaserImpact;

    public ActionMiniValonLazer(EntityModBase actor, byte stopLaserByte, Consumer<Vec3d> onLaserImpact) {
        this.entity = actor;
        this.laserExplosionSize = ModConfig.lazer_explosion_size;
        this.maxLaserDistance = 18;
        this.beamLag = 1;
        this.stopLaserByte = stopLaserByte;
        this.onLaserImpact = onLaserImpact;
    }
    @Override
    public void doAction() {
        int chargeUpTime = 20;
        int lazerEndTime = 420;
        for (int i = 0; i < chargeUpTime; i++) {
            entity.addEvent(() -> entity.world.setEntityState(entity, ModUtils.PARTICLE_BYTE), i);
        }
        entity.addEvent(()-> this.isShootingLazer = true, chargeUpTime);

        entity.addEvent(()-> {
            this.isShootingLazer = false;

            entity.addEvent(() -> entity.world.setEntityState(entity, stopLaserByte), beamLag + 1);
        }, lazerEndTime);
    }


    private Vec3d onLaserImpact(RayTraceResult raytraceresult) {
        Vec3d hitPos = raytraceresult.hitVec;
        if(laserExplosionSize > 0) {
            entity.world.createExplosion(entity, hitPos.x, hitPos.y, hitPos.z, laserExplosionSize, ModUtils.mobGriefing(entity.world, entity));
        }

        onLaserImpact.accept(hitPos);

        return hitPos;
    }


    @Override
    public void update() {
        if(this.isShootingLazer) {
                Vec3d lookPos = this.entity.getLookVec();
                Vec3d currentDirAndPosition = new Vec3d(entity.posX + lookPos.x, entity.posY + 0.5, entity.posZ + lookPos.z);

                entity.addEvent(() -> {

                    // Extend shooting beyond the target position up to 40 blocks
                    Vec3d laserDirection = lookPos.scale(10.0D).normalize();
                    Vec3d lazerPos = currentDirAndPosition.add(laserDirection.scale(maxLaserDistance));
                    // Ray trace both blocks and entities
                    RayTraceResult raytraceresult = entity.world.rayTraceBlocks(entity.getPositionVector().add(ModUtils.yVec(0.5)), lazerPos, false, true, false);
                    if (raytraceresult != null) {
                        lazerPos = onLaserImpact(raytraceresult);
                    }

                    for (Entity target : ModUtils.findEntitiesInLine(entity.getPositionVector().add(ModUtils.yVec(0.5)), lazerPos, entity.world, entity)) {
                        DamageSource source = ModDamageSource.builder()
                                .directEntity(entity)
                                .stoppedByArmorNotShields()
                                .type(ModDamageSource.MAGIC)
                                .build();
                        //Damage
                        target.attackEntityFrom(source, (float) (ModConfig.avalon_attack_damage * ModConfig.avalon_lazer_multiplier));
                    }

                    Main.network.sendToAllTracking(new MessageDirectionForRender(entity, lazerPos), entity);
                }, beamLag);
        }
    }



    @Override
    public int attackLength() {
        return 400;
    }


}
