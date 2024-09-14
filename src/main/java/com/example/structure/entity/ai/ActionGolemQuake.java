package com.example.structure.entity.ai;

import com.example.structure.config.MobConfig;
import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.ProjectileQuake;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;

public class ActionGolemQuake implements IAction{
    @Override
    public void performAction(EntityModBase actor, EntityLivingBase target) {
        DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(actor).build();
        Vec3d offset = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2, 1, 0)));
        ModUtils.handleAreaImpact(2, (e) -> actor.getAttack() * MobConfig.constructor_shockwave_damage, actor, offset, source, 0.5f, 0, true);

        float inaccuracy = 0.0f;
        float speed = 0.5f;
        float pitch = 0; // Projectiles aim straight ahead always

        // Shoots projectiles in a small arc
        for (int i = 0; i < 5; i++) {
            ProjectileQuake projectile = new ProjectileQuake(actor.world, actor, actor.getAttack() * MobConfig.constructor_shockwave_damage, (ItemStack) null);
            projectile.shoot(actor, pitch, actor.rotationYaw - 20 + (i * 10), 0.0F, speed, inaccuracy);
            projectile.setTravelRange(8f);
            actor.world.spawnEntity(projectile);
        }
    }
}
