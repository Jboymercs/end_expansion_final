package com.example.structure.entity;


import com.example.structure.entity.ai.EntityAIRandomFly;
import com.example.structure.entity.knighthouse.EntityEnderMage;
import com.example.structure.entity.knighthouse.EntityEnderShield;
import com.example.structure.entity.trader.EntityAOEArena;
import com.example.structure.entity.trader.EntityControllerLift;
import com.example.structure.init.ModBlocks;
import com.example.structure.init.ModItems;
import com.example.structure.util.ModColors;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModReference;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ModSoundHandler;
import com.example.structure.util.handlers.ParticleManager;
import com.google.common.base.Optional;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.ai.EntityAITimedAttack;
import com.example.structure.entity.ai.EntityFlyMoveHelper;
import com.example.structure.entity.util.IAttack;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;


public class EntityController extends EntityModBase implements IAnimatable, IAttack, IAnimationTickable {

    public boolean isHuntingBlock = false;
    private static final DataParameter<Optional<IBlockState>> BLOCK_HEAD = EntityDataManager.<Optional<IBlockState>>createKey(EntityController.class, DataSerializers.OPTIONAL_BLOCK_STATE);

    protected static final DataParameter<Boolean> CONTROLLER_MODE = EntityDataManager.createKey(EntityController.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> INTERACT = EntityDataManager.createKey(EntityController.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> SHOOT_PROJECTILES = EntityDataManager.createKey(EntityController.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> SUMMON_LIFT_ATTACK = EntityDataManager.createKey(EntityController.class, DataSerializers.BOOLEAN);

    public void setFightMode(boolean value) {this.dataManager.set(CONTROLLER_MODE, Boolean.valueOf(value));}
    public boolean isFightMode() {return this.dataManager.get(CONTROLLER_MODE);}
    public void setInteract(boolean value) {this.dataManager.set(INTERACT, Boolean.valueOf(value));}
    public boolean isInteract() {return this.dataManager.get(INTERACT);}
    public void setShootProjectiles(boolean value) {this.dataManager.set(SHOOT_PROJECTILES, Boolean.valueOf(value));}
    public boolean isShootProjectiles() {return this.dataManager.get(SHOOT_PROJECTILES);}
    public void setSummonLiftAttack(boolean value) {this.dataManager.set(SUMMON_LIFT_ATTACK, Boolean.valueOf(value));}
    public boolean isSummonLiftAttack() {return this.dataManager.get(SUMMON_LIFT_ATTACK);}
    private AnimationFactory factory = new AnimationFactory(this);
    private final String ANIM_IDLE = "idle";
    private final String ANIM_MOVE = "walk";
    private final String ANIM_FLY = "fly";

    private final String ANIM_INTERACT = "interact";
    private final String ANIM_SHOOT = "shoot";
    private final String ANIM_LIFT = "lift";

    private static final ResourceLocation LOOT = new ResourceLocation(ModReference.MOD_ID, "controller");

    //This is the current block rendered and one that the Controller is hunting for
    protected IBlockState currentBlock;

    //This is the output of that hunted block the Controller is looking for
    protected IBlockState changeTooBlock;

    public EntityController(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.moveHelper = new EntityFlyMoveHelper(this);
        this.navigator = new PathNavigateFlying(this, world);
        this.setNoGravity(true);
        this.setSize(0.7F, 2.5F);
    }


    @Override
    public boolean getCanSpawnHere() {
        // Middle end island check
        if (this.world.provider.getDimension() == 1 && world.rand.nextInt(28) == 0) {
            return !ModConfig.does_spawn_middle ? Math.abs(this.posX) > 500 || Math.abs(this.posZ) > 500 : true;
        }

        return super.getCanSpawnHere();
    }


    public EntityController(World worldIn) {
        super(worldIn);
        this.moveHelper = new EntityFlyMoveHelper(this);
        this.navigator = new PathNavigateFlying(this, world);
        this.setNoGravity(true);
        this.setSize(0.7F, 2.5F);
    }


    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if(stack.getItem() == ModItems.RED_CRYSTAL_ITEM) {
            //Change Head to Red Crystals, Hunt Purple Crystals
            this.equipBlock(CONTROLLER_HEAD.HEAD, ModBlocks.RED_CRYSTAL);
            currentBlock = ModBlocks.RED_CRYSTAL.getDefaultState();
            return true;
        } else if(stack.getItem() == ModItems.PURPLE_CRYSTAL_ITEM) {
            //Change Head to Purple Crystals, Hunt Red Crystals
            this.equipBlock(CONTROLLER_HEAD.HEAD, ModBlocks.PURPLE_CRYSTAL);
            currentBlock = ModBlocks.PURPLE_CRYSTAL.getDefaultState();
            return true;
        } else if (stack.getItem() == Items.WHEAT_SEEDS) {
            this.equipBlock(CONTROLLER_HEAD.HEAD, ModBlocks.END_ASH);
            currentBlock = ModBlocks.END_ASH.getDefaultState();
        } else if(stack.getItem() == Items.STICK) {
            this.equipBlock(CONTROLLER_HEAD.HEAD, ModBlocks.BARE_BARK);
            currentBlock = ModBlocks.BARE_BARK.getDefaultState();
        } else if(stack.getItem() == ModItems.AMBER_RAW_ORE) {
            this.equipBlock(CONTROLLER_HEAD.HEAD, ModBlocks.SPROUT_STONE);
            currentBlock = ModBlocks.SPROUT_STONE.getDefaultState();
        } else if(stack.getItem() == Items.PORKCHOP) {
            this.equipBlock(CONTROLLER_HEAD.HEAD, Blocks.DIAMOND_BLOCK);
            currentBlock = Blocks.DIAMOND_BLOCK.getDefaultState();
        } else if(stack.getItem() == Items.DIAMOND) {
            this.equipBlock(CONTROLLER_HEAD.HEAD, ModBlocks.AMBER_ORE);
            currentBlock = ModBlocks.AMBER_ORE.getDefaultState();
        } else if(stack.getItem() == Items.WATER_BUCKET) {
            this.equipBlock(CONTROLLER_HEAD.HEAD, ModBlocks.LAMENTED_END_STONE);
            currentBlock = ModBlocks.LAMENTED_END_STONE.getDefaultState();
        } else if(stack.getItem() == ModItems.HEAL_FOOD) {
            this.equipBlock(CONTROLLER_HEAD.HEAD, ModBlocks.END_HEAL_PLANT);
            currentBlock = ModBlocks.END_HEAL_PLANT.getDefaultState();
        }
        return super.processInteract(player, hand);
    }



    @Override
    protected void entityInit() {
        super.entityInit();
    this.dataManager.register(BLOCK_HEAD,  Optional.of(Blocks.BARRIER.getDefaultState()));
    this.dataManager.register(CONTROLLER_MODE, Boolean.valueOf(false));
    this.dataManager.register(INTERACT, Boolean.valueOf(false));
    this.dataManager.register(SUMMON_LIFT_ATTACK, Boolean.valueOf(false));
    this.dataManager.register(SHOOT_PROJECTILES, Boolean.valueOf(false));
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }
    @Override
    protected void updateFallState(double y, boolean onGroundIn, @Nonnull IBlockState state, @Nonnull BlockPos pos) {
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAIControllerFly(this));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.tasks.addTask(4, new EntityAITimedAttack<>(this, 1.2, 80, 12f, 0.45f));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityEnderKnight>(this, EntityEnderKnight.class, 1, true, false, null));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<EntityEnderMage>(this, EntityEnderMage.class, 1, true, false, null));
        this.targetTasks.addTask(4, new EntityAINearestAttackableTarget<EntityEnderShield>(this, EntityEnderShield.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAINearestAttackableTarget<EntitySnatcher>(this, EntitySnatcher.class, 1, true, false, null));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(ModConfig.guilder_health * ModConfig.lamented_multiplier);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.13D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.5D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4.0D);
    }

    public int startBlockSearch = 80;
    public int checkNearbyPlayers = 60;

    protected int blockTooAttempts = 200;
    @Override
    public void onUpdate() {
        super.onUpdate();
        EntityLivingBase target = this.getAttackTarget();


        if(target != null) {
            if(!this.isBeingRidden()) {
                double distSq = this.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
                double distance = Math.sqrt(distSq);
                if(distance < 7) {
                    double d0 = (this.posX - target.posX) * 0.015;
                    double d1 = (this.posY - target.posY) * 0.01;
                    double d2 = (this.posZ - target.posZ) * 0.015;
                    this.addVelocity(d0, d1, d2);
                }
            }
            if(target.isDead) {
                this.setAttackTarget(null);
            }
        }

        if(target == null && !this.isFightMode()) {

            //Checks for nearby players, if they are holding certain equipment they will aggro on them
            if(checkNearbyPlayers < 0) {
                List<EntityPlayer> nearbyEntities = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(10D), e -> !e.getIsInvulnerable());
                if(!nearbyEntities.isEmpty()) {
                    for(EntityPlayer player : nearbyEntities) {
                        if (!player.isCreative() && !player.isSpectator()) {
                            if (player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.DARK_HELMET || player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == ModItems.DARK_CHESTPLATE ||
                                    player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() == ModItems.UNHOLY_AXE || player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() == ModItems.RED_CRYSTAL_CHUNK || player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() == ModItems.KNIGHT_SWORD) {
                                this.setAttackTarget(player);
                                this.playSound(ModSoundHandler.GUILDER_AGGRO, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.2f));
                            }
                        }
                    }
                }
                checkNearbyPlayers = 40;
            } else {
                checkNearbyPlayers--;
            }
            if(startBlockSearch < 0) {
                this.isHuntingBlock = true;
                //Search for Blocks within it's area
                AxisAlignedBB box = getEntityBoundingBox().grow(8, 8, 8);
                BlockPos pos = ModUtils.searchForBlocks(box, world, this, currentBlock);
                //Check to see if a block of that type exists
                if(ModUtils.searchForBlocks(box, world, this, currentBlock) != null) {
                    Vec3d lookPos = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
                    this.getMoveHelper().setMoveTo(pos.getX(), pos.getY() + 2, pos.getZ(), 0.35);
                    ModUtils.facePosition(lookPos, this, 45, 45);
                    Vec3d distanceFrom = new Vec3d(pos.getX(), pos.getY() + 2, pos.getZ());
                    double distSq = this.getDistanceSq(distanceFrom.x, distanceFrom.y, distanceFrom.z);
                    double distance = Math.sqrt(distSq);
                    //Found Block move to it's Location
                    if(distance > 3) {
                        blockTooAttempts--;
                    } else {
                        //Start to change Block
                        if(!this.isInteract()) {
                            changeBlock(pos, currentBlock);
                        }
                    }
                    //Failed to reach location in time, start over
                    if(blockTooAttempts < 0) {
                        startBlockSearch = 80;
                        this.isHuntingBlock = false;
                        blockTooAttempts = 200;
                    }
                } else {
                    startBlockSearch = 80;
                    blockTooAttempts = 200;
                    this.isHuntingBlock = false;
                }
            } else {
                startBlockSearch--;
            }
        }
    }

    public void changeBlock(BlockPos pos, IBlockState currentBlock) {
        this.setImmovable(true);
        this.setInteract(true);

        this.playSound(ModSoundHandler.GUILDER_CHANGE, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.5f));
        addEvent(()-> {
            for(int i = 0; i < 20; i++) {
                addEvent(()-> world.setEntityState(this, ModUtils.PARTICLE_BYTE), i);
            }
        }, 5);

        addEvent(()-> {
            for(int i = 0; i< 20; i++) {
                addEvent(()-> ParticleManager.spawnColoredSmoke(world, new Vec3d(pos.getX() + rand.nextDouble(), pos.getY() + 1.1f, pos.getZ() + rand.nextDouble()), ModColors.AZURE, new Vec3d(0, 0.1, 0)), i);
            }
        }, 20);
        addEvent(()-> {

            if(world.getBlockState(pos) == currentBlock) {
                //Red To Purple
                if(currentBlock == ModBlocks.RED_CRYSTAL.getDefaultState()) {
                    this.playSound(SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, 0.6f, 1.0f / (rand.nextFloat() * 0.4F + 0.5f));
                    world.setBlockState(pos, ModBlocks.PURPLE_CRYSTAL.getDefaultState());
                }
                //Purple To red
                if(currentBlock == ModBlocks.PURPLE_CRYSTAL.getDefaultState()) {
                    this.playSound(SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, 0.6f, 1.0f / (rand.nextFloat() * 0.4F + 0.5f));
                    world.setBlockState(pos, ModBlocks.RED_CRYSTAL.getDefaultState());
                }
                //End STone to Grass
                if(currentBlock == ModBlocks.END_ASH.getDefaultState()) {
                    this.playSound(SoundEvents.BLOCK_GRASS_PLACE, 0.6f, 1.0f / (rand.nextFloat() * 0.4F + 0.5f));
                    world.setBlockState(pos, Blocks.GRASS.getDefaultState());
                }
                //Barrend Wood to regular Wood
                if(currentBlock == ModBlocks.BARE_BARK.getDefaultState()) {
                    this.playSound(SoundEvents.BLOCK_WOOD_PLACE, 0.6f, 1.0f / (rand.nextFloat() * 0.4F + 0.5f));
                    world.setBlockState(pos, Blocks.LOG.getDefaultState());
                }
                //Sprout Stone to Coal Ore
                if(currentBlock == ModBlocks.SPROUT_STONE.getDefaultState()) {
                    this.playSound(SoundEvents.BLOCK_STONE_PLACE, 0.6f, 1.0f / (rand.nextFloat() * 0.4F + 0.5f));
                    world.setBlockState(pos, Blocks.COAL_ORE.getDefaultState());
                }
                //Diamond Block to Monster Spawner
                if(currentBlock == Blocks.DIAMOND_BLOCK.getDefaultState()) {
                    this.playSound(SoundEvents.BLOCK_METAL_PLACE, 0.6f, 1.0f / (rand.nextFloat() * 0.4F + 0.5f));
                    world.setBlockState(pos, Blocks.MOB_SPAWNER.getDefaultState());
                }
                //Amber Ore to Diamond Ore
                if(currentBlock == ModBlocks.AMBER_ORE.getDefaultState()) {
                    this.playSound(SoundEvents.BLOCK_STONE_PLACE, 0.6f, 1.0f / (rand.nextFloat() * 0.4F + 0.5f));
                    world.setBlockState(pos, Blocks.DIAMOND_ORE.getDefaultState());
                }
                //Lamented End Stone to Water
                if(currentBlock == ModBlocks.LAMENTED_END_STONE.getDefaultState()) {
                    this.playSound(SoundEvents.ITEM_BUCKET_FILL, 0.6f, 1.0f / (rand.nextFloat() * 0.4F + 0.5f));
                    world.setBlockState(pos, Blocks.WATER.getDefaultState());
                }
                //Turium Plant To Sapling
                if(currentBlock == ModBlocks.END_HEAL_PLANT.getDefaultState()) {
                    this.playSound(SoundEvents.BLOCK_GRASS_PLACE, 0.6f, 1.0f / (rand.nextFloat() * 0.4F + 0.5f));
                    world.setBlockState(pos, Blocks.SAPLING.getDefaultState());
                }
            }
        }, 25);

        addEvent(()-> {
            startBlockSearch = 80;
            this.isHuntingBlock = false;
            blockTooAttempts = 200;
            this.setImmovable(false);
            this.setInteract(false);
        }, 40);
    }

    private Consumer<EntityLivingBase> prevAttack;

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if(id == ModUtils.PARTICLE_BYTE) {
            ParticleManager.spawnColoredSmoke(world, getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 1.2, 0))), ModColors.AZURE, new Vec3d(ModRand.getFloat(1) * 0.05,ModRand.getFloat(1) * 0.05,ModRand.getFloat(1) * 0.05));
        }
    }
    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        if(!this.isFightMode()) {
            List<Consumer<EntityLivingBase>> attacks = new ArrayList<>(Arrays.asList(summonProjectiles, summonLift));
            double[] weights = {
                    (distance < 13) ? 1/distance : 0, //Summon Projectiles
                    (distance < 13 && prevAttack != summonLift) ? 1/distance : 0 //Summon Lift
            };
            prevAttack = ModRand.choice(attacks, rand, weights).next();

            prevAttack.accept(target);
        }
        return 80;
    }

    //Shoots Pure Projectile Crystals
    private final Consumer<EntityLivingBase> summonProjectiles = (target) -> {
        this.setFightMode(true);
        this.setShootProjectiles(true);

        addEvent(()-> this.playSound(SoundEvents.ENTITY_ILLAGER_CAST_SPELL, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f)), 10);
        addEvent(()-> {
            for (int i = 0; i < 40; i += 4) {
                addEvent(() -> {
                    this.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 0.4f, 1.0f + ModRand.getFloat(0.2F));
                    float damage =ModConfig.guilder_attack_damage;
                    ProjectilePurple projectile = new ProjectilePurple(this.world, this, damage);
                    Vec3d pos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(ModRand.getFloat(2), 3, ModRand.getFloat(2))));
                    Vec3d targetPos = new Vec3d(target.posX + ModRand.getFloat(3) - 1, target.posY, target.posZ + ModRand.getFloat(3) - 1);
                    Vec3d velocity = targetPos.subtract(pos).normalize().scale(0.70f);
                    projectile.setPosition(pos.x, pos.y, pos.z);
                    projectile.setTravelRange(30f);
                    ModUtils.setEntityVelocity(projectile, velocity);
                    world.spawnEntity(projectile);
                }, i);
            }
        }, 20);

        addEvent(()-> {
            this.setShootProjectiles(false);
            this.setFightMode(false);
        }, 35);
    };
    //Summons An AOE that will shoot the player up in the air
    private final Consumer<EntityLivingBase> summonLift = (target) -> {
      this.setFightMode(true);
      this.setShootProjectiles(true);

        addEvent(()-> this.playSound(SoundEvents.ENTITY_ILLAGER_CAST_SPELL, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f)), 20);
      addEvent(()-> {
          if(target != null) {
              ModUtils.circleCallback(0, 1, (pos)-> {
                  pos = new Vec3d(pos.x, 0, pos.y).add(target.getPositionVector());
                  EntityControllerLift spike = new EntityControllerLift(world);
                  spike.setPosition(pos.x, pos.y, pos.z);
                  world.spawnEntity(spike);
              });
              ModUtils.circleCallback(1, 8, (pos)-> {
                  pos = new Vec3d(pos.x, 0, pos.y).add(target.getPositionVector());
                  EntityControllerLift spike = new EntityControllerLift(world);
                  spike.setPosition(pos.x, pos.y, pos.z);
                  world.spawnEntity(spike);
              });
              ModUtils.circleCallback(2, 16, (pos)-> {
                  pos = new Vec3d(pos.x, 0, pos.y).add(target.getPositionVector());
                  EntityControllerLift spike = new EntityControllerLift(world);
                  spike.setPosition(pos.x, pos.y, pos.z);
                  world.spawnEntity(spike);
              });
          }
      }, 30);

      addEvent(()-> {
          this.setFightMode(false);
          this.setSummonLiftAttack(false);
      }, 45);
    };

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        animationData.addAnimationController(new AnimationController(this, "fly_controller", 0, this::predicateFly));
        animationData.addAnimationController(new AnimationController(this, "interact_controller", 0, this::predicateInteractions));
    }

    private<E extends IAnimatable> PlayState predicateFly(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_FLY, true));
        return PlayState.CONTINUE;
    }

    private<E extends IAnimatable> PlayState predicateInteractions(AnimationEvent<E> e) {
        if(this.isFightMode()) {
            if(this.isShootProjectiles()) {
                e.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SHOOT, false));
                return PlayState.CONTINUE;
            }
            if(this.isSummonLiftAttack()) {
                e.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_LIFT, false));
                return PlayState.CONTINUE;
            }
        }
        if(this.isInteract()) {
            e.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_INTERACT, false));
            return PlayState.CONTINUE;
        }
        e.getController().markNeedsReload();
        return PlayState.STOP;
    }
    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
            if(!this.isInteract() && !this.isFightMode()) {
                if (event.isMoving()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_MOVE, true));

                } else {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));

                }
                return PlayState.CONTINUE;

            }
            return PlayState.STOP;
    }

    @Override
    protected boolean canDropLoot() {
        return true;
    }

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT;
    }

    @Override
    public void tick() {

    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSoundHandler.GUILDER_IDLE;
    }
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSoundHandler.GUILDER_HURT;
    }
    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundHandler.GUILDER_HURT;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {

    }

    @Override
    protected boolean canDespawn()
    {
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.getEntityBoundingBox().minY);
        int k = MathHelper.floor(this.posZ);
        BlockPos blockpos = new BlockPos(i, j, k);

        return this.world.getLight(blockpos) <= 10;
    }


    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }

    public enum CONTROLLER_HEAD {
        HEAD("HeadTop");

        public String getBoneName() {
            return this.boneName;
        }

        private String boneName;

        CONTROLLER_HEAD(String bone) {
        this.boneName = bone;
        }

        @Nullable
        public static CONTROLLER_HEAD getFromBoneName(String boneName) {
            if ("HeadTop".equals(boneName)) {
                return HEAD;
            }
            return null;
        }

        public int getIndex() {
            if (this == CONTROLLER_HEAD.HEAD) {
                return 1;
            }
            return 0;
        }
    }

    public void removeHandBlock(CONTROLLER_HEAD hand) {
        Optional<IBlockState> value = Optional.absent();
        this.equipBlock(hand, value);
    }

    public void equipBlock(CONTROLLER_HEAD head, Optional<IBlockState> state) {
        if(world.isRemote) {
            return;
        }
        if (head == CONTROLLER_HEAD.HEAD) {
            this.dataManager.set(BLOCK_HEAD, state);
        }
    }

    public void equipBlock(CONTROLLER_HEAD hand, Block block) {
        this.equipBlock(hand, block.getDefaultState());
    }

    public void equipBlock(CONTROLLER_HEAD hand, IBlockState blockstate) {
        this.equipBlock(hand, Optional.of(blockstate));
    }

    public Optional<IBlockState> getBlockFromHead(CONTROLLER_HEAD head) {
        if (head == CONTROLLER_HEAD.HEAD) {
            return this.dataManager.get(BLOCK_HEAD);
        }
        return Optional.absent();
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
