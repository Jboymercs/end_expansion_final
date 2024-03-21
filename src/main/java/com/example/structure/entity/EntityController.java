package com.example.structure.entity;


import com.example.structure.entity.ai.EntityAIRandomFly;
import com.example.structure.init.ModBlocks;
import com.example.structure.init.ModItems;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModReference;
import com.example.structure.util.ModUtils;
import com.google.common.base.Optional;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.ai.EntityAITimedAttack;
import com.example.structure.entity.ai.EntityFlyMoveHelper;
import com.example.structure.entity.util.IAttack;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.Sys;
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
import java.util.List;


public class EntityController extends EntityModBase implements IAnimatable, IAttack, IAnimationTickable {

    public boolean isHuntingBlock = false;
    private static final DataParameter<Optional<IBlockState>> BLOCK_HEAD = EntityDataManager.<Optional<IBlockState>>createKey(EntityController.class, DataSerializers.OPTIONAL_BLOCK_STATE);

    protected static final DataParameter<Boolean> FIGHT_MODE = EntityDataManager.createKey(EntityController.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> INTERACT = EntityDataManager.createKey(EntityController.class, DataSerializers.BOOLEAN);

    public void setFightMode(boolean value) {this.dataManager.set(FIGHT_MODE, Boolean.valueOf(value));}
    public boolean isFightMode() {return this.dataManager.get(FIGHT_MODE);}
    public void setInteract(boolean value) {this.dataManager.set(INTERACT, Boolean.valueOf(value));}
    public boolean isInteract() {return this.dataManager.get(INTERACT);}
    private AnimationFactory factory = new AnimationFactory(this);
    private final String ANIM_IDLE = "idle";
    private final String ANIM_MOVE = "walk";
    private final String ANIM_FLY = "fly";

    private final String ANIM_INTERACT = "interact";

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
    }


    @Override
    public boolean getCanSpawnHere() {
        // Middle end island check
        if (this.world.provider.getDimension() == 1 && this.world.rand.nextInt(6) == 0) {
            return ModConfig.does_spawn_middle || this.posX > 500 || this.posX < -500 || this.posZ > 500 || this.posZ < -500;
        }

        return super.getCanSpawnHere();
    }


    public EntityController(World worldIn) {
        super(worldIn);
        this.moveHelper = new EntityFlyMoveHelper(this);
        this.navigator = new PathNavigateFlying(this, world);
        this.setNoGravity(true);
    }


    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if(stack.getItem() == ModItems.RED_CRYSTAL_ITEM) {
            //Change Head to Red Crystals, Hunt Purple Crystals
            this.equipBlock(CONTROLLER_HEAD.HEAD, ModBlocks.RED_CRYSTAL);
            currentBlock = ModBlocks.PURPLE_CRYSTAL.getDefaultState();
            return true;
        } else if(stack.getItem() == ModItems.PURPLE_CRYSTAL_ITEM) {
            //Change Head to Purple Crystals, Hunt Red Crystals
            this.equipBlock(CONTROLLER_HEAD.HEAD, ModBlocks.PURPLE_CRYSTAL);
            currentBlock = ModBlocks.RED_CRYSTAL.getDefaultState();
            return true;
        }
        return super.processInteract(player, hand);
    }



    @Override
    protected void entityInit() {
        super.entityInit();
    this.dataManager.register(BLOCK_HEAD,  Optional.of(ModBlocks.ASH_BRICK.getDefaultState()));
    this.dataManager.register(FIGHT_MODE, Boolean.valueOf(false));
    this.dataManager.register(INTERACT, Boolean.valueOf(false));
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
        this.tasks.addTask(4, new EntityAIRandomFly(this));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.tasks.addTask(4, new EntityAITimedAttack<>(this, 1.2, 60, 3.0f, 0.2f));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25D * ModConfig.lamented_multiplier);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.13D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.5D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4.0D);
    }

    public int startBlockSearch = 80;

    protected int blockTooAttempts = 200;
    @Override
    public void onUpdate() {
        super.onUpdate();
        EntityLivingBase target = this.getAttackTarget();
        List<EntityLivingBase> nearbyEntities = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(10D), e -> !e.getIsInvulnerable());


        if(target == null) {
            if(startBlockSearch < 0) {
                this.isHuntingBlock = true;
                //Search for Blocks within it's area
                AxisAlignedBB box = getEntityBoundingBox().grow(8, 8, 8);
                BlockPos pos = ModUtils.searchForBlocks(box, world, this, currentBlock);
                //Check to see if a block of that type exists
                if(ModUtils.searchForBlocks(box, world, this, currentBlock) != null) {
                    System.out.println("found Block at Coords" + pos);
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
                            System.out.println("Changing Block");
                            changeBlock(pos, currentBlock);
                        }
                    }
                    //Failed to reach location in time, start over
                    if(blockTooAttempts < 0) {
                        startBlockSearch = 80;
                        this.isHuntingBlock = false;
                        blockTooAttempts = 200;
                    }
                }
            } else {
                startBlockSearch--;
            }
        }
    }

    public void changeBlock(BlockPos pos, IBlockState currentBlock) {
        this.setImmovable(true);
        this.setInteract(true);

        addEvent(()-> {

            if(world.getBlockState(pos) == currentBlock) {
                //Red To Purple
                if(currentBlock == ModBlocks.RED_CRYSTAL.getDefaultState()) {
                    world.setBlockState(pos, ModBlocks.PURPLE_CRYSTAL.getDefaultState());
                }
                //Purple To red
                if(currentBlock == ModBlocks.PURPLE_CRYSTAL.getDefaultState()) {
                    world.setBlockState(pos, ModBlocks.RED_CRYSTAL.getDefaultState());
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

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        return 0;
    }

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
        if(this.isInteract()) {
            e.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_INTERACT, false));
            return PlayState.CONTINUE;
        }
        e.getController().markNeedsReload();
        return PlayState.STOP;
    }
    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
            if(!this.isInteract()) {
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
