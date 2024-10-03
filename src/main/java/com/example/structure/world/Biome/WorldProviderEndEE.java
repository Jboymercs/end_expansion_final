package com.example.structure.world.Biome;

import com.example.structure.sky.EndSkyHandler;
import git.jbredwards.nether_api.api.biome.IEndBiome;
import git.jbredwards.nether_api.api.event.NetherAPIFogColorEvent;
import git.jbredwards.nether_api.mod.common.world.WorldProviderTheEnd;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;


public class WorldProviderEndEE extends WorldProviderTheEnd {

    public EndSkyHandler skyRenderer = new EndSkyHandler();

    public WorldProviderEndEE() {
        forceExtraEndFog = true;

    }


    @SideOnly(Side.CLIENT)
    @Override
    public boolean isSkyColored() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IRenderHandler getSkyRenderer() {
        return new EndSkyHandler();
    }


    @SideOnly(Side.CLIENT)
    @Override
    public boolean doesXZShowFog(final int x, final int z) {
        if(forceExtraEndFog) return true;
        @Nonnull final Biome biome = world.getBiome(new BlockPos(x, 0, z));
        return biome instanceof IEndBiome && ((IEndBiome)biome).hasExtraXZFog(world, x, z);
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    @Override
    public Vec3d getFogColor(final float celestialAngle, final float partialTicks) {

        return getFogColor(world, celestialAngle, partialTicks, 0.09411766, 0.07529412, 0.09411766, NetherAPIFogColorEvent.Nether::new);
    }

    @Override
    public double getVoidFogYFactor() {
        return 8.0f / 256f;
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    @Override
    public Vec3d getDefaultFogColor(@Nonnull final Biome biome, final float celestialAngle, final float partialTicks, final double defaultR, final double defaultG, final double defaultB) {
        return biome instanceof IEndBiome ? ((IEndBiome)biome).getFogColor(celestialAngle, partialTicks) : new Vec3d(defaultR, defaultG, defaultB);
    }

}
