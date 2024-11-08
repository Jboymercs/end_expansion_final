package com.example.structure.util;


import com.example.structure.Main;
import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityEnderKnight;
import com.example.structure.entity.Projectile;
import com.example.structure.entity.knighthouse.EntityEnderMage;
import com.example.structure.entity.tileentity.MobSpawnerLogic;
import com.example.structure.init.ModBlocks;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ModUtils {
    //VERY IMPORTANT

    public static final String LANG_DESC = ModReference.MOD_ID + ".desc.";

    public static final String LANG_CHAT = ModReference.MOD_ID + ".dialog.";

    public static byte PARTICLE_BYTE = 12;
    public static byte SECOND_PARTICLE_BYTE = 14;
    public static byte THIRD_PARTICLE_BYTE = 15;
    public static byte FOURTH_PARTICLE_BYTE = 16;

    public static byte SCREEN_SHAKE_BYTE = 17;
    public static Vec3d Y_AXIS = new Vec3d(0, 1, 0);

    public static Vec3d Z_AXIS = new Vec3d(0, 0, 1);
    public static final ResourceLocation PARTICLE = new ResourceLocation(ModReference.MOD_ID + ":textures/particle/particles.png");

    public static int getAverageGroundHeight(World world, int x, int z, int sizeX, int sizeZ, int maxVariation) {
        sizeX = x + sizeX;
        sizeZ = z + sizeZ;
        int corner1 = calculateGenerationHeight(world, x, z);
        int corner2 = calculateGenerationHeight(world, sizeX, z);
        int corner3 = calculateGenerationHeight(world, x, sizeZ);
        int corner4 = calculateGenerationHeight(world, sizeX, sizeZ);

        int max = Math.max(Math.max(corner3, corner4), Math.max(corner1, corner2));
        int min = Math.min(Math.min(corner3, corner4), Math.min(corner1, corner2));
        if (max - min > maxVariation) {
            return -1;
        }
        return min;
    }

    public static int calculateGenerationHeight(World world, int x, int z) {
        return world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).getY();
    }

    public static AxisAlignedBB makeBox(Vec3d pos1, Vec3d pos2) {
        return new AxisAlignedBB(pos1.x, pos1.y, pos1.z, pos2.x, pos2.y, pos2.z);
    }

    public static float getMobDamage(double baseAttackDamage, double healthScaledAttackFactor, float maxHealth, float health) {

        double healthScaledAttack = baseAttackDamage * healthScaledAttackFactor * (((maxHealth * 0.5) - health) / maxHealth);

        return (float) ((healthScaledAttack + baseAttackDamage));
    }


    public static Vec3d getRelativeOffset(EntityLivingBase actor, Vec3d offset) {
        Vec3d look = ModUtils.getVectorForRotation(0, actor.renderYawOffset);
        Vec3d side = look.rotateYaw((float) Math.PI * 0.5f);
        return look.scale(offset.x).add(yVec((float) offset.y)).add(side.scale(offset.z));
    }

    public static Vec3d yVec(double heightAboveGround) {
        return new Vec3d(0, heightAboveGround, 0);
    }

    public static Vec3d getVectorForRotation(float pitch, float yaw) {
        float f = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3d(f1 * f2, f3, f * f2);
    }

    public static List<Vec3d> cubePoints(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax) {
        List<Vec3d> points = new ArrayList<>();
        for(int x = xMin; x < xMax; x++) {
            for(int y = yMin; y < yMax; y++) {
                for(int z = zMin; z < zMax; z++) {
                    points.add(new Vec3d(x, y, z));
                }
            }
        }
        return points;
    }

    public static void handleAreaImpact(float radius, Function<Entity, Float> maxDamage, Entity source, Vec3d pos, DamageSource damageSource,
                                        float knockbackFactor, int fireFactor, boolean damageDecay) {
        if (source == null) {
            return;
        }

        List<Entity> list = source.world.getEntitiesWithinAABBExcludingEntity(source, new AxisAlignedBB(pos.x, pos.y, pos.z, pos.x, pos.y, pos.z).grow(radius));

        Predicate<Entity> isInstance = i -> i instanceof EntityLivingBase || i instanceof MultiPartEntityPart || i.canBeCollidedWith();
        double radiusSq = Math.pow(radius, 2);

        list.stream().filter(isInstance).forEach((entity) -> {

            // Get the hitbox size of the entity because otherwise explosions are less
            // effective against larger mobs
            double avgEntitySize = entity.getEntityBoundingBox().getAverageEdgeLength() * 0.75;

            // Choose the closest distance from the center or the head to encourage
            // headshots
            double distance = Math.min(Math.min(getCenter(entity.getEntityBoundingBox()).distanceTo(pos),
                            entity.getPositionVector().add(ModUtils.yVec(entity.getEyeHeight())).distanceTo(pos)),
                    entity.getPositionVector().distanceTo(pos));

            // Subtracting the average size makes it so that the full damage can be dealt
            // with a direct hit
            double adjustedDistance = Math.max(distance - avgEntitySize, 0);
            double adjustedDistanceSq = Math.pow(adjustedDistance, 2);
            double damageFactor = damageDecay ? Math.max(0, Math.min(1, (radiusSq - adjustedDistanceSq) / radiusSq)) : 1;

            // Damage decays by the square to make missed impacts less powerful
            double damageFactorSq = Math.pow(damageFactor, 2);
            double damage = maxDamage.apply(entity) * damageFactorSq;
            if (damage > 0 && adjustedDistanceSq < radiusSq) {
                entity.setFire((int) (fireFactor * damageFactorSq));
                if(entity.attackEntityFrom(damageSource, (float) damage)) {
                    double entitySizeFactor = avgEntitySize == 0 ? 1 : Math.max(0.5, Math.min(1, 1 / avgEntitySize));
                    double entitySizeFactorSq = Math.pow(entitySizeFactor, 2);

                    // Velocity depends on the entity's size and the damage dealt squared
                    Vec3d velocity = getCenter(entity.getEntityBoundingBox()).subtract(pos).normalize().scale(damageFactorSq).scale(knockbackFactor).scale(entitySizeFactorSq);
                    entity.addVelocity(velocity.x, velocity.y, velocity.z);
                }
            }
        });
    }


    /**
     * Gets the look vector from pitch and yaw, where 0 pitch is forward, negative 90 pitch is down
     * Yaw is the negative rotation of the z vector.
     *
     * @param pitch
     * @param yaw
     * @return
     */
    public static Vec3d getLookVec(float pitch, float yaw) {
        Vec3d yawVec = ModUtils.rotateVector2(ModUtils.Z_AXIS, ModUtils.Y_AXIS, -yaw);
        return ModUtils.rotateVector2(yawVec, yawVec.crossProduct(ModUtils.Y_AXIS), pitch);
    }

    /**
     * Returns the xyz offset using the axis as the relative base
     *
     * @param axis
     * @param offset
     * @return
     */
    public static Vec3d getAxisOffset(Vec3d axis, Vec3d offset) {
        Vec3d forward = axis.normalize();
        Vec3d side = axis.crossProduct(new Vec3d(0, 1, 0)).normalize();
        Vec3d up = axis.crossProduct(side).normalize();
        return forward.scale(offset.x).add(side.scale(offset.z)).add(up.scale(offset.y));
    }

    public static double unsignedAngle(Vec3d a, Vec3d b) {
        double dot = a.dotProduct(b);
        double cos = dot / (a.length() * b.length());
        return Math.acos(cos);
    }


    /**
     * Finds all entities that collide with the line specified by two vectors, excluding a certain entity
     *
     * @param start
     * @param end
     * @param world
     * @param toExclude
     * @return
     */
    public static List<Entity> findEntitiesInLine(Vec3d start, Vec3d end, World world, @Nullable Entity toExclude) {
        return world.getEntitiesInAABBexcluding(toExclude, new AxisAlignedBB(start.x, start.y, start.z, end.x, end.y, end.z), (e) -> {
            RayTraceResult raytraceresult = e.getEntityBoundingBox().calculateIntercept(start, end);
            return raytraceresult != null;
        });
    }


    public static boolean mobGriefing(World world, Entity entity){
        return ForgeEventFactory.getMobGriefingEvent(world, entity);
    }

    public static AxisAlignedBB vecBox(Vec3d vec1, Vec3d vec2) {
        return new AxisAlignedBB(vec1.x, vec1.y, vec1.z, vec2.x, vec2.y, vec2.z);
    }

    public static @Nullable
    Entity createMobFromSpawnData(MobSpawnerLogic.MobSpawnData data, World world, double x, double y, double z) {
        Entity entity;
        if (data.mobData != null) {
            // Read entity with custom NBT
            entity = AnvilChunkLoader.readWorldEntityPos(data.mobData, world, x, y, z, true);
        } else {
            // Read just the default entity
            entity = EntityList.createEntityByIDFromName(new ResourceLocation(data.mobId), world);
        }

        if (entity == null) {
            System.out.println("Failed to spawn entity with id " + data.mobId);
            return null;
        }

        entity.setLocationAndAngles(x, y, z, world.rand.nextFloat() * 360.0F, 0.0F);

        return entity;
    }

    /**
     * Pitch of a vector in degrees 90 is up, -90 is down.
     */
    public static double toPitch(Vec3d vec) {
        double angleBetweenYAxis = Math.toDegrees(unsignedAngle(vec, ModUtils.Y_AXIS.scale(-1)));
        return angleBetweenYAxis - 90;
    }
    /**
     * Search For blocks in area, this will be a periodic check
     */
    public static BlockPos searchForBlocks(AxisAlignedBB box, World world, IBlockState block) {
        int i = MathHelper.floor(box.minX);
        int j = MathHelper.floor(box.minY);
        int k = MathHelper.floor(box.minZ);
        int l = MathHelper.floor(box.maxX);
        int i1 = MathHelper.floor(box.maxY);
        int j1 = MathHelper.floor(box.maxZ);
        for (int x = i; x <= l; ++x) {
            for (int y = j; y <= i1; ++y) {
                for (int z = k; z <= j1; ++z) {
                    BlockPos blockpos = new BlockPos(x, y, z);
                    IBlockState iblockstate = world.getBlockState(blockpos);


                    if(iblockstate == block) {

                        return blockpos;
                    }
                }
            }
                }

        return null;
    }


    public static BlockPos avalonSearchForBlocks(AxisAlignedBB box, World world, Entity entity, IBlockState block) {
        int i = MathHelper.floor(box.minX);
        int j = MathHelper.floor(box.minY);
        int k = MathHelper.floor(box.minZ);
        int l = MathHelper.floor(box.maxX);
        int i1 = MathHelper.floor(box.maxY);
        int j1 = MathHelper.floor(box.maxZ);
        for (int x = i; x <= l; ++x) {
            for (int y = j; y <= i1; ++y) {
                for (int z = k; z <= j1; ++z) {
                    BlockPos blockpos = new BlockPos(x, y, z);
                    IBlockState iblockstate = world.getBlockState(blockpos);


                    if(iblockstate == block && world.getBlockState(blockpos.up()) != ModBlocks.OBSIDIAN_HEALTH_BLOCK.getDefaultState()) {

                        return blockpos;
                    }
                }
            }
        }

        return null;
    }

    public static double getBlastPower(double dist, double radius){
        double decay_rd = radius * 0.95;
        if(dist < decay_rd){
            return 1.1d;
        }
        else {
            return -(1/(radius-decay_rd))*(dist-decay_rd) + 1;
        }
    }

    /**
     * \
     * Method specifically for the Nuke Bombing, credit to songxia28 from Guns, Rocks, and Explosions for creating a circular explosion with rand effects to it
     * @param box
     * @param world
     * @param entity
     * @param x
     * @param y
     * @param z
     */
    public static void destroyBlocksNukeAABB(AxisAlignedBB box, World world, Entity entity, double x, double y, double z) {

        //Circle for calculating Radius to convert to Air
        List<BlockPos> affectedBlockPositions = Lists.newArrayList();
        int radius_int = (int) Math.ceil(60);
        for (int dx = -radius_int; dx < radius_int + 1; dx++) {
            // fast calculate affected blocks
            int y_lim = (int) Math.sqrt(radius_int*radius_int-dx*dx);
            for (int dy = -y_lim; dy < y_lim + 1; dy++) {
                int z_lim = (int) Math.sqrt(radius_int*radius_int-dx*dx-dy*dy);
                for (int dz = -z_lim; dz < z_lim + 1; dz++) {
                    BlockPos blockPos = new BlockPos(x + dx, y + dy, z + dz);
                    double power = getBlastPower(Math.sqrt(dx*dx+dy*dy+dz*dz), 60);
                    if ((power>1) ||(power > new Random().nextDouble())){
                            affectedBlockPositions.add(blockPos);
                    }
                }
            }
        }

        for(BlockPos blockPos : affectedBlockPositions) {
            world.setBlockToAir(blockPos);
        }
        //This is for the Conversion to Ash Wastelands blocks
        List<BlockPos> affectedConversionPositions = Lists.newArrayList();
        int radius_int_conversion = (int) Math.ceil(80);

        for (int dx = -radius_int_conversion; dx < radius_int_conversion + 1; dx++) {
            // fast calculate affected blocks
            int y_lim = (int) Math.sqrt(radius_int_conversion*radius_int_conversion-dx*dx);
            for (int dy = -y_lim; dy < y_lim + 1; dy++) {
                int z_lim = (int) Math.sqrt(radius_int_conversion*radius_int_conversion-dx*dx-dy*dy);
                for (int dz = -z_lim; dz < z_lim + 1; dz++) {
                    BlockPos blockPos = new BlockPos(x + dx, y + dy, z + dz);
                    double power = getBlastPower(Math.sqrt(dx*dx+dy*dy+dz*dz), 80);
                    if ((power>1) ||(power > new Random().nextDouble())){
                        affectedConversionPositions.add(blockPos);
                    }
                }
            }
        }

        for(BlockPos blockPos : affectedConversionPositions) {
            if(world.getBlockState(blockPos).isFullBlock() && world.rand.nextInt(2) == 0) {
                world.setBlockState(blockPos, ModBlocks.END_ASH.getDefaultState());
            }
        }
    }
    public static void destroyBlocksInAABB(AxisAlignedBB box, World world, Entity entity) {
        int i = MathHelper.floor(box.minX);
        int j = MathHelper.floor(box.minY);
        int k = MathHelper.floor(box.minZ);
        int l = MathHelper.floor(box.maxX);
        int i1 = MathHelper.floor(box.maxY);
        int j1 = MathHelper.floor(box.maxZ);

        for (int x = i; x <= l; ++x) {
            for (int y = j; y <= i1; ++y) {
                for (int z = k; z <= j1; ++z) {
                    BlockPos blockpos = new BlockPos(x, y, z);
                    IBlockState iblockstate = world.getBlockState(blockpos);
                    Block block = iblockstate.getBlock();

                    if (!block.isAir(iblockstate, world, blockpos) && iblockstate.getMaterial() != Material.FIRE) {
                        if (ForgeEventFactory.getMobGriefingEvent(world, entity)) {
                            if (block != Blocks.COMMAND_BLOCK &&
                                    block != Blocks.REPEATING_COMMAND_BLOCK &&
                                    block != Blocks.CHAIN_COMMAND_BLOCK &&
                                    block != Blocks.BEDROCK &&
                                    block != ModBlocks.BARE_EYE_ARENA &&
                                    !(block instanceof BlockLiquid)) {
                                if (world.getClosestPlayer(blockpos.getX(), blockpos.getY(), blockpos.getZ(), 20, false) != null) {
                                    world.destroyBlock(blockpos, false);
                                } else {
                                    world.setBlockToAir(blockpos);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void destroySpecificBlocks(AxisAlignedBB box, World world, Entity entity) {
        int i = MathHelper.floor(box.minX);
        int j = MathHelper.floor(box.minY);
        int k = MathHelper.floor(box.minZ);
        int l = MathHelper.floor(box.maxX);
        int i1 = MathHelper.floor(box.maxY);
        int j1 = MathHelper.floor(box.maxZ);

        for (int x = i; x <= l; ++x) {
            for (int y = j; y <= i1; ++y) {
                for (int z = k; z <= j1; ++z) {
                    BlockPos blockpos = new BlockPos(x, y, z);
                    IBlockState iblockstate = world.getBlockState(blockpos);
                    Block block = iblockstate.getBlock();

                    if (!block.isAir(iblockstate, world, blockpos) && iblockstate.getMaterial() != Material.FIRE) {
                        if (ForgeEventFactory.getMobGriefingEvent(world, entity)) {
                            if(block == ModBlocks.BARE_LEAVES || block == ModBlocks.ASH_BRICK_PILLAR || block == ModBlocks.BARE_PLANT || block == ModBlocks.ASH_BRICK_HALF) {
                                world.destroyBlock(blockpos, false);
                            }

                        }
                    }
                }
            }
        }
    }
    public static boolean collisionNearby(AxisAlignedBB box, World world, Entity entity) {
        int i = MathHelper.floor(box.minX);
        int j = MathHelper.floor(box.minY);
        int k = MathHelper.floor(box.minZ);
        int l = MathHelper.floor(box.maxX);
        int i1 = MathHelper.floor(box.maxY);
        int j1 = MathHelper.floor(box.maxZ);

        for (int x = i; x <= l; ++x) {
            for (int y = j; y <= i1; ++y) {
                for (int z = k; z <= j1; ++z) {
                    BlockPos blockpos = new BlockPos(x, y, z);
                    IBlockState iblockstate = world.getBlockState(blockpos);
                    Block block = iblockstate.getBlock();

                    if (!block.isAir(iblockstate, world, blockpos) && iblockstate.getMaterial() != Material.FIRE) {
                            return true;
                    }
                }
            }
        }
        return false;
    }

    public static String translateDesc(String key, Object... params) {
        return I18n.format(ModUtils.LANG_DESC + key, params);
    }

    public static void handleAreaImpact(float radius, Function<Entity, Float> maxDamage, Entity source, Vec3d pos, DamageSource damageSource) {
        handleAreaImpact(radius, maxDamage, source, pos, damageSource, 1, 0);
    }

    public static void handleAreaImpact(float radius, Function<Entity, Float> maxDamage, Entity source, Vec3d pos, DamageSource damageSource,
                                        float knockbackFactor, int fireFactor) {
        handleAreaImpact(radius, maxDamage, source, pos, damageSource, knockbackFactor, fireFactor, true);
    }

    private static Vec3d getCenter(AxisAlignedBB box) {
        return new Vec3d(box.minX + (box.maxX - box.minX) * 0.5D, box.minY + (box.maxY - box.minY) * 0.5D, box.minZ + (box.maxZ - box.minZ) * 0.5D);
    }

    public static void leapTowards(EntityLivingBase entity, Vec3d target, float horzVel, float yVel) {
        Vec3d dir = target.subtract(entity.getPositionVector()).normalize();
        Vec3d leap = new Vec3d(dir.x, 0, dir.z).normalize().scale(horzVel).add(ModUtils.yVec(yVel));
        entity.motionX += leap.x;
        if (entity.motionY < 0.1) {
            entity.motionY += leap.y;
        }
        entity.motionZ += leap.z;

        // Normalize to make sure the velocity doesn't go beyond what we expect
        double horzMag = Math.sqrt(Math.pow(entity.motionX, 2) + Math.pow(entity.motionZ, 2));
        double scale = horzVel / horzMag;
        if (scale < 1) {
            entity.motionX *= scale;
            entity.motionZ *= scale;
        }
    }
    // Act as a quick dash of sorts from it's location to the Player's location, in a fluid line. Used mostly for the Crystal boss
    public static void leapDash (EntityLivingBase entity, Vec3d target, float horzVel, double targetYValue) {
        Vec3d dir = target.subtract(entity.getPositionVector().normalize());
        double ySpeed = entity.motionY;
        double ySpeedResult = 0;
        if(entity.posY > targetYValue) {
            double d0 = entity.posY - targetYValue * 0.05;
            ySpeedResult -=ySpeed * d0;
        }
        if(entity.posY < targetYValue) {
            double d1 = targetYValue - entity.posY * 0.05;
             ySpeedResult += ySpeed * d1;
        }

        Vec3d leap = new Vec3d(dir.x, 0, dir.z).normalize().scale(horzVel).add(ModUtils.yVec(ySpeed));

        entity.motionZ += leap.x;
        entity.motionZ += leap.z;

        double horzMag = Math.sqrt(Math.pow(entity.motionX, 2) + Math.pow(entity.motionZ, 2));
        double scale = horzVel / horzMag;
        if(scale < 1) {
            entity.motionX *= scale;
            entity.motionZ *= scale;
        }

    }

    public static void setEntityPosition(Entity entity, Vec3d vec) {
        entity.setPosition(vec.x, vec.y, vec.z);
    }


    public static boolean attemptTeleport(Vec3d pos, EntityLivingBase entity)
    {
        double d0 = entity.posX;
        double d1 = entity.posY;
        double d2 = entity.posZ;
        ModUtils.setEntityPosition(entity, pos);
        boolean flag = false;
        BlockPos blockpos = new BlockPos(entity);
        World world = entity.world;
        Random random = entity.getRNG();

        if (world.isBlockLoaded(blockpos))
        {
            entity.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);

            if (world.getCollisionBoxes(entity, entity.getEntityBoundingBox()).isEmpty() && !world.containsAnyLiquid(entity.getEntityBoundingBox()))
            {
                flag = true;
            }
        }

        if (!flag)
        {
            entity.setPositionAndUpdate(d0, d1, d2);
            return false;
        }
        else
        {
            for (int j = 0; j < 128; ++j)
            {
                double d6 = (double)j / 127.0D;
                float f = (random.nextFloat() - 0.5F) * 0.2F;
                float f1 = (random.nextFloat() - 0.5F) * 0.2F;
                float f2 = (random.nextFloat() - 0.5F) * 0.2F;
                double d3 = d0 + (entity.posX - d0) * d6 + (random.nextDouble() - 0.5D) * (double)entity.width * 2.0D;
                double d4 = d1 + (entity.posY - d1) * d6 + random.nextDouble() * (double)entity.height;
                double d5 = d2 + (entity.posZ - d2) * d6 + (random.nextDouble() - 0.5D) * (double)entity.width * 2.0D;
                world.spawnParticle(EnumParticleTypes.PORTAL, d3, d4, d5, f, f1, f2);
            }

            if (entity instanceof EntityCreature)
            {
                ((EntityCreature)entity).getNavigator().clearPath();
            }

            return true;
        }
    }

    public static float clamp(double value, double min, double max) {
        return (float) Math.max(min, Math.min(max, value));
    }

    public static void lineCallback(Vec3d start, Vec3d end, int points, BiConsumer<Vec3d, Integer> callback) {
        Vec3d dir = end.subtract(start).scale(1 / (float) (points - 1));
        Vec3d pos = start;
        for (int i = 0; i < points; i++) {
            callback.accept(pos, i);
            pos = pos.add(dir);
        }
    }

    public static void throwProjectile(EntityLivingBase actor, Vec3d target, Projectile projectile, float inaccuracy, float velocity, Vec3d offset) {
        Vec3d pos = projectile.getPositionVector().add(offset);
        projectile.setPosition(pos.x, pos.y, pos.z);
        throwProjectile(actor, target, projectile, inaccuracy, velocity);
    }

    public static void throwProjectile(EntityLivingBase actor, EntityLivingBase target, Projectile projectile, float inaccuracy, float velocity, Vec3d offset) {
        Vec3d pos = projectile.getPositionVector().add(offset);
        projectile.setPosition(pos.x, pos.y, pos.z);
        throwProjectile(actor, target, projectile, inaccuracy, velocity);
    }

    public static void throwProjectile(EntityLivingBase actor, EntityLivingBase target, Projectile projectile, float inaccuracy, float velocity) {
        double d0 = target.posY + target.getEyeHeight() - 0.9;
        throwProjectile(actor, new Vec3d(target.posX, d0, target.posZ), projectile, inaccuracy, velocity);
    }

    public static void throwProjectile(EntityLivingBase actor, Vec3d target, Projectile projectile, float inaccuracy, float velocity) {
        throwProjectileNoSpawn(target, projectile, inaccuracy, velocity);
        actor.world.spawnEntity(projectile);
    }


    public static void throwProjectileNoSpawn(Vec3d target, Projectile projectile, float inaccuracy, float velocity) {
        double d0 = target.y;
        double d1 = target.x - projectile.posX;
        double d2 = d0 - projectile.posY;
        double d3 = target.z - projectile.posZ;
        float f = projectile.hasNoGravity() ? 0 : MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
        projectile.shoot(d1, d2 + f, d3, velocity, inaccuracy);
    }

    public static void addEntityVelocity(Entity entity, Vec3d vec) {
        entity.addVelocity(vec.x, vec.y, vec.z);
    }

    public static void facePosition(Vec3d pos, Entity entity, float maxYawIncrease, float maxPitchIncrease) {
        double d0 = pos.x - entity.posX;
        double d2 = pos.z - entity.posZ;
        double d1 = pos.y - entity.posY;

        double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
        float f = (float) (MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
        float f1 = (float) (-(MathHelper.atan2(d1, d3) * (180D / Math.PI)));
        entity.rotationPitch = updateRotation(entity.rotationPitch, f1, maxPitchIncrease);
        entity.rotationYaw = updateRotation(entity.rotationYaw, f, maxYawIncrease);
    }

    private static float updateRotation(float angle, float targetAngle, float maxIncrease) {
        float f = MathHelper.wrapDegrees(targetAngle - angle);

        if (f > maxIncrease) {
            f = maxIncrease;
        }

        if (f < -maxIncrease) {
            f = -maxIncrease;
        }

        return angle + f;
    }

    public static <T extends EntityAIBase> void removeTaskOfType(EntityAITasks tasks, Class<T> clazz) {
        Set<EntityAIBase> toRemove = Sets.newHashSet();

        for (EntityAITasks.EntityAITaskEntry entry : tasks.taskEntries) {
            if (clazz.isInstance(entry.action)) {
                toRemove.add(entry.action);
            }
        }

        for (EntityAIBase ai : toRemove) {
            tasks.removeTask(ai);
        }
    }





    /**
     * Finds the first solid block below the specified position and returns the position of that block
     */
    public static BlockPos findGroundBelow(World world, BlockPos pos) {
        for (int i = pos.getY(); i > 0; i--) {
            BlockPos tempPos = new BlockPos(pos.getX(), i, pos.getZ());
            if (world.getBlockState(tempPos).isSideSolid(world, tempPos, EnumFacing.UP)) {
                return tempPos;
            }
        }
        return new BlockPos(pos.getX(), 0, pos.getZ());
    }


    public static List<Vec3d> getBoundingBoxCorners(AxisAlignedBB box) {
        return new ArrayList<>(Arrays.asList(
                new Vec3d(box.maxX, box.maxY, box.maxZ),
                new Vec3d(box.maxX, box.maxY, box.minZ),
                new Vec3d(box.maxX, box.minY, box.maxZ),
                new Vec3d(box.maxX, box.minY, box.minZ),
                new Vec3d(box.minX, box.maxY, box.maxZ),
                new Vec3d(box.minX, box.maxY, box.minZ),
                new Vec3d(box.minX, box.minY, box.maxZ),
                new Vec3d(box.minX, box.minY, box.minZ)));
    }


    public static void setEntityVelocity(Entity entity, Vec3d vec) {
        entity.motionX = vec.x;
        entity.motionY = vec.y;
        entity.motionZ = vec.z;
    }

    public static Vec3d getEntityVelocity(Entity entity) {
        return new Vec3d(entity.motionX, entity.motionY, entity.motionZ);
    }


    public static Vec3d planeProject(Vec3d vec, Vec3d plane)
    {
        return ModUtils.rotateVector2(vec.crossProduct(plane), plane, 90);
    }

    public static Vec3d rotateVector2(Vec3d v, Vec3d k, double degrees) {
        double theta = Math.toRadians(degrees);
        k = k.normalize();
        return v
                .scale(Math.cos(theta))
                .add(k.crossProduct(v)
                        .scale(Math.sin(theta)))
                .add(k.scale(k.dotProduct(v))
                        .scale(1 - Math.cos(theta)));
    }

    public static Vec3d direction(Vec3d from, Vec3d to) {
        return to.subtract(from).normalize();
    }

    public static void aerialTravel(EntityLivingBase entity, float strafe, float vertical, float forward) {
        if (entity.isInWater()) {
            entity.moveRelative(strafe, vertical, forward, 0.02F);
            entity.move(MoverType.SELF, entity.motionX, entity.motionY, entity.motionZ);
            entity.motionX *= 0.800000011920929D;
            entity.motionY *= 0.800000011920929D;
            entity.motionZ *= 0.800000011920929D;
        } else if (entity.isInLava()) {
            entity.moveRelative(strafe, vertical, forward, 0.02F);
            entity.move(MoverType.SELF, entity.motionX, entity.motionY, entity.motionZ);
            entity.motionX *= 0.5D;
            entity.motionY *= 0.5D;
            entity.motionZ *= 0.5D;
        } else {
            float f = 0.91F;

            if (entity.onGround) {
                BlockPos underPos = new BlockPos(MathHelper.floor(entity.posX), MathHelper.floor(entity.getEntityBoundingBox().minY) - 1, MathHelper.floor(entity.posZ));
                IBlockState underState = entity.world.getBlockState(underPos);
                f = underState.getBlock().getSlipperiness(underState, entity.world, underPos, entity) * 0.91F;
            }

            float f1 = 0.16277136F / (f * f * f);
            entity.moveRelative(strafe, vertical, forward, entity.onGround ? 0.1F * f1 : 0.02F);
            f = 0.91F;

            if (entity.onGround) {
                BlockPos underPos = new BlockPos(MathHelper.floor(entity.posX), MathHelper.floor(entity.getEntityBoundingBox().minY) - 1, MathHelper.floor(entity.posZ));
                IBlockState underState = entity.world.getBlockState(underPos);
                f = underState.getBlock().getSlipperiness(underState, entity.world, underPos, entity) * 0.91F;
            }

            entity.move(MoverType.SELF, entity.motionX, entity.motionY, entity.motionZ);
            entity.motionX *= f;
            entity.motionY *= f;
            entity.motionZ *= f;
        }

        entity.prevLimbSwingAmount = entity.limbSwingAmount;
        double d1 = entity.posX - entity.prevPosX;
        double d0 = entity.posZ - entity.prevPosZ;
        float f2 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;

        if (f2 > 1.0F) {
            f2 = 1.0F;
        }

        entity.limbSwingAmount += (f2 - entity.limbSwingAmount) * 0.4F;
        entity.limbSwing += entity.limbSwingAmount;
    }

    public static void doSweepAttack(EntityPlayer player, @Nullable EntityLivingBase target, Consumer<EntityLivingBase> perEntity) {
        doSweepAttack(player, target, perEntity, 9, 1);
    }

    public static void doSweepAttack(EntityPlayer player, @Nullable EntityLivingBase target, Consumer<EntityLivingBase> perEntity, float maxDistanceSq, float areaSize) {
        float attackDamage = (float) player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        float sweepDamage = Math.min(0.15F + EnchantmentHelper.getSweepingDamageRatio(player), 1) * attackDamage;

        AxisAlignedBB box;

        if (target != null) {
            box = target.getEntityBoundingBox();
        } else {
            Vec3d center = ModUtils.getAxisOffset(player.getLookVec(), new Vec3d(areaSize * 1.5, 0, 0)).add(player.getPositionEyes(1));
            box = makeBox(center, center).grow(areaSize * 0.5, areaSize, areaSize * 0.5);
        }

        for (EntityLivingBase entitylivingbase : player.world.getEntitiesWithinAABB(EntityLivingBase.class, box.grow(areaSize, 0.25D, areaSize))) {
            if (entitylivingbase != player && entitylivingbase != target && !player.isOnSameTeam(entitylivingbase) && player.getDistanceSq(entitylivingbase) < maxDistanceSq) {
                entitylivingbase.knockBack(player, 0.4F, MathHelper.sin(player.rotationYaw * 0.017453292F), (-MathHelper.cos(player.rotationYaw * 0.017453292F)));
                perEntity.accept(entitylivingbase);
            }
        }
    }

    /**
     * Provides multiple points in a circle via a callback
     *
     * @param radius          The radius of the circle
     * @param points          The number of points around the circle
     * @param particleSpawner
     */

    public static void circleCallback(float radius, int points, Consumer<Vec3d> particleSpawner) {
        float degrees = 360f / points;
        for (int i = 0; i < points; i++) {
            double radians = Math.toRadians(i * degrees);
            Vec3d offset = new Vec3d(Math.sin(radians), Math.cos(radians), 0).scale(radius);
            particleSpawner.accept(offset);
        }
    }

    public static List<Vec3d> circlePoints(float radius, int numPoints) {
        List<Vec3d> points = new ArrayList<>();
        circleCallback(radius, numPoints, points::add);
        return points;
    }

    /**
     * Calls the function n times, passing in the ith iteration
     *
     * @param n
     * @param func
     */
    public static void performNTimes(int n, Consumer<Integer> func) {
        for (int i = 0; i < n; i++) {
            func.accept(i);
        }
    }


    public static BlockPos posToChunk(BlockPos pos) {
        return new BlockPos(pos.getX() / 16f, pos.getY(), pos.getZ() / 16f);
    }

    /**
     * Determines if the chunk is already generated, in which case new structures cannot be placed
     *
     * @param box
     * @param world
     * @return
     */
    public static boolean chunksGenerated(StructureBoundingBox box, World world) {
        return world.isChunkGeneratedAt(box.minX >> 4, box.minZ >> 4) || world.isChunkGeneratedAt(box.minX >> 4, box.maxZ >> 4)
                || world.isChunkGeneratedAt(box.maxX >> 4, box.minZ >> 4) || world.isChunkGeneratedAt(box.maxX >> 4, box.maxZ >> 4);
    }


    public static Vec3d findEntityGroupCenter(Entity mob, double d) {
        Vec3d groupCenter = mob.getPositionVector();
        float numMobs = 1;
        for (EntityLivingBase entity : ModUtils.getEntitiesInBox(mob, new AxisAlignedBB(mob.getPosition()).grow(d))) {
            if (entity instanceof EntityEnderKnight && !(entity instanceof EntityEnderMage)) {
                groupCenter = groupCenter.add(entity.getPositionVector());
                numMobs += 1;
            }
        }

        return groupCenter.scale(1 / numMobs);
    }

    public static List<EntityLivingBase> getEntitiesInBox(Entity entity, AxisAlignedBB bb) {
        List<Entity> list = entity.world.getEntitiesWithinAABBExcludingEntity(entity, bb);

        Predicate<Entity> isInstance = i -> i instanceof EntityLivingBase;
        Function<Entity, EntityLivingBase> cast = i -> (EntityLivingBase) i;

        return list.stream().filter(isInstance).map(cast).collect(Collectors.toList());
    }

    public static boolean canBlockDamageSource(DamageSource damageSourceIn, EntityLivingBase entity)
    {
        if (!damageSourceIn.isUnblockable() && entity.isActiveItemStackBlocking())
        {
            Vec3d vec3d = damageSourceIn.getDamageLocation();

            if (vec3d != null)
            {
                Vec3d vec3d1 = entity.getLook(1.0F);
                Vec3d vec3d2 = vec3d.subtractReverse(new Vec3d(entity.posX, entity.posY, entity.posZ)).normalize();
                vec3d2 = new Vec3d(vec3d2.x, 0.0D, vec3d2.z);

                if (vec3d2.dotProduct(vec3d1) < 0.0D)
                {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Gets the advancement and if that current player has the advancement completed
     *  If the config option is set to false, this will return automatically true
     */
    public static boolean getAdvancementCompletion(EntityPlayer currentPlayer, String advancementName) {
        if(!ModConfig.isModProgressionEnabled) {
            return true;
        }

        ResourceLocation loc = new ResourceLocation(ModReference.MOD_ID + ":" +  advancementName);
      boolean isCompleted = Main.proxy.doesPlayerHaveXAdvancement(currentPlayer, loc);
      return isCompleted;
    }


    public static boolean getAdvancementCompletionAsList(EntityPlayer currentPlayer, String[] advancementNamesList) {
        if(!ModConfig.isModProgressionEnabled) {
            return true;
        }

        for(String adv : advancementNamesList) {
            ResourceLocation loc = new ResourceLocation(adv);
            boolean isCompleted = Main.proxy.doesPlayerHaveXAdvancement(currentPlayer, loc);

            if(!isCompleted) {
                break;
            }

            return true;
        }
      return false;
    }

    public static float calculateValueWithPrecentage(float precentageOf, float precentageVal) {
        return (precentageOf * Math.min(precentageVal, 100.0F)) / 100.0F;
    }


    public static float getPercentageOf(float precentageOf, float precentageVal) {
        return Math.min((precentageVal * 100.0F) / precentageOf, 100.0F);
    }

    /**
     * Returns a {@link Vec3d} of the coordinates at the centre of the given entity's bounding box. This is more
     * efficient than {@code GeometryUtils.getCentre(entity.getEntityBoundingBox())}
     *
     *
     * as it can use the entity's fields.
     */
    public static Vec3d getCentre(Entity entity){
        return new Vec3d(entity.posX, entity.posY + entity.height/2, entity.posZ);
    }

    /**
     * Returns a {@link Vec3d} of the coordinates at the centre of the given block position (i.e. the block coordinates
     * plus 0.5 in x, y, and z).
     */
    public static Vec3d getCentre(BlockPos pos){
        return new Vec3d(pos).add(0.5, 0.5, 0.5);
    }

    /**
     * Returns a {@link Vec3d} of the coordinates at the centre of the given bounding box (The one in {@code AxisAlignedBB}
     * itself is client-side only).
     */
    public static Vec3d getCentre(AxisAlignedBB box){
        return new Vec3d(box.minX + (box.maxX - box.minX) * 0.5, box.minY + (box.maxY - box.minY) * 0.5, box.minZ + (box.maxZ - box.minZ) * 0.5);
    }


    public static void undoGravity(Entity entity){
        if(!entity.hasNoGravity()){
            double gravity = 0.04;
            if(entity instanceof EntityThrowable) gravity = 0.03;
            else if(entity instanceof EntityArrow) gravity = 0.05;
            else if(entity instanceof EntityLivingBase) gravity = 0.08;
            entity.motionY += gravity;
        }
    }

    public static boolean compareItemStacks(ItemStack stack, ItemStack check, boolean useNBT) {
        if (stack != null && check != null) {
            if (check.getItem() == stack.getItem() && (check.getMetadata() == 32767 || check.getMetadata() == stack.getMetadata())) {
                if (!useNBT) {
                    return true;
                } else {
                    NBTTagCompound nbt1 = stack.getTagCompound();
                    NBTTagCompound nbt2 = check.getTagCompound();
                    if (nbt1 != null && nbt2 != null) {
                        return nbt1.equals(nbt2);
                    } else {
                        return nbt1 == nbt2;
                    }
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
