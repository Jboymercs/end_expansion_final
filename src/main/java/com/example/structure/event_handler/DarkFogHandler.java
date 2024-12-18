package com.example.structure.event_handler;

import com.example.structure.config.ModConfig;
import com.example.structure.event_handler.client.BarrendFogRenderer;
import com.example.structure.util.ModColors;
import com.example.structure.util.ModUtils;
import com.example.structure.world.Biome.BiomeAshWasteland;
import com.example.structure.world.Biome.BiomeBarrendLands;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Method;

@Mod.EventBusSubscriber(value = Side.CLIENT)
public class DarkFogHandler {

    public static float CLIFF_FOG_HEIGHT = 60f;
    public static final int SWAMP_FOG_LAYERS = 8;
    public static final int SWAMP_FOG_FADE_START = 5;
    private static final float CLOUD_FOG_HEIGHT = 90.25f;

    private static Method setupFog;
    private static net.minecraftforge.client.IRenderHandler swampFogRenderer = new BarrendFogRenderer();


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
            } else if (fpDis instanceof BiomeBarrendLands) {
                double posY = event.getEntity().getPositionEyes((float) event.getRenderPartialTicks()).y;
                if (posY < CLIFF_FOG_HEIGHT + SWAMP_FOG_LAYERS + SWAMP_FOG_FADE_START) {
                    double maxFogThickness = 0.07f;
                    double minFogThickness = 0.005f;
                   double distanceFromMax = posY - CLIFF_FOG_HEIGHT;
                    double closenessToMax = distanceFromMax / (SWAMP_FOG_LAYERS + SWAMP_FOG_FADE_START);
                    double fogThickness = maxFogThickness * MathHelper.clamp(1 - closenessToMax, 0, 1);
                    GlStateManager.setFog(GlStateManager.FogMode.EXP);
                   GlStateManager.setFogDensity((float) Math.max(fogThickness, minFogThickness));
                }
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

        if (entity instanceof EntityPlayer) {
            Vec3d originalColor = new Vec3d(event.getRed(), event.getGreen(), event.getBlue());
            Vec3d cloudColor = new Vec3d(0.3, 0, 0.145);
            Vec3d color = interpolateFogColor(event.getEntity(), originalColor, cloudColor.scale(Math.sqrt(originalColor.lengthSquared() / cloudColor.lengthSquared())), CLOUD_FOG_HEIGHT, 2);
            Vec3d color2 = interpolateFogColor(event.getEntity(), ModColors.SWAMP_FOG.scale(Math.sqrt(color.lengthSquared() / ModColors.SWAMP_FOG.lengthSquared())), color, CLIFF_FOG_HEIGHT, 1);
            int playerX = MathHelper.floor(entity.posX);
            int playerY = MathHelper.floor(entity.posY);
            int playerZ = MathHelper.floor(entity.posZ);
            Biome fpDis = world.getBiomeForCoordsBody(new BlockPos(playerX, playerY, playerZ));
            if(fpDis instanceof BiomeBarrendLands) {
                event.setRed((float) color2.x);
                event.setGreen((float) color2.y);
                event.setBlue((float) color2.z);
            }
        }

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

        if (!ModConfig.isDarkFogDisabled) {
            Minecraft mc = Minecraft.getMinecraft();
            int playerX = (int) Math.floor(mc.getRenderViewEntity().posX);
            int playerY = (int) Math.floor(mc.getRenderViewEntity().posY);
            int playerZ = (int) Math.floor(mc.getRenderViewEntity().posZ);
            if (mc.getRenderViewEntity().dimension == 1 && mc.getRenderViewEntity().world.getBiomeForCoordsBody(new BlockPos(playerX, playerY, playerZ)) instanceof BiomeBarrendLands) {
                if (setupFog == null) {
                    try {
                        setupFog = ReflectionHelper.findMethod(EntityRenderer.class, "setupFog", "func_78468_a", int.class, float.class);
                        setupFog.setAccessible(true);
                    } catch (Exception e) {
                       System.out.println("Failed to render fog: " + e);
                    }
                }

                if (setupFog != null) {
                    try {
                        if (mc.getRenderViewEntity().posY > CLIFF_FOG_HEIGHT) {
                            setupFog.invoke(mc.entityRenderer, 0, event.getPartialTicks());
                            swampFogRenderer.render(event.getPartialTicks(), Minecraft.getMinecraft().world, Minecraft.getMinecraft());
                            GlStateManager.disableFog();
                        }
                    } catch (Exception e) {
                        System.out.println("Failed to render fog: " + e);
                        GlStateManager.disableFog();
                    }
                }


            }


        }


    }

}
