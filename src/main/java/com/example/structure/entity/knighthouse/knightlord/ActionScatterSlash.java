package com.example.structure.entity.knighthouse.knightlord;

import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.Projectile;
import com.example.structure.entity.ai.IAction;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ModSoundHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.Vec3d;

import java.util.Random;
import java.util.function.Supplier;

public class ActionScatterSlash implements IAction {

    Supplier<Projectile> projectileSupplier;
    float velocity;

    public ActionScatterSlash(Supplier<Projectile> projectileSupplier, float velocity) {
        this.projectileSupplier = projectileSupplier;
        this.velocity = velocity;
    }


    @Override
    public void performAction(EntityModBase actor, EntityLivingBase target) {
        float inaccuracy = 0.0f;
        float speed = velocity;
        float pitch = 0; // Projectiles aim straight ahead always
        actor.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 1.0f, 0.8f / (new Random().nextFloat() * 0.4f + 0.6f));
        Projectile projectile = projectileSupplier.get();
        Projectile projectile1 = projectileSupplier.get();
        Projectile projectile2 = projectileSupplier.get();
        Projectile projectile3 = projectileSupplier.get();
        Projectile projectile4 = projectileSupplier.get();
        Projectile projectile5 = projectileSupplier.get();
        Projectile projectile6 = projectileSupplier.get();
        Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1, 0, 0)));
        projectile.setPosition(relPos.x, relPos.y, relPos.z);
        projectile1.setPosition(relPos.x, relPos.y, relPos.z);
        projectile2.setPosition(relPos.x, relPos.y, relPos.z);
        projectile3.setPosition(relPos.x, relPos.y, relPos.z);
        projectile4.setPosition(relPos.x, relPos.y, relPos.z);
        projectile5.setPosition(relPos.x, relPos.y, relPos.z);
        projectile6.setPosition(relPos.x, relPos.y, relPos.z);

        projectile.shoot(actor, pitch, actor.rotationYaw - 105, 0.0F, speed, inaccuracy);
        projectile1.shoot(actor, pitch, actor.rotationYaw - 70, 0.0F, speed, inaccuracy);
        projectile2.shoot(actor, pitch, actor.rotationYaw - 35, 0.0F, speed, inaccuracy);
        //Center
        projectile3.shoot(actor, pitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
        //
        projectile4.shoot(actor, pitch, actor.rotationYaw + 35, 0.0F, speed, inaccuracy);
        projectile5.shoot(actor, pitch, actor.rotationYaw + 70, 0.0F, speed, inaccuracy);
        projectile6.shoot(actor, pitch, actor.rotationYaw + 105, 0.0F, speed, inaccuracy);

        projectile.setTravelRange(12f);
        projectile1.setTravelRange(12F);
        projectile2.setTravelRange(12F);
        projectile3.setTravelRange(12F);
        projectile4.setTravelRange(12F);
        projectile5.setTravelRange(12F);
        projectile6.setTravelRange(12F);

        actor.world.spawnEntity(projectile);
        actor.world.spawnEntity(projectile1);
        actor.world.spawnEntity(projectile2);
        actor.world.spawnEntity(projectile3);
        actor.world.spawnEntity(projectile4);
        actor.world.spawnEntity(projectile5);
        actor.world.spawnEntity(projectile6);
    }
}
