package com.example.structure.event_handler;

import com.example.structure.init.ModPotions;
import com.example.structure.potion.PotionBase;
import com.example.structure.util.ModUtils;
import com.example.structure.util.NBTExtras;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.http.util.EntityUtils;

@Mod.EventBusSubscriber
public class PotionInArena extends PotionBase {




    public static final String ENTITY_TAG = "containmentPos";



    public PotionInArena(boolean isBadEffectIn, int liquidColorIn) {
        super(isBadEffectIn, liquidColorIn);
        setIconIndex(1,0);
    }

    @Override
    public boolean isReady(int duration, int amplifier){
        return true; // Execute the effect every tick
    }

    public static float getArenaDistance(int effectStrength){
        return 9 - effectStrength * 4;
    }

    @Override
    public void performEffect(EntityLivingBase target, int strength){
        float maxDistance = getArenaDistance(strength);

        // Initialise the containment position to the entity's position if it wasn't set already
        if(!target.getEntityData().hasKey(ENTITY_TAG)){
            NBTExtras.storeTagSafely(target.getEntityData(), ENTITY_TAG, NBTUtil.createPosTag(new BlockPos(target.getPositionVector().subtract(0.5, 0.5, 0.5))));
        }

        Vec3d origin = ModUtils.getCentre(NBTUtil.getPosFromTag(target.getEntityData().getCompoundTag(ENTITY_TAG)));

        double x = target.posX, y = target.posY, z = target.posZ;

        // Containment fields are cubes so we're dealing with each axis separately
        if(target.getEntityBoundingBox().maxX > origin.x + maxDistance) x = origin.x + maxDistance - target.width/2;
        if(target.getEntityBoundingBox().minX < origin.x - maxDistance) x = origin.x - maxDistance + target.width/2;

        if(target.getEntityBoundingBox().maxY > origin.y + maxDistance) y = origin.y + maxDistance - target.height;
        if(target.getEntityBoundingBox().minY < origin.y - maxDistance) y = origin.y - maxDistance;

        if(target.getEntityBoundingBox().maxZ > origin.z + maxDistance) z = origin.z + maxDistance - target.width/2;
        if(target.getEntityBoundingBox().minZ < origin.z - maxDistance) z = origin.z - maxDistance + target.width/2;


        if(x != target.posX || y != target.posY || z != target.posZ)
        {
            target.addVelocity(0.15 * Math.signum(x - target.posX), 0.15 * Math.signum(y - target.posY), 0.15 * Math.signum(z - target.posZ));
            ModUtils.undoGravity(target);
            if(target.world.isRemote){
            //play impact sounds
            }
        }

        // Need to do this here because it's the only way to hook into potion ending both client- and server-side
        if(target.getActivePotionEffect(this).getDuration() <= 1) target.getEntityData().removeTag(ENTITY_TAG);

    }

    @SubscribeEvent
    public static void onLivingUpdateEvent(LivingEvent.LivingUpdateEvent event){
        // This is LAST-RESORT CLEANUP. It does NOT need checking every tick! We always check for the actual potion anyway.
        if(event.getEntity().ticksExisted % 20 == 0 && event.getEntityLiving().getEntityData().hasKey(ENTITY_TAG)
                && !event.getEntityLiving().isPotionActive(ModPotions.IN_ARENA)){
            event.getEntityLiving().getEntityData().removeTag(ENTITY_TAG);
        }
    }
}
