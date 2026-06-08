package com.jboymercs.endexpansion.world.Biome;

import com.jboymercs.endexpansion.sky.EndSkyHandler;
import git.jbredwards.nether_api.api.biome.IEndBiome;
import git.jbredwards.nether_api.mod.common.world.WorldProviderTheEnd;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
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


//    @SideOnly(Side.CLIENT)
//    @Override
//    public boolean isSkyColored() {
//        return true;
//    }
//
//    @SideOnly(Side.CLIENT)
//    @Override
//    public IRenderHandler getSkyRenderer() {
//        return new EndSkyHandler();
//    }
//
//
//    @SideOnly(Side.CLIENT)
//    @Override
//    public boolean doesXZShowFog(final int x, final int z) {
//        if(forceExtraEndFog) return true;
//        @Nonnull final Biome biome = world.getBiome(new BlockPos(x, 0, z));
//        return biome instanceof IEndBiome && ((IEndBiome)biome).hasExtraXZFog(world, x, z);
//    }
//
//    @Nonnull
//    @SideOnly(Side.CLIENT)
//    @Override
//    public Vec3d getFogColor(final float celestialAngle, final float partialTicks) {
//        return super.getFogColor(celestialAngle, partialTicks);
//    }
//
//    @Override
//    public double getVoidFogYFactor() {
//        return 8.0f / 256f;
//    }
//
//    @Nonnull
//    @SideOnly(Side.CLIENT)
//    @Override
//    public Vec3d getBiomeFogColor(final float celestialAngle, final float partialTicks, @Nonnull final Biome biome) {
//        return biome instanceof IEndBiome ? ((IEndBiome)biome).getFogColor(celestialAngle, partialTicks) : getDefaultFogColor(celestialAngle, partialTicks);
//    }
//
//    @Nonnull
//    @SideOnly(Side.CLIENT)
//    @Override
//    public Vec3d getDefaultFogColor(final float celestialAngle, final float partialTicks) {
//        return new Vec3d(0.09411766, 0.07529412, 0.09411766);
//    }

}
