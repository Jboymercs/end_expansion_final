package com.example.structure.util.handlers;

import com.example.structure.particle.EffectParticle;
import com.example.structure.util.ModColors;
import com.example.structure.util.ModParticle;
import com.example.structure.util.ModRand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.*;
import net.minecraft.item.Item;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

public class ParticleManager {

    public static void spawnCrystalWisps(World worldIn, Vec3d pos, Vec3d color, Vec3d motion) {
        ModParticle modParticle = new ModParticle(worldIn, pos, motion, 2, ModRand.range(10, 20), false);
        modParticle.setParticleTextureRange(0, 9, 2);
        spawnParticleWithColor(modParticle, color);
    }

    private static void spawnParticleWithColor(Particle particle, Vec3d baseColor) {
        baseColor = ModColors.variateColor(baseColor, 0.2f);
        particle.setRBGColorF((float) baseColor.x, (float) baseColor.y, (float) baseColor.z);
        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
    }


    public static void spawnColoredSmoke(World worldIn, Vec3d pos, Vec3d baseColor, Vec3d vel) {
        Particle particle = new ParticleSmokeNormal.Factory().createParticle(0, worldIn, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z);

        baseColor = ModColors.variateColor(baseColor, 0.2f);
        particle.setRBGColorF((float) baseColor.x, (float) baseColor.y, (float) baseColor.z);

        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
    }

    public static void spawnBrightFlames(World worldIn, Random rand, Vec3d pos, Vec3d vel) {
        Particle particle = new ParticleFlame.Factory().createParticle(0, worldIn, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z);

        float f = ModRand.getFloat(0.1f);
        particle.setRBGColorF(0.1f, 0.5f, 1f);

        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
    }

    public static void spawnEffect(World world, Vec3d pos, Vec3d baseColor) {
        Particle particle = new EffectParticle.Factory().createParticle(0, world, pos.x, pos.y, pos.z, 0, 0, 0);
        baseColor = ModColors.variateColor(baseColor, 0.2f);
        particle.setRBGColorF((float) baseColor.x, (float) baseColor.y, (float) baseColor.z);
        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
    }

    public static void spawnBreak(World world, Vec3d pos, Item item, Vec3d vel) {
        Particle particle = new ParticleBreaking.Factory().createParticle(0, world, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z, Item.getIdFromItem(item));
        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
    }

    public static void spawnSwirl2(World worldIn, Vec3d pos, Vec3d baseColor, Vec3d motion) {
        Particle particle = new ParticleFlame.Factory().createParticle(0, worldIn, pos.x, pos.y, pos.z, motion.x, motion.y, motion.z);

        spawnParticleWithColor(particle, baseColor);
    }


}
