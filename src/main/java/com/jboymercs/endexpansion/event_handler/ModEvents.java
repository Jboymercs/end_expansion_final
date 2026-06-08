package com.jboymercs.endexpansion.event_handler;

import com.jboymercs.endexpansion.config.ItemConfig;
import com.jboymercs.endexpansion.config.MobConfig;
import com.jboymercs.endexpansion.config.ModConfig;
import com.jboymercs.endexpansion.entity.ProjectilePurple;
import com.jboymercs.endexpansion.entity.barrend.EntityMadSpirit;
import com.jboymercs.endexpansion.entity.endking.ProjectileSpinSword;
import com.jboymercs.endexpansion.entity.magic.IMagicEntity;
import com.jboymercs.endexpansion.init.ModItems;
import com.jboymercs.endexpansion.init.ModPotions;
import com.jboymercs.endexpansion.items.ItemEndfallStaff;
import com.jboymercs.endexpansion.util.ModColors;
import com.jboymercs.endexpansion.util.ModDamageSource;
import com.jboymercs.endexpansion.util.ModRand;
import com.jboymercs.endexpansion.util.ModUtils;
import com.jboymercs.endexpansion.util.handlers.ModSoundHandler;
import com.jboymercs.endexpansion.util.handlers.ParticleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
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
        if (base.world.isRemote || !(base instanceof EntityPlayer)) {
            return;
        }

        Random rand = base.world.rand;
        ItemStack head = base.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        ItemStack chest = base.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        ItemStack mainhand = base.getHeldItemMainhand();
        ItemStack offhand = base.getHeldItemOffhand();

        if (head.getItem() == ModItems.DARK_HELMET && chest.getItem() == ModItems.DARK_CHESTPLATE) {
            base.removeActivePotionEffect(ModPotions.CORRUPTED);
        } else if (shouldCorruptHeldItem(mainhand, offhand, rand)) {
            if (timeTillRage < 0) {
                mainhand.damageItem(5, base);
                base.addPotionEffect(new PotionEffect(ModPotions.CORRUPTED, 400, 1));
                timeTillRage = 40;
            } else {
                timeTillRage--;
            }
        }

        //adds resistance if the player has madness, however effect doesn't work if player is wearing the Lidoped Helmet
        if ((mainhand.getItem() == ModItems.BARREND_TABLET || offhand.getItem() == ModItems.BARREND_TABLET) && base.isPotionActive(ModPotions.MADNESS)) {
            if (head.getItem() != ModItems.LIDOPED_HELMET) {
                if (base.ticksExisted % 40 == 0) {
                    base.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 60, 0));
                    if (rand.nextInt(20) == 0) {
                        if (mainhand.getItem() == ModItems.BARREND_TABLET) {
                            mainhand.damageItem(1, base);
                        } else if (offhand.getItem() == ModItems.BARREND_TABLET) {
                            offhand.damageItem(1, base);
                        }
                    }
                }
            }
        }

        if (head.getItem() == ModItems.LAMENTED_HELMET && chest.getItem() == ModItems.LAMENTED_CHESTPLATE) {
            if (base.hurtTime > 0) {
                double health = base.getHealth() / base.getMaxHealth();
                if (health <= 0.5 && rand.nextInt(10) == 0) {
                    base.addPotionEffect(new PotionEffect(MobEffects.SPEED, 100, 0, false, false));
                    base.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 80, 0, false, false));
                }

            }
        }
    }

    private boolean shouldCorruptHeldItem(ItemStack mainhand, ItemStack offhand, Random rand) {
        Item mainItem = mainhand.getItem();
        Item offhandItem = offhand.getItem();
        return (mainItem == ModItems.KNIGHT_SWORD && rand.nextInt(20) == 0)
                || (mainItem == ModItems.RED_CRYSTAL_ITEM && rand.nextInt(20) == 0)
                || (mainItem == ModItems.RED_CRYSTAL_CHUNK && rand.nextInt(20) == 0)
                || (mainItem == ModItems.UNHOLY_AXE && rand.nextInt(20) == 0)
                || (mainItem == ModItems.UNHOLY_ARROW && rand.nextInt(20) == 0)
                || (offhandItem == ModItems.UNHOLY_ARROW && rand.nextInt(20) == 0)
                || (mainItem == ModItems.REINFORCED_KNIGHT_SWORD && rand.nextInt(10) == 0)
                || (mainItem == ModItems.REINFORCED_UNHOLY_AXE && rand.nextInt(10) == 0);
    }

    public int ProjectileCooldown = ItemConfig.purp_cooldown * 20;


    @SubscribeEvent
    public void onPressStaffButton(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase base = event.getEntityLiving();
        if(!(base instanceof EntityPlayer)) {
            return;
        }

        ItemStack mainhand = base.getHeldItemMainhand();
        if(!(mainhand.getItem() instanceof ItemEndfallStaff)) {
            return;
        }

        if(base.isSwingInProgress && !base.world.isRemote && ProjectileCooldown < 0) {
            Vec3d playerLookVec = base.getLookVec();
            Vec3d playerPos = new Vec3d(base.posX + playerLookVec.x * 1.4D,base.posY + playerLookVec.y + base.getEyeHeight(), base. posZ + playerLookVec.z * 1.4D);
            ProjectilePurple projectile = new ProjectilePurple(base.world, base, ItemConfig.purp_projectile);
            base.world.playSound(null, base.posX, base.posY, base.posZ, ModSoundHandler.SEEKER_SHOOT, SoundCategory.PLAYERS, 0.7F, 1.0f / (base.world.rand.nextFloat() * 0.4F + 0.3f));
            ModUtils.setEntityPosition(projectile, playerPos);
            base.world.spawnEntity(projectile);
            projectile.setTravelRange(20f);
            projectile.shoot(playerLookVec.x, playerLookVec.y, playerLookVec.z, 1.5f, 1.0f);
            mainhand.getItem().setDamage(mainhand, 1);
            if(base.world.isRemote) {
                doParticleEffects(base.world, base);
            }
            ProjectileCooldown = ItemConfig.purp_cooldown * 20;
        } else {
            ProjectileCooldown--;
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
        if(base.world.isRemote || !(base instanceof EntityPlayer) || base.ticksExisted % 40 != 0) {
            return;
        }

        ItemStack head = base.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        ItemStack chest = base.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        ItemStack legs = base.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
        ItemStack feet = base.getItemStackFromSlot(EntityEquipmentSlot.FEET);

        if (head.getItem() == ModItems.ENDFALL_HELMET && chest.getItem() == ModItems.ENDFALL_CHESTPLATE &&
                legs.getItem() == ModItems.ENDFALL_LEGGINGS && feet.getItem() == ModItems.ENDFALL_BOOTS) {
            base.addPotionEffect(new PotionEffect(MobEffects.SPEED, 60, 0));
            base.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 60, 0));
        }

        if (head.getItem() == ModItems.AMBER_HELMET && chest.getItem() == ModItems.AMBER_CHESTPLATE &&
                legs.getItem() == ModItems.AMBER_LEGGINGS && feet.getItem() == ModItems.AMBER_BOOTS) {
            base.addPotionEffect(new PotionEffect(MobEffects.HASTE, 60, 0));
            List<EntityLivingBase> nearbyEntities = base.world.getEntitiesWithinAABB(EntityLivingBase.class, base.getEntityBoundingBox().grow(8D),
                    entityLivingBase -> !(entityLivingBase.getIsInvulnerable()) && !(entityLivingBase instanceof EntityPlayer));
            if (!nearbyEntities.isEmpty()) {
                for (EntityLivingBase base2 : nearbyEntities) {
                    base2.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 100, 0));
                }
            }
        }
    }

    protected int hitCooldown = 0;

    //The Rage potion effect, will throw the player at nearby entities to deal with
    @SubscribeEvent
    public void onRedRage(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        World world = entity.getEntityWorld();
        if(entity.isPotionActive(ModPotions.CORRUPTED)) {
            //Spawns the Red Particles
            if(world.isRemote) {
                if(world.rand.nextInt(3) == 0) {
                    Vec3d pos = entity.getPositionVector();
                    ParticleManager.spawnColoredSmoke(entity.world, pos.add(ModUtils.getRelativeOffset(entity, new Vec3d(0, 1.2, 0))), ModColors.RED, new Vec3d(ModRand.getFloat(1) * 0.1, ModRand.getFloat(1) * 0.1, ModRand.getFloat(1) * 0.1));
                }
            } else {
                List<EntityLivingBase> nearbyEntities = world.getEntitiesWithinAABB(EntityLivingBase.class, entity.getEntityBoundingBox().grow(8D),
                        entityLivingBase -> entityLivingBase != entity && !(entityLivingBase instanceof EntityPlayer));
                if(!nearbyEntities.isEmpty()) {
                    EntityLivingBase target = nearbyEntities.get(0);
                    if(target != entity) {
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

        if(entity.isPotionActive(ModPotions.MADNESS) && !hasSpawnedSpirit) {
            if(entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.LIDOPED_HELMET) {
                entity.removeActivePotionEffect(ModPotions.MADNESS);
            }
            //SPawns a mad spirit upon death of an entity inflicted with Madness
            if(!world.isRemote) {
                if(entity.deathTime == 1 && !(entity instanceof IMagicEntity)) {
                    Vec3d spawnPos = new Vec3d(entity.posX, entity.posY + 1.0, entity.posZ);
                    EntityMadSpirit spirit = new EntityMadSpirit(world);
                    spirit.setPosition(spawnPos.x, spawnPos.y, spawnPos.z);
                    entity.world.spawnEntity(spirit);
                    hasSpawnedSpirit = true;
                }
            }
        } else {
            hasSpawnedSpirit = false;
        }
    }

    private boolean hasSpawnedSpirit = false;





    @SubscribeEvent
    public void onArrowImpact(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase base = event.getEntityLiving();
        if(!(base instanceof EntityPlayer) || base.world.isRemote || base.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() != ModItems.ENDFALL_BOW) {
            return;
        }

        List<EntityLivingBase> nearbyEntities = base.world.getEntitiesWithinAABB(EntityLivingBase.class, base.getEntityBoundingBox().grow(9D),
                e -> !(e instanceof EntityPlayer));
        if(!nearbyEntities.isEmpty()) {
            for(EntityLivingBase entity : nearbyEntities) {
                if(base.world.rand.nextInt(35)==0) {
                    Vec3d baseOffset = entity.getPositionVector();
                    ProjectileSpinSword swords = new ProjectileSpinSword(base.world, base, MobConfig.projectile_sword_damage,base);
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
