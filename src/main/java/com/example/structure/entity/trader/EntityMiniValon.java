package com.example.structure.entity.trader;

import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.ai.IMultiAction;
import com.example.structure.entity.endking.EndKingAction.ActionShootLazer;
import com.example.structure.entity.trader.action.ActionMiniValonLazer;
import com.example.structure.renderer.ITarget;
import com.example.structure.util.DirectionalRender;
import com.example.structure.util.ModColors;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ModSoundHandler;
import com.example.structure.util.handlers.ParticleManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.Optional;

public class EntityMiniValon extends EntityModBase implements IAnimatable, ITarget, DirectionalRender {
    public Vec3d renderLazerPos;
    public Vec3d prevRenderLazerPos;

    public IMultiAction lazerAttack =  new ActionMiniValonLazer(this, stopLazerByte, (vec3d) -> {});

    protected boolean performLazerAttack = false;

    private AnimationFactory factory = new AnimationFactory(this);

    public EntityMiniValon(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(1.0F, 0.6F);
        this.rotationYaw = 0;
        this.rotationYawHead = 0;
        this.setNoAI(true);
        this.setNoGravity(true);
    }

    public EntityMiniValon(World worldIn) {
        super(worldIn);
        this.setSize(1.0F, 0.6F);
        this.rotationYaw = 0;
        this.rotationYawHead = 0;
        this.setNoAI(true);
        this.setNoGravity(true);
    }

    public EntityMiniValon(World worldIn, boolean isLeft) {
        super(worldIn);
        this.setSize(1.0F, 0.6F);
        this.rotationYaw = 0;
        this.rotationYawHead = 0;
        this.setNoAI(true);
        this.setNoGravity(true);
        this.isLeft = isLeft;
    }

    protected boolean isLeft = false;



    public void onUpdate() {
        super.onUpdate();


        Vec3d modifyPosToo = this.getLookVec();
        Vec3d tooFrom = new Vec3d(this.posX + modifyPosToo.x * 10, this.posY + modifyPosToo.y, this.posZ + modifyPosToo.z * 10);
        System.out.println("Look Vec at" + tooFrom);

        //setting Look to use this instead
        if(ticksExisted == 2) {
            this.doLazerAttackCurrently();

        }
        if(ticksExisted <= 20) {
            this.rotationYaw = 0;
            this.rotationYawHead = 0;
        }

        if(!this.isLeft) {
            if(ticksExisted > 20) {
                this.rotationYaw++;
                this.rotationYawHead++;
            }

        } else {
            if (ticksExisted > 20) {
                this.rotationYaw--;
                this.rotationYawHead--;
            }
        }

        if(ticksExisted == 420) {
            this.setDead();
        }
    }

    public void doLazerAttackCurrently() {
        lazerAttack.doAction();
        this.performLazerAttack = true;

        addEvent(()-> this.performLazerAttack = false, 420);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(!world.isRemote && performLazerAttack) {
            lazerAttack.update();
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    protected static final byte stopLazerByte = 39;

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if (id == stopLazerByte) {
            this.renderLazerPos = null;
        }        else if(id == ModUtils.PARTICLE_BYTE) {
            for (int i = 0; i < 5; i++) {
                Vec3d lookVec = ModUtils.getLookVec(0, this.renderYawOffset);
                Vec3d randOffset = ModUtils.rotateVector2(lookVec, lookVec.crossProduct(ModUtils.Y_AXIS), ModRand.range(-70, 70));
                randOffset = ModUtils.rotateVector2(randOffset, lookVec, ModRand.range(0, 360)).scale(1.5f);
                Vec3d velocity = Vec3d.ZERO.subtract(randOffset).normalize().scale(0.15f).add(new Vec3d(this.motionX, this.motionY, this.motionZ));
                Vec3d particlePos = this.getPositionVector().add(ModUtils.getAxisOffset(lookVec, new Vec3d(0.5, 0.5, 0))).add(randOffset);
                ParticleManager.spawnColoredSmoke(world, particlePos, ModColors.MAELSTROM, velocity);
            }
        }
    }


    @Override
    public Optional<Vec3d> getTarget() {
        return Optional.ofNullable(renderLazerPos);
    }

    @Override
    public void setRenderDirection(Vec3d dir) {
        if (this.renderLazerPos != null) {
            this.prevRenderLazerPos = this.renderLazerPos;
        } else {
            this.prevRenderLazerPos = dir;
        }
        this.renderLazerPos = dir;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSoundHandler.MINI_AVALON_SHOOT;
    }
}
