package com.example.structure.util.handlers;

import com.example.structure.util.IBiomeMisty;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
import net.minecraftforge.client.event.EntityViewRenderEvent.RenderFogEvent;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

public class FogHandler {
    private static double MistX;
    private static double MistZ;
    private static boolean MistInit;
    private static float MistFarPlaneDistance;

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
        if ((double) playerX == MistX && (double) playerZ == MistZ && MistInit) {
            renderMist(event.getFogMode(), MistFarPlaneDistance, 0.75F);
        } else {
            MistInit = true;
            byte distance = 20;
            float fpDistanceBiomeMist = 0.0F;
            float weightBiomeMist = 0.0F;

            float farPlaneDistance;
            float farPlaneDistanceScaleBiome;
            for (int weightMixed = -distance; weightMixed <= distance; ++weightMixed) {
                for (int weightDefault = -distance; weightDefault <= distance; ++weightDefault) {
                    Biome fpDistanceBiomeMistAvg = world.getBiomeForCoordsBody(new BlockPos(playerX + weightMixed, playerZ + weightDefault, playerY + weightDefault));
                    if (fpDistanceBiomeMistAvg instanceof IBiomeMisty) {
                        farPlaneDistance = ((IBiomeMisty) fpDistanceBiomeMistAvg).getMistDensity(playerX + weightMixed, playerY, playerZ + weightDefault);
                        farPlaneDistanceScaleBiome = 1.0F;
                        double farPlaneDistanceScale;
                        if (weightMixed == -distance) {
                            farPlaneDistanceScale = 1.0D - (entity.posX - (double) playerX);
                            farPlaneDistance = (float) ((double) farPlaneDistance * farPlaneDistanceScale);
                            farPlaneDistanceScaleBiome = (float) ((double) farPlaneDistanceScaleBiome * farPlaneDistanceScale);
                        } else if (weightMixed == distance) {
                            farPlaneDistanceScale = entity.posX - (double) playerX;
                            farPlaneDistance = (float) ((double) farPlaneDistance * farPlaneDistanceScale);
                            farPlaneDistanceScaleBiome = (float) ((double) farPlaneDistanceScaleBiome * farPlaneDistanceScale);
                        }

                        if (weightDefault == -distance) {
                            farPlaneDistanceScale = 1.0D - (entity.posZ - (double) playerZ);
                            farPlaneDistance = (float) ((double) farPlaneDistance * farPlaneDistanceScale);
                            farPlaneDistanceScaleBiome = (float) ((double) farPlaneDistanceScaleBiome * farPlaneDistanceScale);
                        } else if (weightDefault == distance) {
                            farPlaneDistanceScale = entity.posZ - (double) playerZ;
                            farPlaneDistance = (float) ((double) farPlaneDistance * farPlaneDistanceScale);
                            farPlaneDistanceScaleBiome = (float) ((double) farPlaneDistanceScaleBiome * farPlaneDistanceScale);
                        }

                        fpDistanceBiomeMist += farPlaneDistance;
                        weightBiomeMist += farPlaneDistanceScaleBiome;
                    }
                }
            }

            float var17 = (float) (distance * 2 * distance * 2);
            float var18 = var17 - weightBiomeMist;
            float var19 = weightBiomeMist == 0.0F ? 0.0F : fpDistanceBiomeMist / weightBiomeMist;
            farPlaneDistance = (fpDistanceBiomeMist * 240.0F + event.getFarPlaneDistance() * var18) / var17;
            farPlaneDistanceScaleBiome = 0.1F * (1.0F - var19) + 0.75F * var19;
            float var20 = (farPlaneDistanceScaleBiome * weightBiomeMist + 0.75F * var18) / var17;
            MistX = entity.posX;
            MistZ = entity.posZ;
            MistFarPlaneDistance = Math.min(farPlaneDistance, event.getFarPlaneDistance());
            renderMist(event.getFogMode(), MistFarPlaneDistance, var20);
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
        byte distance = 2;
        float rBiomeMist = 0.0F;
        float gBiomeMist = 0.0F;
        float bBiomeMist = 0.0F;

        float bMixed;
        for (int weight = -distance; weight <= distance; ++weight) {
            for (int respirationLevel = -distance; respirationLevel <= distance; ++respirationLevel) {
                Biome rMixed = world.getBiomeForCoordsBody(new BlockPos(playerX + weight, playerY + weight, playerZ + respirationLevel));
                int gMixed = rMixed.getWaterColorMultiplier();
                bMixed = (float) ((gMixed & 16711680) >> 16);
                float gPart = (float) ((gMixed & '\uff00') >> 8);
                float bPart = (float) (gMixed & 255);
                double zDiff;
                if (weight == -distance) {
                    zDiff = 1.0D - (playerEntity.posX - (double) playerX);
                    bMixed = (float) ((double) bMixed * zDiff);
                    gPart = (float) ((double) gPart * zDiff);
                    bPart = (float) ((double) bPart * zDiff);
                } else if (weight == distance) {
                    zDiff = playerEntity.posX - (double) playerX;
                    bMixed = (float) ((double) bMixed * zDiff);
                    gPart = (float) ((double) gPart * zDiff);
                    bPart = (float) ((double) bPart * zDiff);
                }

                if (respirationLevel == -distance) {
                    zDiff = 1.0D - (playerEntity.posZ - (double) playerZ);
                    bMixed = (float) ((double) bMixed * zDiff);
                    gPart = (float) ((double) gPart * zDiff);
                    bPart = (float) ((double) bPart * zDiff);
                } else if (respirationLevel == distance) {
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
        float var20 = (float) (distance * 2 * distance * 2);
        float var21 = (float) EnchantmentHelper.getRespirationModifier(playerEntity) * 0.2F;
        float var22 = (rBiomeMist * 0.02F + var21) / var20;
        float var23 = (gBiomeMist * 0.02F + var21) / var20;
        bMixed = (bBiomeMist * 0.2F + var21) / var20;
        return postProcessColor(world, playerEntity, var22, var23, bMixed, renderPartialTicks);
    }

    private static Vec3d getMistBlendColour(World world, EntityLivingBase playerEntity, int playerX, int playerY, int playerZ, float defR, float defG, float defB, double renderPartialTicks) {
        GameSettings settings = Minecraft.getMinecraft().gameSettings;
        int[] ranges = ForgeModContainer.blendRanges;
        int distance = 0;
        if (settings.fancyGraphics && settings.renderDistanceChunks >= 0 && settings.renderDistanceChunks < ranges.length) {
            distance = ranges[settings.renderDistanceChunks];
        }

        float rBiomeMist = 0.0F;
        float gBiomeMist = 0.0F;
        float bBiomeMist = 0.0F;
        float weightBiomeMist = 0.0F;

        float rainStrength;
        float thunderStrength;
        float weightMixed;
        for(int celestialAngle = -distance; celestialAngle <= distance; ++celestialAngle) {
            for(int baseScale = -distance; baseScale <= distance; ++baseScale) {
                Biome rScale = world.getBiomeForCoordsBody(new BlockPos(playerX + celestialAngle, playerZ + baseScale, playerZ));
                if (rScale instanceof IBiomeMisty) {
                    IBiomeMisty gScale = (IBiomeMisty)rScale;
                    int bScale = gScale.getMistColour(playerX + celestialAngle, playerY, playerZ + baseScale);
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
            }
        }

        if (weightBiomeMist == 0.0F) {
            return new Vec3d((double)defR, (double)defG, (double)defB);
        } else {
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
            var29.x = (double)((rBiomeMist * weightBiomeMist + defR * var30) / weightMixed);
            var29.y = (double)((gBiomeMist * weightBiomeMist + defG * var30) / weightMixed);
            var29.z = (double)((bBiomeMist * weightBiomeMist + defB * var30) / weightMixed);
            return var29;
        }
    }
}
