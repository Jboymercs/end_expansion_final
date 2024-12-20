package com.example.structure.entity.shadowPlayer.action;

import com.example.structure.entity.ProjectileAcid;
import com.example.structure.entity.shadowPlayer.EntityShadowPlayer;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class ActionShootRanged implements IActionShadow{
    @Override
    public void performAction(EntityShadowPlayer actor, EntityLivingBase target) {
        ProjectileAcid missile = new ProjectileAcid(actor.world, actor, (float) (actor.getAttack() * 0.8));
        ProjectileAcid missile_2 = new ProjectileAcid(actor.world, actor, (float) (actor.getAttack() * 0.8));
        ProjectileAcid missile_3 = new ProjectileAcid(actor.world, actor, (float) (actor.getAttack() * 0.8));
        ProjectileAcid missile_4 = new ProjectileAcid(actor.world, actor,(float) (actor.getAttack() * 0.8));
        ProjectileAcid missile_5 = new ProjectileAcid(actor.world, actor, (float) (actor.getAttack() * 0.8));

        Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.5,1.5,0)));
        Vec3d relPos2 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.5,1.5,0.8)));
        Vec3d relPos3 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.5,1.5,-0.8)));
        Vec3d relPos4 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.5,1.5,-1.6)));
        Vec3d relPos5 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.5,1.5,1.6)));

        missile.setPosition(relPos.x, relPos.y, relPos.z);
        missile_2.setPosition(relPos2.x, relPos2.y, relPos2.z);
        missile_3.setPosition(relPos3.x, relPos3.y, relPos3.z);
        missile_4.setPosition(relPos4.x, relPos4.y, relPos4.z);
        missile_5.setPosition(relPos5.x, relPos5.y, relPos5.z);

        actor.world.spawnEntity(missile);
        actor.world.spawnEntity(missile_2);
        actor.world.spawnEntity(missile_3);
        actor.world.spawnEntity(missile_4);
        actor.world.spawnEntity(missile_5);
        missile.setTravelRange(40);
        missile_2.setTravelRange(40);
        missile_3.setTravelRange(40);
        missile_4.setTravelRange(40);
        missile_5.setTravelRange(40);

        for(int i = 0; i < 13; i++) {
            actor.addEvent(()-> {
                Vec3d pos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.5,1.5,0)));
                ModUtils.setEntityPosition(missile, pos);
            }, i);

            actor.addEvent(()-> {
                Vec3d pos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.5,1.5,0.8)));
                ModUtils.setEntityPosition(missile_2, pos);
            }, i);

            actor.addEvent(()-> {
                Vec3d pos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.5,1.5,-0.8)));
                ModUtils.setEntityPosition(missile_3, pos);
            }, i);
            actor.addEvent(()-> {
                Vec3d pos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.5,1.5,-1.6)));
                ModUtils.setEntityPosition(missile_4, pos);
            }, i);
            actor.addEvent(()-> {
                Vec3d pos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.5,1.5,1.6)));
                ModUtils.setEntityPosition(missile_5, pos);
            }, i);
        }

        //Shoot Projectiles
        actor.addEvent(()-> {
            actor.playSound(SoundEvents.ENTITY_SHULKER_SHOOT, 2.0f, 0.8f / (new Random().nextFloat() * 0.4f + 0.2f));
            Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, -0.75, 0)));

            Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
            Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
            Vec3d lineStart = targetPos.subtract(lineDir);
            Vec3d lineEnd = targetPos.add(lineDir);

            float speed = (float) 1.3;

            ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                ModUtils.throwProjectileNoSpawn(pos,missile,0F, speed);
            });
        }, 14);

        actor.addEvent(()-> {
            Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, -0.75, 0.8)));

            Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
            Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
            Vec3d lineStart = targetPos.subtract(lineDir);
            Vec3d lineEnd = targetPos.add(lineDir);

            float speed = (float) 1.3;
            ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                ModUtils.throwProjectileNoSpawn(pos,missile_2,0F, speed);
            });
        }, 14);

        actor.addEvent(()-> {
            Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, -0.75, -0.8)));

            Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
            Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
            Vec3d lineStart = targetPos.subtract(lineDir);
            Vec3d lineEnd = targetPos.add(lineDir);

            float speed = (float) 1.3;
            ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                ModUtils.throwProjectileNoSpawn(pos,missile_3,0F, speed);
            });
        }, 14);

        actor.addEvent(()-> {
            Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, -0.75, -1.6)));

            Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
            Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
            Vec3d lineStart = targetPos.subtract(lineDir);
            Vec3d lineEnd = targetPos.add(lineDir);

            float speed = (float) 1.3;
            ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                ModUtils.throwProjectileNoSpawn(pos,missile_4,0F, speed);
            });
        }, 14);

        actor.addEvent(()-> {
            Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, -0.75, 1.6)));

            Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
            Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
            Vec3d lineStart = targetPos.subtract(lineDir);
            Vec3d lineEnd = targetPos.add(lineDir);

            float speed = (float) 1.3;
            ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                ModUtils.throwProjectileNoSpawn(pos,missile_5,0F, speed);
            });
        }, 14);
    }
}
