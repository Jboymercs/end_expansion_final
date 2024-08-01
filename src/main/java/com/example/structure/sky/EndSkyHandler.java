package com.example.structure.sky;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EndSkyHandler extends IRenderHandler {

    @Override
    public void render(float partialTicks, WorldClient world, Minecraft mc) {
        RenderCustomEndSky renderer = new RenderCustomEndSky(mc, world);
        System.out.println("Calling SkyBox From Handler");
        renderer.renderFlatSky(mc, false);
    }
}
