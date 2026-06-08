package com.jboymercs.endexpansion.sky;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.client.IRenderHandler;


public class EndSkyHandler extends IRenderHandler {

    @Override
    public void render(float partialTicks, WorldClient world, Minecraft mc) {
        RenderCustomEndSky renderer = new RenderCustomEndSky(mc, world);
        renderer.renderFlatSky(mc, false);
    }
}
