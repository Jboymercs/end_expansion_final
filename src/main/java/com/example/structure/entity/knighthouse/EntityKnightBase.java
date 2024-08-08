package com.example.structure.entity.knighthouse;

import akka.japi.pf.FI;
import com.example.structure.entity.EntityBuffker;
import com.example.structure.entity.EntityController;
import com.example.structure.entity.EntityCrystalKnight;
import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.ai.EntityAIAvoidCrowding;
import com.example.structure.entity.ai.EntityAIWanderWithGroup;
import com.example.structure.entity.endking.EntityAbstractEndKing;
import com.example.structure.entity.endking.ghosts.EntityGhostPhase;
import com.example.structure.entity.seekers.EndSeeker;
import com.example.structure.entity.seekers.EndSeekerPrime;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModReference;
import com.example.structure.util.handlers.ModSoundHandler;
import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;


public abstract class EntityKnightBase extends EntityModBase {

    private static final DataParameter<Integer> SKIN_TYPE = EntityDataManager.<Integer>createKey(EntityKnightBase.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> FIGHT_MODE = EntityDataManager.createKey(EntityKnightBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> MARKED_FOR_UNHOLY = EntityDataManager.createKey(EntityKnightBase.class, DataSerializers.BOOLEAN);

    public void setFightMode(boolean value) {this.dataManager.set(FIGHT_MODE, Boolean.valueOf(value));}
    public boolean isFightMode() {return this.dataManager.get(FIGHT_MODE);}
    public boolean isMarkedForUnholy() {return this.dataManager.get(MARKED_FOR_UNHOLY);}
    public void setMarkedForUnholy(boolean value) {this.dataManager.set(MARKED_FOR_UNHOLY, Boolean.valueOf(value));}
    /**
     * Allows me to insert specific AI and make them seem like they work more like a group
     * @param worldIn
     * @param x
     * @param y
     * @param z
     */

    public EntityKnightBase(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.experienceValue = 40;
    }


    public EntityKnightBase(World worldIn) {
        super(worldIn);
        this.experienceValue = 40;
    }


    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(6, new EntityAIWanderWithGroup(this, 1.0D));
        this.tasks.addTask(7, new EntityAIAvoidCrowding(this, 1.0D));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityCrystalKnight>(this, EntityCrystalKnight.class, 1, true, false, null));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<EntityBuffker>(this, EntityBuffker.class, 1, true, false, null));
        this.targetTasks.addTask(4, new EntityAINearestAttackableTarget<EndSeeker>(this, EndSeeker.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAINearestAttackableTarget<EndSeekerPrime>(this, EndSeekerPrime.class, 1, true, false, null));
        this.targetTasks.addTask(6, new EntityAINearestAttackableTarget<EntityController>(this, EntityController.class, 1, true, false, null));
        this.targetTasks.addTask(7, new EntityAIHurtByTarget(this, true));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(SKIN_TYPE, Integer.valueOf(0));
        this.dataManager.register(FIGHT_MODE, Boolean.valueOf(false));
        this.dataManager.register(MARKED_FOR_UNHOLY, Boolean.valueOf(false));
    }


    public static boolean isFriendlyKnight(Entity entity) {
        return !CAN_TARGET.apply(entity);
    }
    public static final Predicate<Entity> CAN_TARGET = entity -> {

        return !(entity instanceof EntityKnightBase || entity instanceof EntityAbstractEndKing || entity instanceof EntityGhostPhase);
    };
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (!CAN_TARGET.apply(source.getTrueSource())) {
            return false;
        }
        return super.attackEntityFrom(source, amount);
    }

    public int getSkin()
    {
        return this.dataManager.get(SKIN_TYPE).intValue();
    }

    //Hopefully this will fix the weird skin issues going across the models
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData entityLivingData) {
        if(!(this instanceof EntityKnightLord)) {
            this.setSkin(rand.nextInt(5));
        }
        return super.onInitialSpawn(difficulty, entityLivingData);
    }

    public void setSkin(int skinType)
    {
        this.dataManager.set(SKIN_TYPE, Integer.valueOf(skinType));
    }


    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setInteger("Variant", getSkin());
      //  nbt.setBoolean("Fight_Mode", this.dataManager.get(FIGHT_MODE));
     //   nbt.setBoolean("Marked_For_Unholy", this.dataManager.get(MARKED_FOR_UNHOLY));

        nbt.setBoolean("Fight_Mode", this.isFightMode());
        nbt.setBoolean("Marked_For_Unholy", this.isMarkedForUnholy());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        setSkin(nbt.getInteger("Variant"));
      //  this.dataManager.set(FIGHT_MODE, nbt.getBoolean("Fight_Mode"));
       // this.dataManager.set(MARKED_FOR_UNHOLY, nbt.getBoolean("Marked_For_Unholy"));

        this.setFightMode(nbt.getBoolean("Fight_Mode"));
        this.setMarkedForUnholy(nbt.getBoolean("Marked_For_Unholy"));
    }

    @Override
    protected boolean canDespawn() {

            // Edit this to restricting them not despawning in Dungeons
            return this.ticksExisted > 20 * 60 * 20;

    }



    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSoundHandler.KNIGHT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundHandler.KNIGHT_DEATH;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSoundHandler.KNIGHT_IDLE;
    }


    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(ModSoundHandler.KNIGHT_STEP, 0.4F, 1.0f + ModRand.getFloat(0.3F));
    }

    private static final ResourceLocation LOOT = new ResourceLocation(ModReference.MOD_ID, "knighthouse");
    @Override
    protected ResourceLocation getLootTable() {
        return LOOT;
    }
    @Override
    protected boolean canDropLoot() {
        return true;
    }
}
