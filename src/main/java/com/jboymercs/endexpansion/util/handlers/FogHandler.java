package com.jboymercs.endexpansion.util.handlers;

import com.jboymercs.endexpansion.util.IBiomeMisty;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

public class FogHandler {
    private static final int MIST_DISTANCE = 20;
    private static final int WATER_BLEND_DISTANCE = 2;
    private static final int MIST_CACHE_SIZE = (MIST_DISTANCE * 2 + 1) * (MIST_DISTANCE * 2 + 1);

    private static final BlockPos.MutableBlockPos MUTABLE_POS = new BlockPos.MutableBlockPos();
    private static final boolean[] MIST_ACTIVE = new boolean[MIST_CACHE_SIZE];
    private static final float[] MIST_DENSITY = new float[MIST_CACHE_SIZE];

    private static World mistWorld;
    private static int mistX;
    private static int mistY;
    private static int mistZ;
    private static boolean mistInit;

    private static World mistColourWorld;
    private static int mistColourX;
    private static int mistColourY;
    private static int mistColourZ;
    private static int mistColourDistance = -1;
    private static boolean mistColourInit;
    private static boolean mistColourHasActive;
    private static boolean[] mistColourActive = new boolean[0];
    private static int[] cachedMistColours = new int[0];

    private static boolean mistyBiomeRegistryChecked;
    private static boolean mistyBiomesPresent;

