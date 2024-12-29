package com.example.structure.util;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.shadowPlayer.EntityShadowPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ServerScaleUtil {

    //This sets the Current Health in accordance with how many players are current
    //Each Player after 1 adding 0.5 * currentHealth
    public static float changeHealthAccordingToPlayers(EntityLivingBase actor, World world) {

        int playersNearby = 0;

        if(!world.isRemote && !ModConfig.disable_scaling_mod) {
            List<EntityPlayer> nearbyPlayers = actor.world.getEntitiesWithinAABB(EntityPlayer.class, actor.getEntityBoundingBox().grow(60D), e -> !e.getIsInvulnerable());

            if(!nearbyPlayers.isEmpty()) {
                for(EntityPlayer playerCap : nearbyPlayers) {
                    if(!playerCap.isCreative() && !playerCap.isSpectator()) {
                        playersNearby++;
                    }
                }
            }

            if(playersNearby > 1) {
                double additiveHealth = actor.getHealth() * ((playersNearby * ModConfig.scale_mod_bosses) - ModConfig.scale_mod_bosses); //We have to subtract atleats 0.5D due to the player count 1 not adding anything
                //Returns the additive amount and this is what sets the health
                return (float) (actor.getHealth() + additiveHealth);
            } else {
                return actor.getHealth();
            }
        }

            return actor.getHealth();
    }

    //This sets the Max Health of an Entity in accordance with how many players are nearby
    //Why we need to adjust the max health is to show little to no difference in health bar when a player joins the fight

    public static double setMaxHealthWithPlayers(EntityLivingBase actor, World world) {

        int playersNearby = 0;
        if(!world.isRemote && !ModConfig.disable_scaling_mod) {
            List<EntityPlayer> nearbyPlayers = actor.world.getEntitiesWithinAABB(EntityPlayer.class, actor.getEntityBoundingBox().grow(60D), e -> !e.getIsInvulnerable());

            if(!nearbyPlayers.isEmpty()) {
                for(EntityPlayer playerCap : nearbyPlayers) {
                    if(!playerCap.isCreative() && !playerCap.isSpectator()) {
                        playersNearby++;
                    }
                }
            }

            if(playersNearby > 1) {
                double additiveHealth = actor.getMaxHealth() * ((playersNearby * ModConfig.scale_mod_bosses) - ModConfig.scale_mod_bosses); //We have to subtract atleats 0.5D due to the player count 1 not adding anything
                //Returns the additive amount and this is what sets the health

                return (float) (actor.getMaxHealth() + additiveHealth);
            } else {
                return actor.getMaxHealth();
            }
        }
        return actor.getMaxHealth();
    }

    //This scales the Attack Damage with how many players are near on startUp
    public static double scaleAttackDamageInAccordanceWithPlayers(EntityLivingBase actor, World world) {
        double currentAttackDamage = actor.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        int playersNearby = 0;
        if(!world.isRemote && !ModConfig.disable_scaling_mod) {
            List<EntityPlayer> nearbyPlayers = actor.world.getEntitiesWithinAABB(EntityPlayer.class, actor.getEntityBoundingBox().grow(60D), e-> !e.getIsInvulnerable());
            if(!nearbyPlayers.isEmpty()) {
                for(EntityPlayer playerCap : nearbyPlayers) {
                    if(!playerCap.isCreative() && !playerCap.isSpectator()) {
                        playersNearby++;
                    }
                }
            }

            if(playersNearby > 1) {
                double additiveAttackDamage = currentAttackDamage * ((playersNearby * ModConfig.scale_attack_damge) - ModConfig.scale_attack_damge);

                return currentAttackDamage + additiveAttackDamage;
            } else {
                return currentAttackDamage;
            }
        }

        return currentAttackDamage;
    }

    //This functions allows us to switch between targets and allow everyone to get a bit of the fun

    public static EntityLivingBase targetSwitcher(EntityModBase actor, World world) {
        EntityLivingBase currentTarget = actor.getAttackTarget();
        if(currentTarget != null && !world.isRemote && currentTarget instanceof EntityPlayer) {
            double range = actor.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue();
            List<EntityPlayer> nearbySimilarTargets = actor.world.getEntitiesWithinAABB(EntityPlayer.class, actor.getEntityBoundingBox().grow(range), e-> !e.getIsInvulnerable());
            List<EntityShadowPlayer> nearbyShadow= actor.world.getEntitiesWithinAABB(EntityShadowPlayer.class, actor.getEntityBoundingBox().grow(range), e-> !e.getIsInvulnerable());
            int currentPlayerCountCanSee = 0;
            int shadowPlayerCurrent = 0;
            //Accounting for Shadow Players
            if(!nearbyShadow.isEmpty()) {
                for(EntityShadowPlayer shadowPlayer : nearbyShadow) {
                    if(shadowPlayer.getOwner() != null) {
                        currentPlayerCountCanSee++;
                        shadowPlayerCurrent++;
                    }
                }
            }
            if(!nearbySimilarTargets.isEmpty()) {
                //Firts gets a count
                for(EntityPlayer baseToo : nearbySimilarTargets) {
                        if (!baseToo.isSpectator() && !baseToo.isCreative()) {
                            currentPlayerCountCanSee++;
                        }
                }
                //After checking how many players it runs a test to see if there is more than one this entity can see and then selects them as it's new target
                for(EntityPlayer baseFrom : nearbySimilarTargets) {
                    if(currentPlayerCountCanSee > 1 && actor.getEntitySenses().canSee(baseFrom) && !baseFrom.isCreative() && !baseFrom.isSpectator()) {
                        return baseFrom;
                    } else {
                        //If there is only one, return current target
                        return currentTarget;
                    }

                }
            }
        }

        return currentTarget;
    }


    public static EntityLivingBase targetSwitcherIncludingShadow(EntityModBase actor, World world) {
        EntityLivingBase currentTarget = actor.getAttackTarget();
        if(currentTarget != null && !world.isRemote && currentTarget instanceof EntityPlayer || currentTarget != null && !world.isRemote && currentTarget instanceof EntityShadowPlayer) {
            double range = actor.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue();
            List<EntityLivingBase> nearbySimilarTargets = actor.world.getEntitiesWithinAABB(EntityLivingBase.class, actor.getEntityBoundingBox().grow(range), e-> !e.getIsInvulnerable());
            int currentPlayerCountCanSee = 0;
            List<EntityLivingBase> targetSelection = new ArrayList<>();

            if(!nearbySimilarTargets.isEmpty()) {
                //Firts gets a count
                for(EntityLivingBase baseToo : nearbySimilarTargets) {

                    if(baseToo instanceof EntityShadowPlayer) {
                        if(((EntityShadowPlayer)baseToo).getOwner() != null) {
                            currentPlayerCountCanSee++;
                            targetSelection.add(baseToo);
                        }
                    }
                    if(baseToo instanceof EntityPlayer) {
                        EntityPlayer player = ((EntityPlayer) baseToo);
                        if(!player.isSpectator() && !player.isCreative()) {
                            currentPlayerCountCanSee++;
                            targetSelection.add(baseToo);
                        }
                    }
                }
                //After checking how many players it runs a test to see if there is more than one this entity can see and then selects them as it's new target
                for(EntityLivingBase baseFrom : nearbySimilarTargets) {
                    if(!targetSelection.isEmpty() && currentPlayerCountCanSee > 1 && actor.getEntitySenses().canSee(baseFrom)) {
                        return targetSelection.iterator().next();
                    } else {
                        //If there is only one, return current target
                        return currentTarget;
                    }

                }
            }
        }

        return currentTarget;
    }



    public static int getPlayers(EntityLivingBase actor, World world) {

        int playersNearby = 0;

        if(!world.isRemote) {
            List<EntityPlayer> nearbyPlayers = actor.world.getEntitiesWithinAABB(EntityPlayer.class, actor.getEntityBoundingBox().grow(60D), e -> !e.getIsInvulnerable());
            if (!nearbyPlayers.isEmpty()) {
                for (EntityPlayer playerCap : nearbyPlayers) {
                    if (!playerCap.isCreative() && !playerCap.isSpectator()) {
                        playersNearby++;
                    }
                }
            }
        }

        if(playersNearby > 1) {
            //subtract one for the first player, the first player is always base stats, and it only adds after the first player is accounted for
            return playersNearby - 1;
        } else {
            return 0;
        }
    }


    /**
     * Used for resetting bosses if they have killed a player and there is no current active targets
     * @param actor
     * @param world
     * @return
     */
    public static int getPlayersForReset(EntityLivingBase actor, World world) {

        int playersNearby = 0;

        if(!world.isRemote) {
            double range = actor.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue();
            List<EntityPlayer> nearbyPlayers = actor.world.getEntitiesWithinAABB(EntityPlayer.class, actor.getEntityBoundingBox().grow(range), e -> !e.getIsInvulnerable());
            if (!nearbyPlayers.isEmpty()) {
                for (EntityPlayer playerCap : nearbyPlayers) {
                    if (!playerCap.isCreative() && !playerCap.isSpectator()) {
                        playersNearby++;
                    }
                }
            }
        }

        return playersNearby;
    }

}
