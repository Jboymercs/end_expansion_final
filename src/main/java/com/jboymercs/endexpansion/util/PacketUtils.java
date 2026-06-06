package com.jboymercs.endexpansion.util;

import com.jboymercs.endexpansion.packets.MessageModParticles;
import com.jboymercs.endexpansion.util.handlers.ParticleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PacketUtils {


    public static World getWorld() {
        return Minecraft.getMinecraft().world;
    }


    public static void spawnEffect(MessageModParticles message) {
        ParticleManager.spawnEffect(Minecraft.getMinecraft().world, new Vec3d(message.xCoord, message.yCoord, message.zCoord), new Vec3d(message.particleArguments[0], message.particleArguments[1], message.particleArguments[2]));
    }

    public static EntityPlayer getPlayer() {
        return Minecraft.getMinecraft().player;
    }

}
