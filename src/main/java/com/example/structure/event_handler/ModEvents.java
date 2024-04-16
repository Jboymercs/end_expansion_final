package com.example.structure.event_handler;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.ProjectilePurple;
import com.example.structure.entity.endking.ProjectileSpinSword;
import com.example.structure.init.ModItems;
import com.example.structure.init.ModPotions;
import com.example.structure.items.ItemEndfallStaff;
import com.example.structure.util.ModColors;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ParticleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.Random;

public class ModEvents {

    protected int timeTillRage = 40;
    @SubscribeEvent
    public void onPlayerHoldItem(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase base = event.getEntityLiving();
        Random rand = new Random();

        if(!base.world.isRemote) {
            if (base.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.DARK_HELMET&& base.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == ModItems.DARK_CHESTPLATE) {
                base.removeActivePotionEffect(ModPotions.CORRUPTED);
            } else {
                if(base.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() == ModItems.KNIGHT_SWORD || base.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() == ModItems.RED_CRYSTAL_ITEM ||
                        base.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() == ModItems.RED_CRYSTAL_CHUNK && rand.nextInt(20) == 0) {

                    if (timeTillRage < 0) {
                        base.getHeldItemMainhand().damageItem(5, base);
                        base.addPotionEffect(new PotionEffect(ModPotions.CORRUPTED, 400, 1));
                        timeTillRage = 40;
                    } else {
                        timeTillRage--;
                    }

                }
            }
        }
    }

    public int ProjectileCooldown = ModConfig.purp_cooldown * 20;


    @SubscribeEvent
    public void onPressStaffButton(LivingEvent.LivingUpdateEvent event) {

        EntityLivingBase base = event.getEntityLiving();
        ItemStack mainhand = base.getHeldItemMainhand();

        if(mainhand.getItem() instanceof ItemEndfallStaff) {
            if(base.isSwingInProgress && !base.world.isRemote && ProjectileCooldown < 0) {
                Vec3d playerLookVec = base.getLookVec();
                Vec3d playerPos = new Vec3d(base.posX + playerLookVec.x * 1.4D,base.posY + playerLookVec.y + base.getEyeHeight(), base. posZ + playerLookVec.z * 1.4D);
                ProjectilePurple projectile = new ProjectilePurple(base.world, base, ModConfig.purp_projectile);
                ModUtils.setEntityPosition(projectile, playerPos);
                base.world.spawnEntity(projectile);
                projectile.setTravelRange(20f);
                projectile.shoot(playerLookVec.x, playerLookVec.y, playerLookVec.z, 1.5f, 1.0f);
                mainhand.getItem().setDamage(mainhand, 1);
                doParticleEffects(base.world, base);
                ProjectileCooldown = ModConfig.purp_cooldown * 20;
            } else {
                ProjectileCooldown--;
            }
        }
    }


    protected void doParticleEffects(World world, EntityLivingBase player) {
        ModUtils.circleCallback(1, 30, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y);
            ParticleManager.spawnColoredSmoke(world, player.getPositionVector().add(ModUtils.getRelativeOffset(player, new Vec3d(0.5, 0.1, 0))), ModColors.MAELSTROM, pos.normalize().scale(0.5).add(ModUtils.yVec(0)));
        });
    }

    @SubscribeEvent
    public void onArmorEquip(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase base = event.getEntityLiving();

        if (base.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.ENDFALL_HELMET && base.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == ModItems.ENDFALL_CHESTPLATE &&
                base.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() == ModItems.ENDFALL_LEGGINGS && base.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() == ModItems.ENDFALL_BOOTS) {
            if (!base.world.isRemote && base.ticksExisted % 40 == 0) {
                base.addPotionEffect(new PotionEffect(MobEffects.SPEED, 60, 0));
                base.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 60, 0));
            }
        }


    }

    protected int hitCooldown = 0;

    //The Rage potion effect, will throw the player at nearby entities to deal with
    @SubscribeEvent
    public void onRedRage(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        Vec3d pos = entity.getPositionVector();
        Random rand = new Random();
        if(entity.isPotionActive(ModPotions.CORRUPTED)) {
            World world = event.getEntityLiving().getEntityWorld();
            //Spawns the Red Particles
            if(rand.nextInt(3) == 0) {
                ParticleManager.spawnColoredSmoke(entity.world, pos.add(ModUtils.getRelativeOffset(entity, new Vec3d(0, 1.2, 0))), ModColors.RED, new Vec3d(ModRand.getFloat(1) * 0.1, ModRand.getFloat(1) * 0.1, ModRand.getFloat(1) * 0.1));
            }

            List<EntityLivingBase> nearbyEntities = world.getEntitiesWithinAABB(EntityLivingBase.class, entity.getEntityBoundingBox().grow(8D), entityLivingBase -> !(entityLivingBase instanceof EntityPlayer));
            if(nearbyEntities.iterator().hasNext()) {
                EntityLivingBase target = (EntityLivingBase)nearbyEntities.get(0);
                if(target != null && target != entity) {
                    double distSq = entity.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
                    double distance = Math.sqrt(distSq);
                    if(entity.canBePushed()) {
                        Vec3d moveVec = target.getPositionVector().subtract(entity.getPositionVector()).scale(0.13F);
                        entity.motionX = moveVec.x;
                        entity.motionY = moveVec.y;
                        entity.motionZ = moveVec.z;
                        entity.velocityChanged = true;
                    }
                  if(distance < 3 && hitCooldown > 40) {
                      entity.getHeldItemMainhand().damageItem(1, entity);
                    Vec3d posDamage = target.getPositionVector().add(ModUtils.yVec(0.3D));
                      DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(entity).build();
                      float damage = ModConfig.potion_damage;
                      ModUtils.handleAreaImpact(0.1f, (e)-> damage, entity, posDamage, source, 0.3f, 0, false);
                      hitCooldown = 0;
                  } else {
                      hitCooldown++;
                  }

                }
            }

        }
    }





    @SubscribeEvent
    public void onArrowImpact(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase base = event.getEntityLiving();
        if(base instanceof EntityPlayer && base.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() == ModItems.ENDFALL_BOW) {
            List<EntityLivingBase> nearbyEntities = base.world.getEntitiesWithinAABB(EntityLivingBase.class, base.getEntityBoundingBox().grow(9D), e -> !e.getIsInvulnerable() || !(e instanceof EntityPlayer));

            if(!nearbyEntities.isEmpty()) {
                for(EntityLivingBase entity : nearbyEntities) {
                    if(!(entity instanceof EntityPlayer)) {
                        if(base.world.rand.nextInt(35)==0 && !base.world.isRemote) {
                            Vec3d baseOffset = entity.getPositionVector();
                            ProjectileSpinSword swords = new ProjectileSpinSword(base.world, base, ModConfig.projectile_sword_damage,base);
                            swords.setPosition(baseOffset.x + ModRand.range(-4, 4), baseOffset.y + ModRand.range(1, 4), baseOffset.z + ModRand.range(-4, 4));
                            swords.setTravelRange(20);
                            Vec3d vel = baseOffset.add(ModUtils.yVec(1)).subtract(swords.getPositionVector());
                            swords.shoot(vel.x, vel.y, vel.z, 1.5f, 0f);
                            base.world.spawnEntity(swords);
                        }
                    }
                }
            }

        }
    }
}
