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

    private static final double CLOUD_FOG_RED = 0.3D;
    private static final double CLOUD_FOG_GREEN = 0D;
    private static final double CLOUD_FOG_BLUE = 0.145D;
    private static final double CLOUD_FOG_LENGTH_SQ = CLOUD_FOG_RED * CLOUD_FOG_RED + CLOUD_FOG_GREEN * CLOUD_FOG_GREEN + CLOUD_FOG_BLUE * CLOUD_FOG_BLUE;

    private static Method setupFog;
    private static final net.minecraftforge.client.IRenderHandler swampFogRenderer = new BarrendFogRenderer();
    private static final BlockPos.MutableBlockPos biomePos = new BlockPos.MutableBlockPos();
    private static World cachedBiomeWorld;
    private static int cachedBiomeDimension = Integer.MIN_VALUE;
    private static int cachedBiomeX = Integer.MIN_VALUE;
    private static int cachedBiomeY = Integer.MIN_VALUE;
    private static int cachedBiomeZ = Integer.MIN_VALUE;
    private static Biome cachedBiome;


    /**
     * Altering the fog density through the render fog event because the fog density
     * event is a pain because you have to override it for some reason
     */
    @SideOnly(Side.CLIENT)
    @SubscribeEvent()
    public static void onFogDensityRender(EntityViewRenderEvent.RenderFogEvent event) {
        Entity entity = event.getEntity();
        if(!(entity instanceof EntityPlayer) || ModConfig.isDarkFogDisabled) {
            return;
        }

        Biome fpDis = getCachedBiome(entity);
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

    @SideOnly(Side.CLIENT)
    @SubscribeEvent()
    public static void onFogColor(EntityViewRenderEvent.FogColors event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof EntityPlayer)) {
            return;
        }

        Biome fpDis = getCachedBiome(entity);
        if (fpDis instanceof BiomeAshWasteland) {
            event.setBlue(0);
            event.setRed(0);
            event.setGreen(0);
        } else if(fpDis instanceof BiomeBarrendLands) {
            applyBarrendFogColor(event, entity);
        }
    }

    private static void applyBarrendFogColor(EntityViewRenderEvent.FogColors event, Entity entity) {
        double originalRed = event.getRed();
        double originalGreen = event.getGreen();
        double originalBlue = event.getBlue();
        double originalLengthSq = originalRed * originalRed + originalGreen * originalGreen + originalBlue * originalBlue;
        double cloudScale = Math.sqrt(originalLengthSq / CLOUD_FOG_LENGTH_SQ);
        double cloudRed = CLOUD_FOG_RED * cloudScale;
        double cloudGreen = CLOUD_FOG_GREEN * cloudScale;
        double cloudBlue = CLOUD_FOG_BLUE * cloudScale;

        float cloudAlpha = ModUtils.clamp((entity.posY - CLOUD_FOG_HEIGHT) / 2, 0, 1);
        double colorRed = interpolate(originalRed, cloudRed, cloudAlpha);
        double colorGreen = interpolate(originalGreen, cloudGreen, cloudAlpha);
        double colorBlue = interpolate(originalBlue, cloudBlue, cloudAlpha);

        double colorLengthSq = colorRed * colorRed + colorGreen * colorGreen + colorBlue * colorBlue;
        double swampScale = Math.sqrt(colorLengthSq / ModColors.SWAMP_FOG.lengthSquared());
        double swampRed = ModColors.SWAMP_FOG.x * swampScale;
        double swampGreen = ModColors.SWAMP_FOG.y * swampScale;
        double swampBlue = ModColors.SWAMP_FOG.z * swampScale;

        float swampAlpha = ModUtils.clamp(entity.posY - CLIFF_FOG_HEIGHT, 0, 1);
        event.setRed((float) interpolate(swampRed, colorRed, swampAlpha));
        event.setGreen((float) interpolate(swampGreen, colorGreen, swampAlpha));
        event.setBlue((float) interpolate(swampBlue, colorBlue, swampAlpha));
    }

    private static double interpolate(double fog1, double fog2, float alpha) {
        return fog1 * (1 - alpha) + fog2 * alpha;
    }

    private static Biome getCachedBiome(Entity entity) {
        int playerX = MathHelper.floor(entity.posX);
        int playerY = MathHelper.floor(entity.posY);
        int playerZ = MathHelper.floor(entity.posZ);
        World world = entity.world;
        if (world != cachedBiomeWorld || entity.dimension != cachedBiomeDimension || playerX != cachedBiomeX || playerY != cachedBiomeY || playerZ != cachedBiomeZ) {
            cachedBiomeWorld = world;
            cachedBiomeDimension = entity.dimension;
            cachedBiomeX = playerX;
            cachedBiomeY = playerY;
            cachedBiomeZ = playerZ;
            cachedBiome = world.getBiomeForCoordsBody(biomePos.setPos(playerX, playerY, playerZ));
        }
        return cachedBiome;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent()
    public static void onRenderWorldLastEvent(RenderWorldLastEvent event) {

        if (!ModConfig.isDarkFogDisabled) {
            Minecraft mc = Minecraft.getMinecraft();
            Entity renderViewEntity = mc.getRenderViewEntity();
            if (renderViewEntity != null && renderViewEntity.dimension == 1 && getCachedBiome(renderViewEntity) instanceof BiomeBarrendLands) {
                if (setupFog == null) {
                    try {
                        setupFog = ReflectionHelper.findMethod(EntityRenderer.class, "setupFog", "func_78468_a", int.class, float.class);
                        setupFog.setAccessible(true);
                    } catch (Exception e) {
                    }
                }

                if (setupFog != null) {
                    try {
                        if (renderViewEntity.posY > CLIFF_FOG_HEIGHT) {
                            setupFog.invoke(mc.entityRenderer, 0, event.getPartialTicks());
                            swampFogRenderer.render(event.getPartialTicks(), mc.world, mc);
                            GlStateManager.disableFog();
                        }
                    } catch (Exception e) {
                        GlStateManager.disableFog();
                    }
                }


            }


        }


    }

}
