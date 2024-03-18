package com.example.structure.entity.endking.EndKingAction;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityCrystalSpikeSmall;
import com.example.structure.entity.EntityGroundCrystal;
import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.Projectile;
import com.example.structure.entity.ai.IAction;
import com.example.structure.entity.endking.EntityGroundSword;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class ActionSummonSwordAttacks implements IAction {

    protected int uniqueIDtoperform = ModRand.range(1, 3);

    protected int xInt = ModRand.range(-5, 5);
    protected int zInt = ModRand.range(-5, 5);
    protected boolean isFastSwords;

    public ActionSummonSwordAttacks(boolean isFastSwords) {
        this.isFastSwords = isFastSwords;
    }

    @Override
    public void performAction(EntityModBase actor, EntityLivingBase target) {

        if(uniqueIDtoperform == 1) {
            //Random Sword Dropping
            actor.addEvent(() -> {
                Vec3d targetPos = target.getPositionVector().add(ModRand.range(-4, 4), 0, ModRand.range(-4, 4));
                EntityGroundSword sword = new EntityGroundSword(actor.world, isFastSwords);
                sword.setPosition(targetPos.x, targetPos.y, targetPos.z);
                actor.world.spawnEntity(sword);
            }, 5);
            actor.addEvent(() -> {
                Vec3d targetPos = target.getPositionVector().add(ModRand.range(-4, 4), 0, ModRand.range(-4, 4));
                EntityGroundSword sword = new EntityGroundSword(actor.world, isFastSwords);
                sword.setPosition(targetPos.x, targetPos.y, targetPos.z);
                actor.world.spawnEntity(sword);
            }, 10);
            actor.addEvent(() -> {
                Vec3d targetPos = target.getPositionVector().add(ModRand.range(-4, 4), 0, ModRand.range(-4, 4));
                EntityGroundSword sword = new EntityGroundSword(actor.world, isFastSwords);
                sword.setPosition(targetPos.x, targetPos.y, targetPos.z);
                actor.world.spawnEntity(sword);
            }, 15);
            actor.addEvent(() -> {
                Vec3d targetPos = target.getPositionVector().add(ModRand.range(-4, 4), 0, ModRand.range(-4, 4));
                EntityGroundSword sword = new EntityGroundSword(actor.world, isFastSwords);
                sword.setPosition(targetPos.x, targetPos.y, targetPos.z);
                actor.world.spawnEntity(sword);
            }, 20);
            actor.addEvent(() -> {
                Vec3d targetPos = target.getPositionVector().add(ModRand.range(-4, 4), 0, ModRand.range(-4, 4));
                EntityGroundSword sword = new EntityGroundSword(actor.world, isFastSwords);
                sword.setPosition(targetPos.x, targetPos.y, targetPos.z);
                actor.world.spawnEntity(sword);
            }, 25);
            actor.addEvent(() -> {
                Vec3d targetPos = target.getPositionVector().add(ModRand.range(-4, 4), 0, ModRand.range(-4, 4));
                EntityGroundSword sword = new EntityGroundSword(actor.world, isFastSwords);
                sword.setPosition(targetPos.x, targetPos.y, targetPos.z);
                actor.world.spawnEntity(sword);
            }, 30);
            actor.addEvent(() -> {
                Vec3d targetPos = target.getPositionVector().add(ModRand.range(-4, 4), 0, ModRand.range(-4, 4));
                EntityGroundSword sword = new EntityGroundSword(actor.world, isFastSwords);
                sword.setPosition(targetPos.x, targetPos.y, targetPos.z);
                actor.world.spawnEntity(sword);
            }, 35);
            actor.addEvent(() -> {
                Vec3d targetPos = target.getPositionVector().add(ModRand.range(-4, 4), 0, ModRand.range(-4, 4));
                EntityGroundSword sword = new EntityGroundSword(actor.world, isFastSwords);
                sword.setPosition(targetPos.x, targetPos.y, targetPos.z);
                actor.world.spawnEntity(sword);
            }, 40);
            actor.addEvent(() -> {
                Vec3d targetPos = target.getPositionVector().add(ModRand.range(-4, 4), 0, ModRand.range(-4, 4));
                EntityGroundSword sword = new EntityGroundSword(actor.world, isFastSwords);
                sword.setPosition(targetPos.x, targetPos.y, targetPos.z);
                actor.world.spawnEntity(sword);
            }, 45);
            actor.addEvent(() -> {
                Vec3d targetPos = target.getPositionVector().add(ModRand.range(-4, 4), 0, ModRand.range(-4, 4));
                EntityGroundSword sword = new EntityGroundSword(actor.world, isFastSwords);
                sword.setPosition(targetPos.x, targetPos.y, targetPos.z);
                actor.world.spawnEntity(sword);
            }, 50);
            actor.addEvent(() -> {
                Vec3d targetPos = target.getPositionVector().add(ModRand.range(-4, 4), 0, ModRand.range(-4, 4));
                EntityGroundSword sword = new EntityGroundSword(actor.world, isFastSwords);
                sword.setPosition(targetPos.x, targetPos.y, targetPos.z);
                actor.world.spawnEntity(sword);
            }, 55);
            actor.addEvent(() -> {
                Vec3d targetPos = target.getPositionVector().add(ModRand.range(-4, 4), 0, ModRand.range(-4, 4));
                EntityGroundSword sword = new EntityGroundSword(actor.world, isFastSwords);
                sword.setPosition(targetPos.x, targetPos.y, targetPos.z);
                actor.world.spawnEntity(sword);
            }, 60);
        } else if(uniqueIDtoperform == 2) {
            //Line Sword Dropping
            Vec3d targetPosition = target.getPositionVector();
            Vec3d throwerPosition = actor.getPositionVector();
            Vec3d dir = targetPosition.subtract(throwerPosition).normalize();
            AtomicReference<Vec3d> spawnPos = new AtomicReference<>(throwerPosition);
            for (int i = 0; i < 60; i += 4) {
                int additive = i;
                actor.addEvent(() -> {

                    ModUtils.lineCallback(throwerPosition.add(dir), throwerPosition.add(dir.scale(additive)), additive * 2, (pos, r) -> {
                        spawnPos.set(pos);
                    });
                    Vec3d initPos = spawnPos.get();
                    EntityGroundSword crystal = new EntityGroundSword(actor.world, isFastSwords);
                    BlockPos blockPos = new BlockPos(initPos.x, initPos.y, initPos.z);
                    crystal.setPosition(blockPos);
                    actor.world.spawnEntity(crystal);
                }, i);
            }
        }


    }
}
