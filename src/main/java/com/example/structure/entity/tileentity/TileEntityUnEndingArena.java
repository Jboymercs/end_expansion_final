package com.example.structure.entity.tileentity;

import com.deeperdepths.common.DeeperDepths;
import com.deeperdepths.common.DeeperDepthsSoundEvents;
import com.deeperdepths.common.blocks.enums.EnumTrialSpawnerState;
import com.deeperdepths.common.integration.RaidsIntegration;
import com.deeperdepths.common.potion.DeeperDepthsPotions;
import com.example.structure.blocks.arenaBlocks.BlockEnumArenaStates;
import com.example.structure.entity.EntityBuffker;
import com.example.structure.entity.EntityEnderKnight;
import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.knighthouse.EntityEnderMage;
import com.example.structure.entity.knighthouse.EntityEnderShield;
import com.example.structure.entity.knighthouse.EntityKnightLord;
import com.example.structure.entity.seekers.EndSeeker;
import com.example.structure.entity.seekers.EndSeekerPrime;
import com.example.structure.entity.trader.EntityAvalon;
import com.example.structure.event_handler.PotionInArena;
import com.example.structure.init.ModBlocks;
import com.example.structure.init.ModPotions;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModReference;
import com.example.structure.util.ModUtils;
import com.example.structure.util.NBTExtras;
import com.example.structure.util.handlers.ModSoundHandler;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.fml.common.Loader;
import org.lwjgl.Sys;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

public class TileEntityUnEndingArena extends TileEntity implements ITickable {

    private List<UUID> active_players = Lists.newArrayList();
    private List<WeakReference<Entity>> current_mobs = Lists.newArrayList();
    private final List<UUID> mob_cache = Lists.newArrayList();
    private BlockEnumArenaStates state = BlockEnumArenaStates.INACTIVE;


    private double scale_level;

    private int required_range = 11;

    private static final ResourceLocation LOOT_TRADE = new ResourceLocation(ModReference.MOD_ID, "arena_table");
    private static final ResourceLocation LOOT_TRADE_2 = new ResourceLocation(ModReference.MOD_ID, "arena_table_2");

    private int spawned_mobs = 0;
    private int waveCount = 0;
    private int ejected_items = 0;

    private int tierCount = waveCount;
    private boolean isMiniBossSpawn = false;

    private boolean triedBossSpawning = false;
    private int cooldown = 0;

    private int miniBossBonus = 0;

    private int ejectedItems = 0;

    private boolean isWaveSet = false;

    private int canBeMiniboss = (ModRand.range(1, 10));

    private List<ItemStack> trade_items = Lists.newArrayList();

    private List<ItemStack> trade_items_2 = Lists.newArrayList();

    private long lootTableTradeSeed;
    private long lootTableTradeSeed_2;

    private TileEntity linkedBarrier;
    private AxisAlignedBB containmentField;

    private BlockPos linkedContainerPos; // Temporary stores the container position read from NBT until the world is set

    public void linkBarrier(TileEntity container) {
        this.linkedBarrier = container;
    }

    @Override
    public void setPos(BlockPos pos){
        super.setPos(pos);
        initBarrierField(pos);
    }

    private void initBarrierField(BlockPos pos){
        float r = PotionInArena.getArenaDistance(0);
        this.containmentField = new AxisAlignedBB(-r, -r, -r, r, r, r).offset(ModUtils.getCentre(pos));
    }


