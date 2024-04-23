package com.example.structure.entity.trader;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityChomper;
import com.example.structure.entity.EntityEye;
import com.example.structure.event_handler.ClientRender;
import com.example.structure.init.ModItems;
import com.example.structure.init.ModProfressions;
import com.example.structure.items.ItemObiCoin;
import com.example.structure.util.ModColors;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ModSoundHandler;
import com.example.structure.util.handlers.ParticleManager;
import com.example.structure.world.api.trader.WorldGenTraderArena;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class EntityAvalon extends EntityTrader implements IAnimatable, IAnimationTickable {
    /**
     * The Rare and mysterious trader found in the End, that may or may not be a boss
     */

    private final BossInfoServer bossInfo = (new BossInfoServer(this.getDisplayName(), BossInfo.Color.BLUE, BossInfo.Overlay.NOTCHED_6));
    private final String ANIM_IDLE_HIDDEN = "idle_hidden";
    private final String ANIM_IDLE_OPEN = "idle_open";
    private final String ANIM_OPEN = "state_expand";
    private final String ANIM_CLOSE = "state_close";

    private static final DataParameter<Boolean> OPEN = EntityDataManager.createKey(EntityAvalon.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CLOSE = EntityDataManager.createKey(EntityAvalon.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> OPEN_STATE = EntityDataManager.createKey(EntityAvalon.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> I_AM_BOSS = EntityDataManager.createKey(EntityAvalon.class, DataSerializers.BOOLEAN);
    public void setOpen(boolean value) {this.dataManager.set(OPEN, Boolean.valueOf(value));}
    public boolean isOpen() {return this.dataManager.get(OPEN);}
    public void setClose(boolean value) {this.dataManager.set(CLOSE, Boolean.valueOf(value));}
    public boolean isClose() {return this.dataManager.get(CLOSE);}
    public void setOpenState(boolean value) {this.dataManager.set(OPEN_STATE, Boolean.valueOf(value));}
    public boolean isOpenState() {return this.dataManager.get(OPEN_STATE);}
    public void setIAmBoss(boolean value) {this.dataManager.set(I_AM_BOSS, Boolean.valueOf(value));}
    public boolean isIAmBoss() {return this.dataManager.get(I_AM_BOSS);}

    private AnimationFactory factory = new AnimationFactory(this);
    public EntityAvalon(World worldIn) {
        super(worldIn);
        this.setImmovable(true);
        this.setNoGravity(true);
        this.setSize(1.3F, 2.3F);
        this.iAmBossMob = true;
    }

    public boolean IAmAggroed = false;

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 5.0F, 1.0F));
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    public boolean transitionTooOpen = false;
    public boolean hasSelectedTarget = false;
    public int tickTime = 60;
    @Override
    public void onUpdate() {
        super.onUpdate();
        EntityLivingBase target = this.getAttackTarget();

        if(this.IAmAggroed) {
            this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        }
        if(IAmAggroed) {
            List<EntityPlayer> nearbyPlayers = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(30D), e -> !e.getIsInvulnerable());
            if(nearbyPlayers.isEmpty()) {
                this.IAmAggroed = false;
            }
            if(!nearbyPlayers.isEmpty() && !hasSelectedTarget) {
                for(EntityPlayer player : nearbyPlayers) {
                    this.setAttackTarget(player);
                    BlockPos posToo = new BlockPos(this.posX, this.posY, this.posZ);
                    stateChangeTooAggro(posToo, player);
                    hasSelectedTarget = true;
                }
            }
        }


        if(target == null) {
            if (!this.isOpenState()) {
                this.motionX = 0;
                this.motionZ = 0;
                this.rotationYaw = 0;
                this.rotationPitch = 0;
                this.rotationYawHead = 0;
                this.renderYawOffset = 0;
            }

            List<EntityPlayer> nearbyPlayers = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(5D), e -> !e.getIsInvulnerable());

            if (!nearbyPlayers.isEmpty() && !this.isOpenState() && !this.transitionTooOpen) {
                for (EntityPlayer player : nearbyPlayers) {
                    if (!player.isSpectator() && !player.isCreative()) {
                        stateTransitionTooOpen();
                    }
                }
            }

            if (nearbyPlayers.isEmpty()) {
                if (this.isOpenState() && tickTime < 0 && !this.transitionTooOpen) {
                    stateTransitionTooClose();
                } else {
                    tickTime--;
                }
            }

            List<EntityItem> nearbyCoins = this.world.getEntitiesWithinAABB(EntityItem.class, this.getEntityBoundingBox().grow(5D), e -> !e.getIsInvulnerable());

            if (!nearbyCoins.isEmpty() && !this.IAmAggroed) {
                for (EntityItem item : nearbyCoins) {

                    ItemStack stackIn = item.getItem();
                    if (stackIn.getItem() == ModItems.OBSIDIAN_COIN) {
                        item.setDead();
                        System.out.println("Found A COIN!");
                        IAmAggroed = true;
                    }
                }
            }

        }

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) {

        if (id == ModUtils.PARTICLE_BYTE) {
            ModUtils.circleCallback(1, 40, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.0, 0))), ModColors.MAELSTROM, pos.normalize().scale(4).add(ModUtils.yVec(0)));
            });
        }
        super.handleStatusUpdate(id);
    }

    public void stateChangeTooAggro(BlockPos pos, EntityPlayer target) {

        addEvent(()-> ClientRender.SCREEN_SHAKE = 2f, 30);


        addEvent(()-> {
            this.world.newExplosion(this, this.posX, this.posY -1, this.posZ, 0, false, false);
            world.setEntityState(this, ModUtils.PARTICLE_BYTE);

            addEvent(()-> {
                Vec3d posToo = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 0, 0)));
                DamageSource source = ModDamageSource.builder()
                        .type(ModDamageSource.MOB)
                        .directEntity(this)
                        .build();
                float damage = (float) ((this.getAttack() - 10) * ModConfig.biome_multiplier);
                ModUtils.handleAreaImpact(5.0f, (e) -> damage, this, posToo, source, 1.2F, 0, false );
            }, 5);
        }, 60);

        addEvent(()-> new WorldGenTraderArena("trader/eye_arena").generateStructure(world, pos, Rotation.NONE), 85);
    }


    /**
     * When the entity is right clicked
     */
    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        if (this.isEntityAlive() && !this.isTrading() && !this.isChild() && !player.isSneaking() && this.getAttackTarget() == null && this.isOpenState()) {
            if (this.buyingList == null) {
                this.populateBuyingList();
            }

            if (hand == EnumHand.MAIN_HAND) {
                player.addStat(StatList.TALKED_TO_VILLAGER);
            }

            this.onTraderInteract(player);

            if (!this.world.isRemote && !this.buyingList.isEmpty()) {
                this.setCustomer(player);
                player.displayVillagerTradeGui(this);
            } else if (this.buyingList.isEmpty()) {
                return super.processInteract(player, hand);
            }

            return true;
        } else {
            return super.processInteract(player, hand);
        }
    }

    public void stateTransitionTooClose() {
        this.setOpenState(false);
        this.setClose(true);

        addEvent(()-> {
            tickTime = 60;
        this.setClose(false);
        }, 30);
    }

    public void stateTransitionTooOpen() {
        this.setOpen(true);
        this.transitionTooOpen = true;
        addEvent(()-> {
        this.setOpen(false);
        this.setOpenState(true);
        this.transitionTooOpen = false;
        }, 60);
    }


    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(OPEN_STATE, Boolean.valueOf(false));
        this.dataManager.register(CLOSE, Boolean.valueOf(false));
        this.dataManager.register(OPEN, Boolean.valueOf(false));
        this.dataManager.register(I_AM_BOSS, Boolean.valueOf(false));
    }

    @Override
    protected List<EntityVillager.ITradeList> getTrades() {
        return ModProfressions.AVALON_TRADER.getTrades(0);
    }

    @Override
    protected String getVillagerName() {
        return "The Avalon";
    }

    @Override
    public void removeTrackingPlayer(EntityPlayerMP player) {
        if(this.IAmAggroed) {
            super.removeTrackingPlayer(player);
            this.bossInfo.removePlayer(player);
        }
    }

    public void setPosition(BlockPos pos) {
        this.setPosition(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void addTrackingPlayer(EntityPlayerMP player) {
        if(this.IAmAggroed) {
            super.addTrackingPlayer(player);
            this.bossInfo.addPlayer(player);
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "transition_controller", 0, this::predicateTransitions));
    }
    private<E extends IAnimatable> PlayState predicateTransitions(AnimationEvent<E> event) {
        if(this.isOpen()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_OPEN, false));
            return PlayState.CONTINUE;
        }
        if(this.isClose()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_CLOSE, false));
            return PlayState.CONTINUE;
        }

        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(!this.isOpen() && !this.isClose()) {
            if(this.isOpenState()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_OPEN, true));
                return PlayState.CONTINUE;
            } else if(!this.isIAmBoss()){
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_HIDDEN, true));
                return PlayState.CONTINUE;
            }
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void tick() {


    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSoundHandler.AVALON_IDLE;
    }


    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }
}
