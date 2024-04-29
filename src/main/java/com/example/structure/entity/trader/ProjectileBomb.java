package com.example.structure.entity.trader;

import com.example.structure.entity.Projectile;
import com.example.structure.init.ModItems;
import com.example.structure.util.ModColors;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ParticleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ProjectileBomb extends Projectile {

    private static final int PARTICLE_AMOUNT = 1;
    private static final int EXPOSION_AREA_FACTOR = 2;

    protected Vec3d posImpact;

    public ProjectileBomb(World worldIn, EntityLivingBase throwerIn, float damage, Vec3d posImpact) {
        super(worldIn, throwerIn, damage);
        this.posImpact = posImpact;
    }

    public ProjectileBomb(World worldIn) {
        super(worldIn);
    }

    public ProjectileBomb(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }


    @Override
    public Item getItemToRender() {
        return ModItems.BOMB_PROJECTILE;
    }

    @Override
    protected void spawnParticles() {
        for (int i = 0; i < this.PARTICLE_AMOUNT; i++) {
            float size = 0.25f;
            ParticleManager.spawnColoredSmoke(world, getPositionVector(), ModColors.RANDOM_GREY, new Vec3d(0, 0.1, 0));
        }
        float groundHeight = ModUtils.findGroundBelow(world, getPosition()).getY() + 1.2f;
        Vec3d indicationPos = new Vec3d(this.posX, groundHeight, this.posZ);
        ModUtils.circleCallback(EXPOSION_AREA_FACTOR, 8, (pos) -> {
            Vec3d circleOffset = rotateCircleOverTime(pos);
            ParticleManager.spawnColoredSmoke(world, indicationPos.add(circleOffset), ModColors.PURPLE, new Vec3d(0, 0,0));
        });
    }


    private Vec3d rotateCircleOverTime(Vec3d pos) {
        Vec3d circleOffset = new Vec3d(pos.x, 0, pos.y);
        circleOffset = ModUtils.rotateVector2(circleOffset, ModUtils.Y_AXIS, ticksExisted * 2);
        return circleOffset;
    }


    @Override
    protected void onHit(RayTraceResult result) {
        DamageSource source = ModDamageSource.builder()
                .indirectEntity(shootingEntity)
                .directEntity(this)
                .type(ModDamageSource.EXPLOSION)
                .stoppedByArmorNotShields().build();

        ModUtils.handleAreaImpact(EXPOSION_AREA_FACTOR, (e) -> this.getDamage(), this.shootingEntity, this.getPositionVector(), source);
        this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.8F));
        super.onHit(result);
    }
}