    @Override
    public void update() {
        if(world == null) {
            return;
        }


        if (cooldown > 0) {
            cooldown--;
            return;
        }

        if(this.linkedBarrier == null && this.linkedContainerPos != null){
            this.linkBarrier(world.getTileEntity(this.linkedContainerPos));
        }

        //once in a active state, it can begin spawning
        if(state == BlockEnumArenaStates.ACTIVE) {
            tierCount = waveCount;
            if(isWaveSet) {
                clearInvalidEntities();
                if(current_mobs.isEmpty()) {
                    isWaveSet = false;
                    cooldown = 100;
                    world.playSound(null, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f,
                            SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.BLOCKS, 1, 1);
                }
            }

                if(state != BlockEnumArenaStates.EJECTING) {
                    containNearbyTargets();
                }


            if(state.isActive() && !world.isRemote) {
                AxisAlignedBB box = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
                List<EntityPlayer> nearbyPlayers = this.world.getEntitiesWithinAABB(EntityPlayer.class, box.grow(13D), e -> !e.getIsInvulnerable());

                if(!nearbyPlayers.isEmpty()) {
                    //do barrier

                    //do mobs total
                    int grandTotal = isMiniBossSpawn ? 1 :(3* waveCount) + 3;

                    if(current_mobs.isEmpty() && spawned_mobs >= grandTotal) {
                        //Eject Loot
                        setState(BlockEnumArenaStates.EJECTING, 0, 0);
                        state = BlockEnumArenaStates.EJECTING;

                    }

                    if(tierCount >= 2 && !isMiniBossSpawn && !triedBossSpawning) {
                        if(canBeMiniboss >= 7) {
                            //spawn Miniboss
                            double x = pos.getX() + (world.rand.nextDouble() - world.rand.nextDouble()) * 4 + 0.5D;
                            double y = pos.getY() + world.rand.nextInt(3) - 1;
                            double z = pos.getZ() + (world.rand.nextDouble() - world.rand.nextDouble()) * 4 + 0.5D;
                            EntityModBase entity = getMiniBoss(scale_level);
                            BlockPos posToo = new BlockPos(x, y, z);
                            if(!(canSpawn(entity, posToo))) {
                                triedBossSpawning = true;
                                return;
                            }
                            entity.setScale(scale_level);
                            entity.setPosition(x,y,z);
                            entity.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, world.rand.nextFloat() * 360, 0);
                            spawned_mobs++;
                            current_mobs.add(new WeakReference<>(entity));
                            world.spawnEntity(entity);
                            cooldown = 40;
                            isWaveSet = true;
                            isMiniBossSpawn = true;
                        } else {
                            triedBossSpawning = true;
                            return;
                        }

                        //Regular Mob Spawning
                    } else if (!isMiniBossSpawn) {

                        if (current_mobs.size() < (2 + waveCount) && spawned_mobs < grandTotal && !isWaveSet) {
                            double x = pos.getX() + (world.rand.nextDouble() - world.rand.nextDouble()) * 5 + 0.5D;
                            double y = pos.getY() + world.rand.nextInt(3) - 1;
                            double z = pos.getZ() + (world.rand.nextDouble() - world.rand.nextDouble()) * 5 + 0.5D;
                            BlockPos posToo = new BlockPos(x, y, z);
                            EntityModBase entity = getMob(scale_level);
                            if (!(canSpawn(entity, posToo))) return;
                            entity.setPosition(x, y, z);
                            entity.setScale(scale_level);
                            entity.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, world.rand.nextFloat() * 360, 0);
                            spawned_mobs++;
                            current_mobs.add(new WeakReference<>(entity));
                            world.spawnEntity(entity);
                            this.isWaveSet = false;
                            cooldown = 60;
                        } else  {
                            isWaveSet = true;
                        }

                    }


                }
            }
        }


        if(state == BlockEnumArenaStates.EJECTING) {
            //EjectItems
            for (int i = ejectedItems; i <= waveCount + miniBossBonus + 3; i++) {
                if (tierCount <= 2) {
                    LootContext.Builder lootcontext$builder = (new LootContext.Builder((WorldServer) this.world));
                    trade_items = this.world.getLootTableManager().getLootTableFromLocation(LOOT_TRADE).generateLootForPools(this.lootTableTradeSeed == 0 ? new Random() : new Random(this.lootTableTradeSeed), lootcontext$builder.build());
                    for (ItemStack item : trade_items) {
                        EntityItem itemEntity = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 2, pos.getZ() + 0.5, item);
                        world.spawnEntity(itemEntity);
                        ejectedItems++;
                    }
                    if (i >= waveCount + miniBossBonus + 3) {
                        spawned_mobs = 0;
                        triedBossSpawning = false;
                        isMiniBossSpawn = false;
                        isWaveSet = false;
                        waveCount = 0;
                        tierCount = 0;
                        current_mobs.clear();
                        miniBossBonus = 0;
                        ejectedItems = 0;

                        AxisAlignedBB box = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
                        List<EntityPlayer> nearbyPlayers = this.world.getEntitiesWithinAABB(EntityPlayer.class, box.grow(16D), e -> !e.getIsInvulnerable());
                        if (!nearbyPlayers.isEmpty()) {
                            for (EntityPlayer player : nearbyPlayers) {
                                    player.clearActivePotions();
                                    System.out.println("Cleared Active Potions");
                            }
                        }
                        setState(BlockEnumArenaStates.INACTIVE, 0, 0);
                    }
                    cooldown = 20;
                } else {
                    LootContext.Builder lootcontext$builder = (new LootContext.Builder((WorldServer) this.world));
                    trade_items_2 = this.world.getLootTableManager().getLootTableFromLocation(LOOT_TRADE_2).generateLootForPools(this.lootTableTradeSeed_2 == 0 ? new Random() : new Random(this.lootTableTradeSeed_2), lootcontext$builder.build());
                    for (ItemStack item : trade_items_2) {
                        EntityItem itemEntity = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 2, pos.getZ() + 0.5, item);
                        world.spawnEntity(itemEntity);
                        ejectedItems++;
                    }
                    if (i >= waveCount + miniBossBonus + 3) {
                        spawned_mobs = 0;
                        triedBossSpawning = false;
                        isMiniBossSpawn = false;
                        isWaveSet = false;
                        waveCount = 0;
                        tierCount = 0;
                        current_mobs.clear();
                        miniBossBonus = 0;
                        ejectedItems = 0;

                        AxisAlignedBB box = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
                        List<EntityPlayer> nearbyPlayers = this.world.getEntitiesWithinAABB(EntityPlayer.class, box.grow(16D), e -> !e.getIsInvulnerable());
                        if (!nearbyPlayers.isEmpty()) {
                            for (EntityPlayer player : nearbyPlayers) {
                                    player.clearActivePotions();
                            }
                        }
                        setState(BlockEnumArenaStates.INACTIVE, 0, 0);
                    }
                    cooldown = 20;
                }
            }
        }
    }


    private boolean canSpawn(Entity entity, BlockPos pos) {
        if (world.collidesWithAnyBlock(entity.getEntityBoundingBox())) return false;
        if(world.getBlockState(pos).causesSuffocation()) {
            return false;
        }
        return true;
    }


    private void clearInvalidEntities() {
        current_mobs = current_mobs.stream().filter(ref -> ref.get() != null && ref.get().isEntityAlive()).collect(Collectors.toList());
    }

    protected EntityModBase getMiniBoss(double scale) {
        if(tierCount == 2) {
            miniBossBonus = 1;
            return new EndSeekerPrime(world);
        } else if (tierCount == 3) {
            if(world.rand.nextInt(2) == 0) {
                miniBossBonus = 1;
                return new EndSeekerPrime(world);
            } else {
                miniBossBonus = 2;
                return new EntityKnightLord(world);
            }
        } else if (tierCount == 4) {
            miniBossBonus = 3;
            return new EntityKnightLord(world);
        } else {
            miniBossBonus = 1;
            return new EndSeekerPrime(world);
        }
    }
    protected EntityModBase getMob(double scale) {
        if(tierCount == 1) {
            if(world.rand.nextInt(5) == 0) {
                return new EntityBuffker(world);
            } else {
                return new EndSeeker(world);
            }
        } else if (tierCount == 2) {
            if(world.rand.nextInt(3) == 0) {
                return new EntityBuffker(world);
            } else {
                return new EndSeeker(world);
            }

        } else if (tierCount == 3) {
            if(world.rand.nextInt(8) == 0) {
                if(world.rand.nextInt(2) == 0) {
                    return new EntityEnderMage(world);
                } else {
                    return new EntityEnderShield(world);
                }
            } else {
                return new EntityEnderKnight(world);
            }

        } else if (tierCount == 4) {
            if(world.rand.nextInt(4) == 0) {
                if(world.rand.nextInt(2) == 0) {
                    return new EntityEnderMage(world);
                } else {
                    return new EntityEnderShield(world);
                }
            } else {
                return new EntityEnderKnight(world);
            }
        } else {
            return new EndSeeker(world);
        }
    }

    public void setState(BlockEnumArenaStates state) {
        this.state = state;
        markDirty();
    }

    public void setState(BlockEnumArenaStates state, int waveCount, double scale) {
        this.state = state;
        this.scale_level = scale;
        this.waveCount = waveCount;
        this.tierCount = waveCount;
        markDirty();
    }

    public BlockEnumArenaStates getState() {
        return state;
    }


    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setByte("state", (byte)state.ordinal());
        nbt.setInteger("spawned_mobs", spawned_mobs);
        NBTTagList mobs = new NBTTagList();
        for (WeakReference<Entity> ref : current_mobs) {
            if (ref.get() == null) continue;
            mobs.appendTag(NBTUtil.createUUIDTag(ref.get().getUniqueID()));
        }
        nbt.setTag("current_mobs", mobs);
        nbt.setLong("loot_table_seed", lootTableTradeSeed);
        nbt.setLong("loot_table_seed_2", lootTableTradeSeed_2);
        if(linkedBarrier != null) NBTExtras.storeTagSafely(nbt, "linkedContainerPos", NBTUtil.createPosTag(linkedBarrier.getPos()));
        return nbt;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setByte("state", (byte)state.ordinal());
        return nbt;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound nbt) {
        if (nbt.hasKey("state", 1)) state = BlockEnumArenaStates.values()[nbt.getByte("state")];

    }

    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
    }


    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getNbtCompound());
        world.markBlockRangeForRenderUpdate(pos, pos);
    }

    public void readFromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("state", 1)) state = BlockEnumArenaStates.values()[nbt.getByte("state")];
        this.lootTableTradeSeed = nbt.getLong("loot_table_seed");
        this.lootTableTradeSeed_2 = nbt.getLong("loot_table_seed_2");
        this.linkedContainerPos = NBTUtil.getPosFromTag(nbt.getCompoundTag("linkedContainerPos"));
        initBarrierField(this.pos);
    }

    private void containNearbyTargets(){

        List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, containmentField,
                e -> e instanceof EntityPlayer || e instanceof EntityModBase);

        for(EntityLivingBase entity : entities){
            if(!entity.world.isRemote) {
                entity.addPotionEffect(new PotionEffect(ModPotions.IN_ARENA, 219));
                entity.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 100, 1));
            }

            NBTExtras.storeTagSafely(entity.getEntityData(), PotionInArena.ENTITY_TAG, NBTUtil.createPosTag(this.pos));
        }
    }

    @Override
    public void markDirty() {
        IBlockState state = world.getBlockState(pos);
        world.markBlockRangeForRenderUpdate(pos, pos);
        world.notifyBlockUpdate(pos, state, state, 3);
        world.scheduleBlockUpdate(pos, getBlockType(), 0, 0);
        super.markDirty();
    }
}
