package com.example.structure.entity.ai;

import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.Projectile;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.Vec3d;

import java.util.function.Function;
import java.util.function.Supplier;

public class ActionVollet implements IAction{
    Supplier<Projectile> projectileSupplier;
    float velocity;

    public ActionVollet(Supplier<Projectile> projectileSupplier, float velocity) {
        this.projectileSupplier = projectileSupplier;
        this.velocity = velocity;
    }

    @Override
    public void performAction(EntityModBase actor, EntityLivingBase target) {
        Function<Vec3d, Runnable> missile = (offset) -> () -> {
            Projectile projectile = projectileSupplier.get();
            projectile.setTravelRange(40);

            ModUtils.throwProjectile(actor, target.getPositionEyes(1),
                    projectile,
                    6.0f,
                    velocity,
                    offset);

            actor.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 0.4F, ModRand.getFloat(0.2f) + 1.3f);
        };

        actor.addEvent(missile.apply(ModUtils.getRelativeOffset(actor, new Vec3d(0, 1.5, 1))), 16);
        actor.addEvent(missile.apply(ModUtils.getRelativeOffset(actor, new Vec3d(0, 1.5, -1))), 16);
        actor.addEvent(missile.apply(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0.5, 1))), 21);
        actor.addEvent(missile.apply(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0.5, -1))), 21);
        actor.addEvent(missile.apply(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, 1))), 26);
        actor.addEvent(missile.apply(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, -1))), 26);
        actor.addEvent(missile.apply(ModUtils.getRelativeOffset(actor, new Vec3d(0, -0.5, 1))), 31);
        actor.addEvent(missile.apply(ModUtils.getRelativeOffset(actor, new Vec3d(0, -0.5, -1))), 31);
    }
}
