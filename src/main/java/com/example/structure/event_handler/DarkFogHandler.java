package com.example.structure.event_handler;

import com.example.structure.config.ModConfig;
import com.example.structure.sky.EndSkyHandler;
import com.example.structure.util.ModUtils;
import com.example.structure.world.Biome.BiomeAshWasteland;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEnd;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Method;

@Mod.EventBusSubscriber(value = Side.CLIENT)
public class DarkFogHandler {

    public static float CLIFF_FOG_HEIGHT = 45.55f;
    public static final int SWAMP_FOG_LAYERS = 8;
    public static final int SWAMP_FOG_FADE_START = 5;
    private static final float CLOUD_FOG_HEIGHT = 239.25f;
    private static final Biome ashWastes = new BiomeAshWasteland();


    private static Method setupFog;
    private final EndSkyHandler skyHandler = new EndSkyHandler();
    /**
     * Altering the fog density through the render fog event because the fog density
     * event is a pain because you have to override it for some reason
     */
    @SideOnly(Side.CLIENT)
    @SubscribeEvent()
    public static void onFogDensityRender(EntityViewRenderEvent.RenderFogEvent event) {
        Entity entity = event.getEntity();
        World world = entity.world;
        if(entity instanceof EntityPlayer && !ModConfig.isDarkFogDisabled) {
            int playerX = MathHelper.floor(entity.posX);
            int playerY = MathHelper.floor(entity.posY);
            int playerZ = MathHelper.floor(entity.posZ);
            Biome fpDis = world.getBiomeForCoordsBody(new BlockPos(playerX, playerY, playerZ));
            if (fpDis instanceof BiomeAshWasteland) {
                GlStateManager.setFog(GlStateManager.FogMode.EXP);
                GlStateManager.setFogDensity(ModConfig.dark_fog_variable);
            }
        }

    }


    private static Vec3d interpolateFogColor(Entity renderEntity, Vec3d fog1, Vec3d fog2, float transitionStart, float transitionLength) {
        float alpha = ModUtils.clamp((renderEntity.posY - transitionStart) / transitionLength, 0, 1);
        return fog1.scale(1 - alpha).add(fog2.scale(alpha));
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent()
    public static void onFogColor(EntityViewRenderEvent.FogColors event) {

        Entity entity = event.getEntity();
        World world = entity.world;
        if(entity instanceof EntityPlayer) {
            int playerX = MathHelper.floor(entity.posX);
            int playerY = MathHelper.floor(entity.posY);
            int playerZ = MathHelper.floor(entity.posZ);
            Biome fpDis = world.getBiomeForCoordsBody(new BlockPos(playerX, playerY, playerZ));
            if (fpDis instanceof BiomeAshWasteland) {
                event.setBlue(0);
                event.setRed(0);
                event.setGreen(0);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent()
    public static void onRenderWorldLastEvent(RenderWorldLastEvent event) {

            Minecraft mc = Minecraft.getMinecraft();


    }

}
