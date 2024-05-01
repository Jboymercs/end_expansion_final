package com.example.structure.entity.ai;

import com.example.structure.entity.EntityBarrendParasite;
import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.trader.EntityAOEArena;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ModSoundHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class ActionBarrendSummon implements IAction{

    public ActionBarrendSummon() {

    }
    @Override
    public void performAction(EntityModBase actor, EntityLivingBase target) {
        if(actor.getHorizontalFacing() == EnumFacing.EAST || actor.getHorizontalFacing() == EnumFacing.WEST) {
            int maxDistance = 16;
            Vec3d startPosCenter = new Vec3d(actor.posX, actor.posY, actor.posZ);
            Vec3d startPosL1 = new Vec3d(actor.posX, actor.posY, actor.posZ + 1);
            Vec3d startPosL2 = new Vec3d(actor.posX , actor.posY, actor.posZ + 2);
            Vec3d startPosR1 = new Vec3d(actor.posX, actor.posY, actor.posZ - 1);
            Vec3d startPosR2 = new Vec3d(actor.posX, actor.posY, actor.posZ - 2);
            Vec3d targetedPosCenter = new Vec3d(target.posX, startPosCenter.y, target.posZ);
            Vec3d targetedPosL1 = new Vec3d(target.posX, startPosCenter.y, target.posZ + 1);
            Vec3d targetedPosL2 = new Vec3d(target.posX, startPosCenter.y, target.posZ + 2);
            Vec3d targetedPosR1 = new Vec3d(target.posX, startPosCenter.y, target.posZ - 1);
            Vec3d targetedPosR2 = new Vec3d(target.posX, startPosCenter.y, target.posZ - 2);
            Vec3d dirCenter = targetedPosCenter.subtract(startPosCenter).normalize();
            Vec3d dirL1 = targetedPosL1.subtract(startPosL1).normalize();
            Vec3d dirL2 = targetedPosL2.subtract(startPosL2).normalize();
            Vec3d dirR1 = targetedPosR1.subtract(startPosR1).normalize();
            Vec3d dirR2 = targetedPosR2.subtract(startPosR2).normalize();

            Random random = new Random();
            actor.playSound(ModSoundHandler.PARASITE_GROUND, 1.4f, 1.0f / random.nextFloat() * 0.4f + 0.4f);
            actor.playSound(ModSoundHandler.PARASITE_GROUND, 1.4f, 1.0f / random.nextFloat() * 0.4f + 0.4f);
            actor.playSound(ModSoundHandler.PARASITE_GROUND, 1.4f, 1.0f / random.nextFloat() * 0.4f + 0.4f);

            ModUtils.lineCallback(startPosCenter.add(dirCenter), startPosCenter.add(dirCenter.scale(maxDistance)), maxDistance * 2, (pos , i) -> {
                EntityBarrendParasite arena = new EntityBarrendParasite(actor.world);
                arena.setPosition(pos.x, pos.y,pos.z);
                actor.world.spawnEntity(arena);
            });

            ModUtils.lineCallback(startPosL1.add(dirL1), startPosL1.add(dirL1.scale(maxDistance)), maxDistance * 2, (pos , i) -> {
                EntityBarrendParasite arena = new EntityBarrendParasite(actor.world);
                arena.setPosition(pos.x, pos.y,pos.z);
                actor.world.spawnEntity(arena);
            });

            ModUtils.lineCallback(startPosL2.add(dirL2), startPosL2.add(dirL2.scale(maxDistance)), maxDistance * 2, (pos , i) -> {
                EntityBarrendParasite arena = new EntityBarrendParasite(actor.world);
                arena.setPosition(pos.x, pos.y,pos.z);
                actor.world.spawnEntity(arena);
            });

            ModUtils.lineCallback(startPosR1.add(dirR1), startPosR1.add(dirR1.scale(maxDistance)), maxDistance * 2, (pos , i) -> {
                EntityBarrendParasite arena = new EntityBarrendParasite(actor.world);
                arena.setPosition(pos.x, pos.y,pos.z);
                actor.world.spawnEntity(arena);
            });

            ModUtils.lineCallback(startPosR2.add(dirR2), startPosR2.add(dirR2.scale(maxDistance)), maxDistance * 2, (pos , i) -> {
                EntityBarrendParasite arena = new EntityBarrendParasite(actor.world);
                arena.setPosition(pos.x, pos.y,pos.z);
                actor.world.spawnEntity(arena);
            });
        } else if(actor.getHorizontalFacing() == EnumFacing.NORTH || actor.getHorizontalFacing() == EnumFacing.SOUTH) {
            //Correalates the Direction for specific facings
            int maxDistance = 16;
            Vec3d startPosCenter = new Vec3d(actor.posX, actor.posY, actor.posZ);
            Vec3d startPosL1 = new Vec3d(actor.posX + 1, actor.posY, actor.posZ);
            Vec3d startPosL2 = new Vec3d(actor.posX + 2, actor.posY, actor.posZ);
            Vec3d startPosR1 = new Vec3d(actor.posX - 1, actor.posY, actor.posZ);
            Vec3d startPosR2 = new Vec3d(actor.posX - 2, actor.posY, actor.posZ);
            Vec3d targetedPosCenter = new Vec3d(target.posX, startPosCenter.y, target.posZ);
            Vec3d targetedPosL1 = new Vec3d(target.posX + 1, startPosCenter.y, target.posZ);
            Vec3d targetedPosL2 = new Vec3d(target.posX + 2, startPosCenter.y, target.posZ);
            Vec3d targetedPosR1 = new Vec3d(target.posX - 1, startPosCenter.y, target.posZ);
            Vec3d targetedPosR2 = new Vec3d(target.posX - 2, startPosCenter.y, target.posZ);
            Vec3d dirCenter = targetedPosCenter.subtract(startPosCenter).normalize();
            Vec3d dirL1 = targetedPosL1.subtract(startPosL1).normalize();
            Vec3d dirL2 = targetedPosL2.subtract(startPosL2).normalize();
            Vec3d dirR1 = targetedPosR1.subtract(startPosR1).normalize();
            Vec3d dirR2 = targetedPosR2.subtract(startPosR2).normalize();
            Random random = new Random();
            actor.playSound(ModSoundHandler.PARASITE_GROUND, 1.4f, 1.0f / random.nextFloat() * 0.4f + 0.4f);
            actor.playSound(ModSoundHandler.PARASITE_GROUND, 1.4f, 1.0f / random.nextFloat() * 0.4f + 0.4f);
            actor.playSound(ModSoundHandler.PARASITE_GROUND, 1.4f, 1.0f / random.nextFloat() * 0.4f + 0.4f);

            ModUtils.lineCallback(startPosCenter.add(dirCenter), startPosCenter.add(dirCenter.scale(maxDistance)), maxDistance * 2, (pos , i) -> {
                EntityBarrendParasite arena = new EntityBarrendParasite(actor.world);
                arena.setPosition(pos.x, pos.y,pos.z);
                actor.world.spawnEntity(arena);
            });

            ModUtils.lineCallback(startPosL1.add(dirL1), startPosL1.add(dirL1.scale(maxDistance)), maxDistance * 2, (pos , i) -> {
                EntityBarrendParasite arena = new EntityBarrendParasite(actor.world);
                arena.setPosition(pos.x, pos.y,pos.z);
                actor.world.spawnEntity(arena);
            });

            ModUtils.lineCallback(startPosL2.add(dirL2), startPosL2.add(dirL2.scale(maxDistance)), maxDistance * 2, (pos , i) -> {
                EntityBarrendParasite arena = new EntityBarrendParasite(actor.world);
                arena.setPosition(pos.x, pos.y,pos.z);
                actor.world.spawnEntity(arena);
            });

            ModUtils.lineCallback(startPosR1.add(dirR1), startPosR1.add(dirR1.scale(maxDistance)), maxDistance * 2, (pos , i) -> {
                EntityBarrendParasite arena = new EntityBarrendParasite(actor.world);
                arena.setPosition(pos.x, pos.y,pos.z);
                actor.world.spawnEntity(arena);
            });

            ModUtils.lineCallback(startPosR2.add(dirR2), startPosR2.add(dirR2.scale(maxDistance)), maxDistance * 2, (pos , i) -> {
                EntityBarrendParasite arena = new EntityBarrendParasite(actor.world);
                arena.setPosition(pos.x, pos.y,pos.z);
                actor.world.spawnEntity(arena);
            });
        }
    }
}
