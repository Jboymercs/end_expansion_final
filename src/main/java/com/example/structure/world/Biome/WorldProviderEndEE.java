package com.example.structure.world.Biome;

import com.example.structure.sky.EndSkyHandler;
import git.jbredwards.nether_api.mod.common.world.WorldProviderTheEnd;
import net.minecraft.world.WorldProviderEnd;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class WorldProviderEndEE extends WorldProviderTheEnd {

    public EndSkyHandler skyRenderer = new EndSkyHandler();

    public WorldProviderEndEE() {

    }


    @SideOnly(Side.CLIENT)
    @Override
    public boolean isSkyColored() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IRenderHandler getSkyRenderer() {
        System.out.println("Returning new Sky box");
        return new EndSkyHandler();
    }

}
