package com.example.structure.entity;

import com.example.structure.config.ItemConfig;
import com.example.structure.config.ModConfig;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class EntityGhostArm extends EntityModBase implements IAnimatable {

    private AnimationFactory factory = new AnimationFactory(this);

    private final String ANIM_IDLE = "attack";

    protected EntityPlayer playerToo;

    public EntityGhostArm(World worldIn, float x, float y, float z, EntityPlayer player) {
        super(worldIn, x, y, z);

        this.setSize(0.2f, 0.2f);
        this.setNoGravity(true);
        this.noClip = true;
        this.playerToo = player;
    }

    public EntityGhostArm(World worldIn) {
        super(worldIn);

        this.setSize(0.9f, 2.0f);
        this.setNoGravity(true);
        this.noClip = true;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller_spinToo", 0, this::predicateAttack));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if(playerToo != null) {
            Vec3d playerLook = playerToo.getLookVec();
            Vec3d playerPos = playerToo.getPositionVector();
            this.rotationPitch = playerToo.rotationPitch;
            this.rotationYawHead = playerToo.rotationYawHead;
            this.renderYawOffset = playerToo.renderYawOffset;
            this.rotationYaw = playerToo.rotationYaw;
            ModUtils.setEntityPosition(this, playerPos);
        }

        if(ticksExisted == 20) {
            //Arm at max swing, here it will deal damage at this cord
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(3, 1.3, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = (float) (ItemConfig.endfall_sword_damage * 1.5);
            ModUtils.handleAreaImpact(2.2f, (e) -> damage, this, offset, source, 0.9f, 0, false);
        }

        if(this.ticksExisted == 30) {
            this.setDead();
        }

    }

    @Override
    public final boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }


    private<E extends IAnimatable> PlayState predicateAttack(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, false));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