    public FogHandler(){

    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onGetMistColour(EntityViewRenderEvent.FogColors event) {
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            World world = player.world;
            int x = MathHelper.floor(player.posX);
            int y = MathHelper.floor(player.posY);
            int z = MathHelper.floor(player.posZ);
            IBlockState blockStateAtEyes = ActiveRenderInfo.getBlockStateAtEntityViewpoint(world, event.getEntity(), (float) event.getRenderPartialTicks());
            if (blockStateAtEyes.getMaterial() == Material.LAVA) {
                return;
            }

            Vec3d mixedColor;
            if (blockStateAtEyes.getMaterial() == Material.WATER) {
                mixedColor = getMistBlendColorWater(world, player, x, y, z, event.getRenderPartialTicks());
            } else {
                if (!hasMistyBiomes()) {
                    return;
                }

                mixedColor = getMistBlendColour(world, player, x, y, z, event.getRed(), event.getGreen(), event.getBlue(), event.getRenderPartialTicks());
            }
            event.setRed((float) mixedColor.x);
            event.setGreen((float) mixedColor.y);
            event.setBlue((float) mixedColor.z);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderMist(EntityViewRenderEvent.RenderFogEvent event) {
        Entity entity = event.getEntity();
        World world = entity.world;
        int playerX = MathHelper.floor(entity.posX);
        int playerY = MathHelper.floor(entity.posY);
        int playerZ = MathHelper.floor(entity.posZ);
        if (!hasMistyBiomes()) {
            mistInit = false;
            return;
        }

        if (!mistInit || mistWorld != world || playerX != mistX || playerY != mistY || playerZ != mistZ) {
            updateMistCache(world, playerX, playerY, playerZ);
        }

        float fpDistanceBiomeMist = 0.0F;
        float weightBiomeMist = 0.0F;
        int index = 0;
        for (int weightMixed = -MIST_DISTANCE; weightMixed <= MIST_DISTANCE; ++weightMixed) {
            for (int weightDefault = -MIST_DISTANCE; weightDefault <= MIST_DISTANCE; ++weightDefault) {
                if (MIST_ACTIVE[index]) {
                    float farPlaneDistance = MIST_DENSITY[index];
                    float farPlaneDistanceScaleBiome = 1.0F;
                    double farPlaneDistanceScale;
                    if (weightMixed == -MIST_DISTANCE) {
                        farPlaneDistanceScale = 1.0D - (entity.posX - (double) playerX);
                        farPlaneDistance = (float) ((double) farPlaneDistance * farPlaneDistanceScale);
                        farPlaneDistanceScaleBiome = (float) ((double) farPlaneDistanceScaleBiome * farPlaneDistanceScale);
                    } else if (weightMixed == MIST_DISTANCE) {
                        farPlaneDistanceScale = entity.posX - (double) playerX;
                        farPlaneDistance = (float) ((double) farPlaneDistance * farPlaneDistanceScale);
                        farPlaneDistanceScaleBiome = (float) ((double) farPlaneDistanceScaleBiome * farPlaneDistanceScale);
                    }

                    if (weightDefault == -MIST_DISTANCE) {
                        farPlaneDistanceScale = 1.0D - (entity.posZ - (double) playerZ);
                        farPlaneDistance = (float) ((double) farPlaneDistance * farPlaneDistanceScale);
                        farPlaneDistanceScaleBiome = (float) ((double) farPlaneDistanceScaleBiome * farPlaneDistanceScale);
                    } else if (weightDefault == MIST_DISTANCE) {
                        farPlaneDistanceScale = entity.posZ - (double) playerZ;
                        farPlaneDistance = (float) ((double) farPlaneDistance * farPlaneDistanceScale);
                        farPlaneDistanceScaleBiome = (float) ((double) farPlaneDistanceScaleBiome * farPlaneDistanceScale);
                    }

                    fpDistanceBiomeMist += farPlaneDistance;
                    weightBiomeMist += farPlaneDistanceScaleBiome;
                }

                ++index;
            }
        }

        float var17 = (float) (MIST_DISTANCE * 2 * MIST_DISTANCE * 2);
        float var18 = var17 - weightBiomeMist;
        float var19 = weightBiomeMist == 0.0F ? 0.0F : fpDistanceBiomeMist / weightBiomeMist;
        float farPlaneDistance = (fpDistanceBiomeMist * 240.0F + event.getFarPlaneDistance() * var18) / var17;
        float farPlaneDistanceScaleBiome = 0.1F * (1.0F - var19) + 0.75F * var19;
        float var20 = (farPlaneDistanceScaleBiome * weightBiomeMist + 0.75F * var18) / var17;
        renderMist(event.getFogMode(), Math.min(farPlaneDistance, event.getFarPlaneDistance()), var20);
    }

    private static int getMistColourDistance() {
        GameSettings settings = Minecraft.getMinecraft().gameSettings;
        int[] ranges = ForgeModContainer.blendRanges;
        return settings.fancyGraphics && settings.renderDistanceChunks >= 0 && settings.renderDistanceChunks < ranges.length ? ranges[settings.renderDistanceChunks] : 0;
    }

    private static boolean hasMistyBiomes() {
        if (!mistyBiomeRegistryChecked) {
            mistyBiomeRegistryChecked = true;
            for (Biome biome : ForgeRegistries.BIOMES.getValuesCollection()) {
                if (biome instanceof IBiomeMisty) {
                    mistyBiomesPresent = true;
                    break;
                }
            }
        }

        return mistyBiomesPresent;
    }

    private static Biome getBiome(World world, int x, int y, int z) {
        MUTABLE_POS.setPos(x, y, z);
        return world.getBiomeForCoordsBody(MUTABLE_POS);
    }

    private static void updateMistCache(World world, int playerX, int playerY, int playerZ) {
        mistInit = true;
        mistWorld = world;
        mistX = playerX;
        mistY = playerY;
        mistZ = playerZ;

        int index = 0;
        for (int weightMixed = -MIST_DISTANCE; weightMixed <= MIST_DISTANCE; ++weightMixed) {
            for (int weightDefault = -MIST_DISTANCE; weightDefault <= MIST_DISTANCE; ++weightDefault) {
                Biome biome = getBiome(world, playerX + weightMixed, playerZ + weightDefault, playerY + weightDefault);
                if (biome instanceof IBiomeMisty) {
                    MIST_ACTIVE[index] = true;
                    MIST_DENSITY[index] = ((IBiomeMisty) biome).getMistDensity(playerX + weightMixed, playerY, playerZ + weightDefault);
                } else {
                    MIST_ACTIVE[index] = false;
                    MIST_DENSITY[index] = 0.0F;
                }

                ++index;
            }
        }
    }

    private static void renderMist(int MistMode, float farPlaneDistance, float farPlaneDistanceScale) {
        if (MistMode < 0) {
            GL11.glFogf(2915, 0.0F);
            GL11.glFogf(2916, farPlaneDistance);
        } else {
            GL11.glFogf(2915, farPlaneDistance * farPlaneDistanceScale);
            GL11.glFogf(2916, farPlaneDistance);
        }
    }

    private static Vec3d postProcessColor(World world, EntityLivingBase player, double r, double g, double b, double renderPartialTicks) {
        double darkScale = (player.lastTickPosY + (player.posY - player.lastTickPosY) * renderPartialTicks) * world.provider.getVoidFogYFactor();
        int aR;
        if (player.isPotionActive(Potion.getPotionById(15))) {
            aR = player.getActivePotionEffect(Potion.getPotionById(15)).getDuration();
            darkScale *= aR < 20 ? (double)(1.0F - (float)aR / 20.0F) : 0.0;
        }

        if (darkScale < 1.0) {
            darkScale = darkScale < 0.0 ? 0.0 : darkScale * darkScale;
            r = (float)((double)r * darkScale);
            g = (float)((double)g * darkScale);
            b = (float)((double)b * darkScale);
        }

        double aG;
        double aB;
        if (player.isPotionActive(Potion.getPotionById(16))) {
            aR = player.getActivePotionEffect(Potion.getPotionById(16)).getDuration();
            aG = aR > 200 ? 1.0F : 0.7F + MathHelper.sin((float)(((double)aR - renderPartialTicks) * Math.PI * 0.20000000298023224)) * 0.3F;
            aB = 1.0F / r;
            aB = Math.min(aB, 1.0F / g);
            aB = Math.min(aB, 1.0F / b);
            r = r * (1.0F - aG) + r * aB * aG;
            g = g * (1.0F - aG) + g * aB * aG;
            b = b * (1.0F - aG) + b * aB * aG;
        }

        if (Minecraft.getMinecraft().gameSettings.anaglyph) {
            float aR1 = (float) ((r * 30.0F + g * 59.0F + b * 11.0F) / 100.0F);
            aG = (r * 30.0F + g * 70.0F) / 100.0F;
            aB = (r * 30.0F + b * 70.0F) / 100.0F;
            r = aR1;
            g = aG;
            b = aB;
        }

        return new Vec3d((double)r, (double)g, (double)b);
    }

    private static Vec3d getMistBlendColorWater(World world, EntityLivingBase playerEntity, int playerX, int playerY, int playerZ, double renderPartialTicks) {
        float rBiomeMist = 0.0F;
        float gBiomeMist = 0.0F;
        float bBiomeMist = 0.0F;

        float bMixed;
        for (int weight = -WATER_BLEND_DISTANCE; weight <= WATER_BLEND_DISTANCE; ++weight) {
            for (int respirationLevel = -WATER_BLEND_DISTANCE; respirationLevel <= WATER_BLEND_DISTANCE; ++respirationLevel) {
                Biome rMixed = getBiome(world, playerX + weight, playerY + weight, playerZ + respirationLevel);
                int gMixed = rMixed.getWaterColorMultiplier();
                bMixed = (float) ((gMixed & 16711680) >> 16);
                float gPart = (float) ((gMixed & '\uff00') >> 8);
                float bPart = (float) (gMixed & 255);
                double zDiff;
                if (weight == -WATER_BLEND_DISTANCE) {
                    zDiff = 1.0D - (playerEntity.posX - (double) playerX);
                    bMixed = (float) ((double) bMixed * zDiff);
                    gPart = (float) ((double) gPart * zDiff);
                    bPart = (float) ((double) bPart * zDiff);
                } else if (weight == WATER_BLEND_DISTANCE) {
                    zDiff = playerEntity.posX - (double) playerX;
                    bMixed = (float) ((double) bMixed * zDiff);
                    gPart = (float) ((double) gPart * zDiff);
                    bPart = (float) ((double) bPart * zDiff);
                }

                if (respirationLevel == -WATER_BLEND_DISTANCE) {
                    zDiff = 1.0D - (playerEntity.posZ - (double) playerZ);
                    bMixed = (float) ((double) bMixed * zDiff);
                    gPart = (float) ((double) gPart * zDiff);
                    bPart = (float) ((double) bPart * zDiff);
                } else if (respirationLevel == WATER_BLEND_DISTANCE) {
                    zDiff = playerEntity.posZ - (double) playerZ;
                    bMixed = (float) ((double) bMixed * zDiff);
                    gPart = (float) ((double) gPart * zDiff);
                    bPart = (float) ((double) bPart * zDiff);
                }

                rBiomeMist += bMixed;
                gBiomeMist += gPart;
                bBiomeMist += bPart;
            }
        }

        rBiomeMist /= 255.0F;
        gBiomeMist /= 255.0F;
        bBiomeMist /= 255.0F;
        float var20 = (float) (WATER_BLEND_DISTANCE * 2 * WATER_BLEND_DISTANCE * 2);
        float var21 = (float) EnchantmentHelper.getRespirationModifier(playerEntity) * 0.2F;
        float var22 = (rBiomeMist * 0.02F + var21) / var20;
        float var23 = (gBiomeMist * 0.02F + var21) / var20;
        bMixed = (bBiomeMist * 0.2F + var21) / var20;
        return postProcessColor(world, playerEntity, var22, var23, bMixed, renderPartialTicks);
    }

    private static Vec3d getMistBlendColour(World world, EntityLivingBase playerEntity, int playerX, int playerY, int playerZ, float defR, float defG, float defB, double renderPartialTicks) {
        int distance = getMistColourDistance();

        if (!mistColourInit || mistColourWorld != world || playerX != mistColourX || playerY != mistColourY || playerZ != mistColourZ || distance != mistColourDistance) {
            updateMistColourCache(world, playerX, playerY, playerZ, distance);
        }

        if (!mistColourHasActive) {
            return new Vec3d((double) defR, (double) defG, (double) defB);
        }

        float rBiomeMist = 0.0F;
        float gBiomeMist = 0.0F;
        float bBiomeMist = 0.0F;
        float weightBiomeMist = 0.0F;
        float rainStrength;
        float thunderStrength;
        float weightMixed;
        int index = 0;
        for(int celestialAngle = -distance; celestialAngle <= distance; ++celestialAngle) {
            for(int baseScale = -distance; baseScale <= distance; ++baseScale) {
                if (mistColourActive[index]) {
                    int bScale = cachedMistColours[index];
                    rainStrength = (float)(bScale >> 16);
                    thunderStrength = (float)(bScale >> 8);
                    float processedColor = (float)bScale;
                    weightMixed = 1.0F;
                    double weightDefault;
                    if (celestialAngle == -distance) {
                        weightDefault = 1.0 - (playerEntity.posX - (double)playerX);
                        rainStrength = (float)((double)rainStrength * weightDefault);
                        thunderStrength = (float)((double)thunderStrength * weightDefault);
                        processedColor = (float)((double)processedColor * weightDefault);
                        weightMixed = (float)((double)weightMixed * weightDefault);
                    } else if (celestialAngle == distance) {
                        weightDefault = playerEntity.posX - (double)playerX;
                        rainStrength = (float)((double)rainStrength * weightDefault);
                        thunderStrength = (float)((double)thunderStrength * weightDefault);
                        processedColor = (float)((double)processedColor * weightDefault);
                        weightMixed = (float)((double)weightMixed * weightDefault);
                    }

                    if (baseScale == -distance) {
                        weightDefault = 1.0 - (playerEntity.posZ - (double)playerZ);
                        rainStrength = (float)((double)rainStrength * weightDefault);
                        thunderStrength = (float)((double)thunderStrength * weightDefault);
                        processedColor = (float)((double)processedColor * weightDefault);
                        weightMixed = (float)((double)weightMixed * weightDefault);
                    } else if (baseScale == distance) {
                        weightDefault = playerEntity.posZ - (double)playerZ;
                        rainStrength = (float)((double)rainStrength * weightDefault);
                        thunderStrength = (float)((double)thunderStrength * weightDefault);
                        processedColor = (float)((double)processedColor * weightDefault);
                        weightMixed = (float)((double)weightMixed * weightDefault);
                    }

                    rBiomeMist += rainStrength;
                    gBiomeMist += thunderStrength;
                    bBiomeMist += processedColor;
                    weightBiomeMist += weightMixed;
                }

                ++index;
            }
        }

        rBiomeMist /= 255.0F;
        gBiomeMist /= 255.0F;
        bBiomeMist /= 255.0F;
        float var31 = world.getCelestialAngle((float)renderPartialTicks);
        float var32 = MathHelper.clamp(MathHelper.cos(var31 * 3.1415927F * 2.0F) * 2.0F + 0.5F, 0.0F, 1.0F);
        float var33 = var32 * 0.94F + 0.06F;
        float var34 = var32 * 0.94F + 0.06F;
        float var28 = var32 * 0.91F + 0.09F;
        rainStrength = world.getRainStrength((float)renderPartialTicks);
        if (rainStrength > 0.0F) {
            var33 *= 1.0F - rainStrength * 0.5F;
            var34 *= 1.0F - rainStrength * 0.5F;
            var28 *= 1.0F - rainStrength * 0.4F;
        }

        thunderStrength = world.getThunderStrength((float)renderPartialTicks);
        if (thunderStrength > 0.0F) {
            var33 *= 1.0F - thunderStrength * 0.5F;
            var34 *= 1.0F - thunderStrength * 0.5F;
            var28 *= 1.0F - thunderStrength * 0.5F;
        }

        rBiomeMist *= var33 / weightBiomeMist;
        gBiomeMist *= var34 / weightBiomeMist;
        bBiomeMist *= var28 / weightBiomeMist;
        Vec3d var29 = postProcessColor(world, playerEntity, rBiomeMist, gBiomeMist, bBiomeMist, renderPartialTicks);
        rBiomeMist = (float)var29.x;
        gBiomeMist = (float)var29.y;
        bBiomeMist = (float)var29.z;
        weightMixed = (float)(distance * 2 * distance * 2);
        float var30 = weightMixed - weightBiomeMist;
        return new Vec3d((double)((rBiomeMist * weightBiomeMist + defR * var30) / weightMixed),
                (double)((gBiomeMist * weightBiomeMist + defG * var30) / weightMixed),
                (double)((bBiomeMist * weightBiomeMist + defB * var30) / weightMixed));
    }

    private static void updateMistColourCache(World world, int playerX, int playerY, int playerZ, int distance) {
        mistColourInit = true;
        mistColourWorld = world;
        mistColourX = playerX;
        mistColourY = playerY;
        mistColourZ = playerZ;
        mistColourDistance = distance;
        mistColourHasActive = false;

        int cacheSize = (distance * 2 + 1) * (distance * 2 + 1);
        if (mistColourActive.length != cacheSize) {
            mistColourActive = new boolean[cacheSize];
            cachedMistColours = new int[cacheSize];
        }

        int index = 0;
        for(int celestialAngle = -distance; celestialAngle <= distance; ++celestialAngle) {
            for(int baseScale = -distance; baseScale <= distance; ++baseScale) {
                Biome rScale = getBiome(world, playerX + celestialAngle, playerZ + baseScale, playerZ);
                if (rScale instanceof IBiomeMisty) {
                    IBiomeMisty gScale = (IBiomeMisty)rScale;
                    mistColourActive[index] = true;
                    cachedMistColours[index] = gScale.getMistColour(playerX + celestialAngle, playerY, playerZ + baseScale);
                    mistColourHasActive = true;
                } else {
                    mistColourActive[index] = false;
                    cachedMistColours[index] = 0;
                }

                ++index;
            }
        }
    }
}
