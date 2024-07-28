package com.example.structure.entity.trader;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.ai.EntityAIAvalon;
import com.example.structure.entity.trader.action.ActionFarRange;
import com.example.structure.entity.trader.action.ActionLineAOE;
import com.example.structure.entity.trader.action.ActionMediumRangeAOE;
import com.example.structure.entity.trader.action.ActionShortRangeAOE;
import com.example.structure.entity.util.IAttack;
import com.example.structure.event_handler.ClientEvents;
import com.example.structure.event_handler.ClientRender;
import com.example.structure.init.ModItems;
import com.example.structure.util.*;
import com.example.structure.util.handlers.ModSoundHandler;
import com.example.structure.util.handlers.ParticleManager;
import com.example.structure.world.api.trader.WorldGenTraderArena;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class EntityAvalon extends EntityAbstractAvalon implements IAnimatable, IAnimationTickable, IAttack {


    /**
     * The Rare and mysterious trader found in the End, that may or may not be a boss
     */
    private final BossInfoServer bossInfo = (new BossInfoServer(this.getDisplayName(), BossInfo.Color.BLUE, BossInfo.Overlay.NOTCHED_6));
    private final String ANIM_IDLE_HIDDEN = "idle_hidden";
    private final String ANIM_IDLE_OPEN = "idle_open";
    private final String ANIM_OPEN = "state_expand";
    private final String ANIM_CLOSE = "state_close";

    private final String ANIM_CAST_LAZERS = "summon_lazer";
    private final String ANIM_CAST_AOE = "cast";
    private final String ANIM_SMASH_ATTACK = "smash";
    private final String ANIM_PROJECTILE_BOMB = "shoot";

    private final String ANIM_DELAY_SMASH = "delay_smash";


    //Other ANIMS
    private final String ANIM_SUMMON = "summon";
    private final String ANIM_DEATH = "death";
    protected Vec3d positionStarted = this.getPositionVector();
    private Consumer<EntityLivingBase> prevAttack;
    public EntityLivingBase thisIsMyTarget;

    private AnimationFactory factory = new AnimationFactory(this);
    public EntityAvalon(World worldIn) {
        super(worldIn);

        this.setSummonState(true);
        addEvent(()-> this.setSummonState(false), 50);
    }



    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 5.0F, 1.0F));
    }

    public boolean transitionTooOpen = false;

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
                    this.thisIsMyTarget = player;
                    BlockPos posToo = new BlockPos(this.posX, this.posY, this.posZ);
                    stateChangeTooAggro(posToo, player);
                    hasSelectedTarget = true;
                }
            }
        }


        if(target == null) {
            if (!this.isOpenState() && !this.isSummonState() && !this.isDeathState()) {
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

            if (nearbyPlayers.isEmpty() && !this.isSummonState() && !this.isDeathState()) {
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
                ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.0, 0))), ModColors.MAELSTROM, pos.normalize().scale(3).add(ModUtils.yVec(0)));
            });
        }
        if(id == ModUtils.SECOND_PARTICLE_BYTE) {
            ModUtils.circleCallback(2, 50, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                Vec3d vel = pos.normalize().scale(0.5).add(ModUtils.yVec(0.0));
                Vec3d currentPos = this.getPositionVector().add(ModUtils.yVec(0.1));
                world.spawnParticle(EnumParticleTypes.CLOUD, currentPos.x, currentPos.y - 2, currentPos.z, vel.x, vel.y, vel.z);

            });
        }

        if(id == ModUtils.THIRD_PARTICLE_BYTE) {
            Vec3d pos = new Vec3d(this.posX + ModRand.range(-1 , 1), this.posY + 2, this.posZ + ModRand.range(-1, 1));
            ParticleManager.spawnColoredSmoke(world, pos, ModColors.PURPLE, new Vec3d(0, 0.05, 0));
        }

        if(id == ModUtils.FOURTH_PARTICLE_BYTE) {
            List<EntityPlayer> nearbyPlayers = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(25D), e -> !e.getIsInvulnerable());
            for(EntityPlayer player : nearbyPlayers) {
              //  ClientRender.SCREEN_SHAKE = 2F;

            }
        }
        super.handleStatusUpdate(id);
    }

    public void stateChangeTooAggro(BlockPos pos, EntityPlayer target) {
        this.setIAmBoss(true);
        this.playSound(ModSoundHandler.AVALON_SPEAK, 1.4f, 1.0f / (rand.nextFloat() * 0.4f + 0.6f));

        addEvent(()-> world.setEntityState(this, ModUtils.FOURTH_PARTICLE_BYTE), 30);


        addEvent(()-> {
            this.canBeCollidedWith();
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
                this.initBossAI();

            }, 5);
        }, 60);

        if(!world.isRemote) {
            addEvent(() -> new WorldGenTraderArena("trader/eye_arena").generateStructure(this.world, pos, Rotation.NONE), 70);
        }
        addEvent(()-> this.ableTooDisableWhenTargetsAreGone = true, 200);
    }

    //Adds the AI for the boss
    public void initBossAI() {
        this.tasks.addTask(4, new EntityAIAvalon<>(this, 1.0, 80, 17F, 0F));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
        this.hasRemovedAITasks = false;
    }

    @Override
    public boolean canBeCollidedWith() {
        if(this.isIAmBoss()) {
            return false;
        }
     return true;
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
    public void removeTrackingPlayer(EntityPlayerMP player) {
            super.removeTrackingPlayer(player);
            this.bossInfo.removePlayer(player);
    }

    public void setPosition(BlockPos pos) {
        this.setPosition(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void addTrackingPlayer(EntityPlayerMP player) {
            super.addTrackingPlayer(player);
            this.bossInfo.addPlayer(player);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "transition_controller", 0, this::predicateTransitions));
        data.addAnimationController(new AnimationController(this, "fight_idle_controller", 0, this::predicateFightIdle));
        data.addAnimationController(new AnimationController(this, "attack_controller", 0, this::predicateFight));
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
        //We are just going to disable this controller if the mob is Aggroed
        if(!this.isOpen() && !this.isClose() && !this.isIAmBoss() && !this.isSummonState()) {
            if(this.isOpenState()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_OPEN, true));
                return PlayState.CONTINUE;
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_HIDDEN, true));
                return PlayState.CONTINUE;
            }
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateFightIdle(AnimationEvent<E> event) {
        if(!this.isFightMode() && this.isIAmBoss() && !this.isDeathState()) {
            if(this.isOpenState()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_OPEN, true));
                return PlayState.CONTINUE;
            }
            else if(this.isCloseState()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_HIDDEN, true));
                return PlayState.STOP;
            }
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateFight(AnimationEvent<E> event) {
        if(this.isFightMode() && !this.isSummonState() && !this.isDeathState()) {
            if(this.isCastAOE()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_CAST_AOE, false));
                return PlayState.CONTINUE;
            }
            if(this.isCastLazers()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_CAST_LAZERS, false));
                return PlayState.CONTINUE;
            }
            if(this.isSmashAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SMASH_ATTACK, false));
                return PlayState.CONTINUE;
            }
            if(this.isProjectileAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_PROJECTILE_BOMB, false));
                return PlayState.CONTINUE;
            }
            if(this.isTeleportAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DELAY_SMASH, false));
                return PlayState.CONTINUE;
            }
        }
        if(this.isSummonState()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SUMMON, false));
            return PlayState.CONTINUE;
        }
        if(this.isDeathState()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DEATH, false));
            return PlayState.CONTINUE;
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

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        double HealthChange = this.getHealth() / this.getMaxHealth();
        if(!this.isFightMode() && this.isIAmBoss() && !this.isCloseState()) {
            List<Consumer<EntityLivingBase>> attacks = new ArrayList<>(Arrays.asList(summonLazerMinions, AOE_attack, AOE_line, smash_attack, projectile_Attack, teleportSlam));

            double[] weights = {
                    (distance < 18 && !this.hasLazerMinions) ? 1/distance : 0, //Summon Lazer Minions
                    (distance < 18 && HealthChange > 0.75) ? 1/distance : (distance < 20 && prevAttack != AOE_attack) ? 1/distance : 0, //AOE Ranged Attack Can be spammed while above 0.75 Health
                    (distance < 18 && prevAttack != AOE_line && HealthChange <= 0.75) ? 1/distance : 0, //AOE Attack Line
                    (distance <= 4 && prevAttack != smash_attack && HealthChange <= 0.75 && !this.isCloseState()) ? 50 : 0,   // Smash Attack
                    (distance < 18 && distance >= 4 && prevAttack != projectile_Attack) ? 1/distance : 0, //Projectile Attack Test
                    (distance < 18 && this.posY > target.posY && prevAttack != teleportSlam && HealthChange <= 0.5) ? 1/distance : 0 // Teleport Slam
            };
            prevAttack = ModRand.choice(attacks, rand, weights).next();
            prevAttack.accept(target);
        }

        //This decreases the attack timer past each Health chunk it loses, making it progressively faster
        return (HealthChange > 0.75) ? 130 : (HealthChange <= 0.75 && HealthChange > 0.5) ? 110 : (HealthChange <= 0.5 && HealthChange > 0.25) ? 90 : 70;
    }

    protected final Consumer<EntityLivingBase> teleportSlam = (target) -> {
        this.setFightMode(true);
        this.setTeleportAttack(true);
        this.playSound(ModSoundHandler.AVALON_TELEPORT_SMASH, 1.0f, 1.0f / (rand.nextFloat() * 0.4f + 0.5f));
        Vec3d posToGoBackToo = this.getPositionVector();
        for(int i = 0; i < 20; i++) {
            world.setEntityState(this, ModUtils.THIRD_PARTICLE_BYTE);
        }

        addEvent(()-> {
            this.setImmovable(false);
            ModUtils.attemptTeleport(new Vec3d(target.posX, this.posY, target.posZ), this);
            this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.4F, 1.0f / rand.nextFloat() * 0.4f + 0.6f);
        }, 20);

        addEvent(()-> {
            world.setEntityState(this, ModUtils.SECOND_PARTICLE_BYTE);
            this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f / rand.nextFloat() * 0.4f + 0.6f);
            Vec3d posToo = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, -2, 0)));
            DamageSource source = ModDamageSource.builder()
                    .type(ModDamageSource.MOB)
                    .directEntity(this)
                    .build();
            float damage = (float) ((this.getAttack() - 2));
            ModUtils.handleAreaImpact(3.5f, (e) -> damage, this, posToo, source, 0.9F, 0, false );
        }, 30);

        addEvent(()-> {
            this.setTeleportAttack(false);
            ModUtils.attemptTeleport(posToGoBackToo, this);
            this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.4F, 1.0f / rand.nextFloat() * 0.4f + 0.6f);
        }, 50);

        addEvent(() -> {
            this.setImmovable(true);
        }, 55);
        addEvent(()-> {
            this.setFightMode(false);
        }, 65);
    };

    private final Consumer<EntityLivingBase> projectile_Attack = (target) -> {
    this.setProjectileAttack(true);
    this.setFightMode(true);
        this.playSound(ModSoundHandler.AVALON_SHOOT, 1.0f, 1.0f / (rand.nextFloat() * 0.4f + 0.5f));
    //Randomly and accurately spawns projectiles above the Player's head
    addEvent(()-> {
        for(int y = 0; y < 45; y +=5) {
            addEvent(()-> {
                Vec3d randomImpact = new Vec3d(target.posX + ModRand.range(-6, 6), this.posY, target.posZ + ModRand.range(-6, 6));
            ProjectileBomb bomb = new ProjectileBomb(world, this, this.getAttack(), randomImpact);
            bomb.setNoGravity(false);
            bomb.setPosition(randomImpact.x, randomImpact.y + 18, randomImpact.z);
            this.world.spawnEntity(bomb);
            }, y);
        }
        addEvent(()-> {
            Vec3d randomImpact = new Vec3d(target.posX, this.posY, target.posZ);
            ProjectileBomb bomb = new ProjectileBomb(world, this, this.getAttack(), randomImpact);
            bomb.setNoGravity(false);
            bomb.setPosition(randomImpact.x, randomImpact.y + 18, randomImpact.z);
            this.world.spawnEntity(bomb);
        }, 10);

        addEvent(()-> {
            Vec3d randomImpact = new Vec3d(target.posX, this.posY, target.posZ);
            ProjectileBomb bomb = new ProjectileBomb(world, this, this.getAttack(), randomImpact);
            bomb.setNoGravity(false);
            bomb.setPosition(randomImpact.x, randomImpact.y + 18, randomImpact.z);
            this.world.spawnEntity(bomb);
        }, 25);
        addEvent(()-> {
            Vec3d randomImpact = new Vec3d(target.posX, this.posY, target.posZ);
            ProjectileBomb bomb = new ProjectileBomb(world, this, this.getAttack(), randomImpact);
            bomb.setNoGravity(false);
            bomb.setPosition(randomImpact.x, randomImpact.y + 18, randomImpact.z);
            this.world.spawnEntity(bomb);
        }, 40);
    }, 25);
    addEvent(()-> {
        this.setFightMode(false);
        this.setProjectileAttack(false);
    }, 80);
    };
    private final Consumer<EntityLivingBase> smash_attack = (target) -> {
        this.setSmashAttack(true);
        this.setFightMode(true);
        this.playSound(ModSoundHandler.AVALON_SMASH, 1.0f, 1.0f / (rand.nextFloat() * 0.4f + 0.5f));
        addEvent(()-> {
            this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f / rand.nextFloat() * 0.4f + 0.6f);
            world.setEntityState(this, ModUtils.SECOND_PARTICLE_BYTE);
            Vec3d posToo = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, -2, 0)));
            DamageSource source = ModDamageSource.builder()
                    .type(ModDamageSource.MOB)
                    .directEntity(this)
                    .build();
            float damage = (float) ((this.getAttack() - 2));
            ModUtils.handleAreaImpact(3.5f, (e) -> damage, this, posToo, source, 0.9F, 0, false );
        }, 23);
        addEvent(()-> {
            this.setFightMode(false);
            this.setSmashAttack(false);
        }, 40);
    };
    private final Consumer<EntityLivingBase> AOE_attack = (target) -> {
      this.setCastAOE(true);
      this.setFightMode(true);
        this.playSound(ModSoundHandler.AVALON_CAST, 1.0f, 1.0f / (rand.nextFloat() * 0.4f + 0.5f));
      double HealthFactor = this.getHealth()/this.getMaxHealth();
      int randomInterval = ModRand.range(1, 4);

      if(HealthFactor > 0.50) {
          addEvent(()-> new ActionShortRangeAOE().performAction(this, target), 15);
          addEvent(()-> new ActionMediumRangeAOE().performAction(this, target), 40);
          addEvent(()-> new ActionFarRange().performAction(this, target), 65);
      } else {
          if(randomInterval == 1) {
              addEvent(()-> new ActionShortRangeAOE().performAction(this, target), 15);
              addEvent(()-> new ActionMediumRangeAOE().performAction(this, target), 40);
              addEvent(()-> new ActionFarRange().performAction(this, target), 65);
          } else if (randomInterval == 2) {
              //Does Outer layers first then inner
              addEvent(()-> new ActionShortRangeAOE().performAction(this, target), 15);
              addEvent(()-> new ActionMediumRangeAOE().performAction(this, target), 40);
              addEvent(()-> new ActionFarRange().performAction(this, target), 15);
          }else if(randomInterval == 3) {
              //Does inner layer first then outer
              addEvent(()-> new ActionShortRangeAOE().performAction(this, target), 40);
              addEvent(()-> new ActionMediumRangeAOE().performAction(this, target), 15);
              addEvent(()-> new ActionFarRange().performAction(this, target), 40);
          }
          else {
              addEvent(()-> new ActionShortRangeAOE().performAction(this, target), 15);
              addEvent(()-> new ActionMediumRangeAOE().performAction(this, target), 40);
              addEvent(()-> new ActionFarRange().performAction(this, target), 65);
          }
      }
      addEvent(()-> {
        this.setFightMode(false);
        this.setCastAOE(false);
      }, 60);
    };

    private final Consumer<EntityLivingBase> AOE_line = (target) -> {
        this.playSound(ModSoundHandler.AVALON_CAST, 1.0f, 1.0f / (rand.nextFloat() * 0.4f + 0.5f));
      this.setCastAOE(true);
      this.setFightMode(true);

      addEvent(()-> {
        new ActionLineAOE().performAction(this, target);
      }, 25);
      addEvent(()-> {
          this.setFightMode(false);
          this.setCastAOE(false);
      }, 60);
    };
    private final Consumer<EntityLivingBase> summonLazerMinions = (target) -> {
      this.setCastLazers(true);
      this.setFightMode(true);
      double healthFactor = this.getHealth()/this.getMaxHealth();
        this.playSound(ModSoundHandler.AVALON_LAZER, 1.0f, 1.0f / (rand.nextFloat() * 0.4f + 0.5f));
      //summons the lazer minions
      addEvent(()-> {
        if(healthFactor >= 0.75) {
            EntityMiniValon valon = new EntityMiniValon(world, rand.nextBoolean());
            valon.setPosition(this.posX, this.posY -2, this.posZ);
            world.spawnEntity(valon);
        } else if(healthFactor < 0.75) {
            EntityMiniValon valon = new EntityMiniValon(world, rand.nextBoolean());
            valon.setPosition(this.posX, this.posY -2, this.posZ);
            world.spawnEntity(valon);
            EntityMiniValon valon2 = new EntityMiniValon(world, rand.nextBoolean());
            valon2.setPosition(this.posX, this.posY -1, this.posZ);
            world.spawnEntity(valon2);
        }
      }, 27);
      addEvent(()-> {
        this.setCastLazers(false);
        this.setFightMode(false);
      }, 40);
    };

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSoundHandler.AVALON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundHandler.AVALON_DEATH;
    }
    private static final ResourceLocation LOOT_BOSS = new ResourceLocation(ModReference.MOD_ID, "avalon");

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_BOSS;
    }

    @Override
    protected boolean canDropLoot() {
        return true;
    }

    @Override
    public void onDeath(DamageSource cause) {
        if(this.isIAmBoss()) {
            this.setDeathState(true);
            this.setHealth(0.0001f);
            if(this.isDeathState()) {
                List<EntityMiniValon> nearbySwords = this.world.getEntitiesWithinAABB(EntityMiniValon.class, this.getEntityBoundingBox().grow(30D), e -> !e.getIsInvulnerable());
                if(!nearbySwords.isEmpty()) {
                    for(EntityMiniValon valon : nearbySwords) {
                        valon.setDead();
                    }
                }
                addEvent(()-> this.playSound(ModSoundHandler.AVALON_DEATH, 1.5f, 1.0f), 0);
                addEvent(()-> this.setDeathState(false), 100);
                addEvent(this::setDead, 100);
                addEvent(()-> this.setDropItemsWhenDead(true), 90);
                addEvent(()-> {
                    if(!world.isRemote) {
                        EntityAvalon avalon = new EntityAvalon(world);
                        avalon.copyLocationAndAnglesFrom(this);
                        world.spawnEntity(avalon);
                    }
                }, 98);
            }
        }
        super.onDeath(cause);
    }
}
