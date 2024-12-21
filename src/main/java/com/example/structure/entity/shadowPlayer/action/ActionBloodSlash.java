package com.example.structure.entity.shadowPlayer.action;

import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.Projectile;
import com.example.structure.entity.shadowPlayer.EntityShadowPlayer;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.Vec3d;

import java.util.Random;
import java.util.function.Supplier;

public class ActionBloodSlash implements IActionShadow{
    Supplier<Projectile> projectileSupplier;
    float velocity;

    public ActionBloodSlash(Supplier<Projectile> projectileSupplier, float velocity) {
        this.projectileSupplier = projectileSupplier;
        this.velocity = velocity;
    }

    @Override
    public void performAction(EntityShadowPlayer actor, EntityLivingBase target) {
        float inaccuracy = 0.0f;
        float speed = velocity;
        float pitch = 0; // Projectiles aim straight ahead always
        actor.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 1.0f, 0.8f / (new Random().nextFloat() * 0.4f + 0.6f));
        Projectile projectile = projectileSupplier.get();
        Projectile projectile1 = projectileSupplier.get();
        Projectile projectile2 = projectileSupplier.get();
        Projectile projectile3 = projectileSupplier.get();
        Projectile projectile4 = projectileSupplier.get();
        Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, 0)));
        Vec3d relPos2 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, -1)));
        Vec3d relPos3 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, 1)));
        Vec3d relPos4 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, -2)));
        Vec3d relPos5 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, 2)));
        projectile.setPosition(relPos.x, relPos.y, relPos.z);
        projectile1.setPosition(relPos2.x, relPos2.y, relPos2.z);
        projectile2.setPosition(relPos3.x, relPos3.y, relPos3.z);
        projectile3.setPosition(relPos4.x, relPos4.y, relPos4.z);
        projectile4.setPosition(relPos5.x, relPos5.y, relPos5.z);
        projectile.shoot(actor, pitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
        projectile1.shoot(actor, pitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
        projectile2.shoot(actor, pitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
        projectile3.shoot(actor, pitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
        projectile4.shoot(actor, pitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
        projectile.setTravelRange(30f);
        projectile1.setTravelRange(30F);
        projectile2.setTravelRange(30F);
        projectile3.setTravelRange(30F);
        projectile4.setTravelRange(30F);
        actor.world.spawnEntity(projectile);
        actor.world.spawnEntity(projectile1);
        actor.world.spawnEntity(projectile2);
        actor.world.spawnEntity(projectile3);
        actor.world.spawnEntity(projectile4);
    }
}
