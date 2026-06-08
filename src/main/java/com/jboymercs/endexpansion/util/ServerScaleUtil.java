package com.jboymercs.endexpansion.util;

import com.jboymercs.endexpansion.config.ModConfig;
import com.jboymercs.endexpansion.entity.EntityModBase;
import com.jboymercs.endexpansion.entity.shadowPlayer.EntityShadowPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

public class ServerScaleUtil {

    private static int countNearbyPlayers(EntityLivingBase actor, double range) {
        int playersNearby = 0;
        AxisAlignedBB searchBox = actor.getEntityBoundingBox().grow(range);
        for(EntityPlayer playerCap : actor.world.playerEntities) {
            if(!playerCap.getIsInvulnerable() && !playerCap.isCreative() && !playerCap.isSpectator()
                    && playerCap.getEntityBoundingBox().intersects(searchBox)) {
                playersNearby++;
            }
        }
        return playersNearby;
    }

    public static int countScalingPlayers(EntityLivingBase actor, World world) {
        if(world.isRemote || ModConfig.disable_scaling_mod) {
            return 0;
        }
        return countNearbyPlayers(actor, 60D);
    }

    private static float changeHealthAccordingToPlayers(EntityLivingBase actor, int playersNearby) {
        if(playersNearby > 1) {
            double additiveHealth = actor.getHealth() * ((playersNearby * ModConfig.scale_mod_bosses) - ModConfig.scale_mod_bosses);
            return (float) (actor.getHealth() + additiveHealth);
        }
        return actor.getHealth();
    }

    private static double setMaxHealthWithPlayers(EntityLivingBase actor, int playersNearby) {
        if(playersNearby > 1) {
            double additiveHealth = actor.getMaxHealth() * ((playersNearby * ModConfig.scale_mod_bosses) - ModConfig.scale_mod_bosses);
            return actor.getMaxHealth() + additiveHealth;
        }
        return actor.getMaxHealth();
    }

    private static double scaleAttackDamageInAccordanceWithPlayers(EntityLivingBase actor, int playersNearby) {
        double currentAttackDamage = actor.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        if(playersNearby > 1) {
            double additiveAttackDamage = currentAttackDamage * ((playersNearby * ModConfig.scale_attack_damge) - ModConfig.scale_attack_damge);
            return currentAttackDamage + additiveAttackDamage;
        }
        return currentAttackDamage;
    }

    //This sets the Current Health in accordance with how many players are current
    //Each Player after 1 adding 0.5 * currentHealth
    public static float changeHealthAccordingToPlayers(EntityLivingBase actor, World world) {
        if(world.isRemote || ModConfig.disable_scaling_mod) {
            return actor.getHealth();
        }
        return changeHealthAccordingToPlayers(actor, countNearbyPlayers(actor, 60D));
    }

    //This sets the Max Health of an Entity in accordance with how many players are nearby
    //Why we need to adjust the max health is to show little to no difference in health bar when a player joins the fight
    public static double setMaxHealthWithPlayers(EntityLivingBase actor, World world) {
        if(world.isRemote || ModConfig.disable_scaling_mod) {
            return actor.getMaxHealth();
        }
        return setMaxHealthWithPlayers(actor, countNearbyPlayers(actor, 60D));
    }

    //This scales the Attack Damage with how many players are near on startUp
    public static double scaleAttackDamageInAccordanceWithPlayers(EntityLivingBase actor, World world) {
        if(world.isRemote || ModConfig.disable_scaling_mod) {
            return actor.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        }
        return scaleAttackDamageInAccordanceWithPlayers(actor, countNearbyPlayers(actor, 60D));
    }

    public static ScaledStats getScaledStats(EntityLivingBase actor, World world) {
        int playersNearby = countScalingPlayers(actor, world);
        return new ScaledStats(
                scaleAttackDamageInAccordanceWithPlayers(actor, playersNearby),
                changeHealthAccordingToPlayers(actor, playersNearby),
                setMaxHealthWithPlayers(actor, playersNearby));
    }

    //This functions allows us to switch between targets and allow everyone to get a bit of the fun
    public static EntityLivingBase targetSwitcher(EntityModBase actor, World world) {
        EntityLivingBase currentTarget = actor.getAttackTarget();
        if(currentTarget instanceof EntityPlayer && !world.isRemote) {
            double range = actor.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue();
            AxisAlignedBB searchBox = actor.getEntityBoundingBox().grow(range);
            EntityPlayer firstPlayer = null;
            int currentPlayerCountCanSee = 0;

            for(EntityPlayer baseToo : actor.world.playerEntities) {
                if(!baseToo.getIsInvulnerable() && baseToo.getEntityBoundingBox().intersects(searchBox)) {
                    if(!baseToo.isSpectator() && !baseToo.isCreative()) {
                        currentPlayerCountCanSee++;
                        if(firstPlayer == null) {
                            firstPlayer = baseToo;
                        }
                    }
                }
            }

            List<EntityShadowPlayer> nearbyShadow= actor.world.getEntitiesWithinAABB(EntityShadowPlayer.class, searchBox, e-> !e.getIsInvulnerable());
            for(EntityShadowPlayer shadowPlayer : nearbyShadow) {
                if(shadowPlayer.getOwner() != null) {
                    currentPlayerCountCanSee++;
                }
            }

            if(firstPlayer != null) {
                if(currentPlayerCountCanSee > 1 && actor.getEntitySenses().canSee(firstPlayer)) {
                    return firstPlayer;
                }
                return currentTarget;
            }
        }

        return currentTarget;
    }


    public static EntityLivingBase targetSwitcherIncludingShadow(EntityModBase actor, World world) {
        EntityLivingBase currentTarget = actor.getAttackTarget();
        if(currentTarget != null && !world.isRemote && (currentTarget instanceof EntityPlayer || currentTarget instanceof EntityShadowPlayer)) {
            double range = actor.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue();
            AxisAlignedBB searchBox = actor.getEntityBoundingBox().grow(range);
            EntityLivingBase firstTarget = null;
            int currentPlayerCountCanSee = 0;

            for(EntityPlayer player : actor.world.playerEntities) {
                if(!player.getIsInvulnerable() && player.getEntityBoundingBox().intersects(searchBox)
                        && !player.isSpectator() && !player.isCreative()) {
                    currentPlayerCountCanSee++;
                    if(firstTarget == null) {
                        firstTarget = player;
                    }
                }
            }

            List<EntityShadowPlayer> nearbyShadow = actor.world.getEntitiesWithinAABB(EntityShadowPlayer.class, searchBox, e-> !e.getIsInvulnerable());
            for(EntityShadowPlayer shadowPlayer : nearbyShadow) {
                if(shadowPlayer.getOwner() != null) {
                    currentPlayerCountCanSee++;
                    if(firstTarget == null) {
                        firstTarget = shadowPlayer;
                    }
                }
            }

            if(firstTarget != null) {
                if(currentPlayerCountCanSee > 1 && actor.getEntitySenses().canSee(firstTarget)) {
                    return firstTarget;
                }
                return currentTarget;
            }
        }

        return currentTarget;
    }



    public static int getPlayers(EntityLivingBase actor, World world) {
        if(!world.isRemote) {
            int playersNearby = countNearbyPlayers(actor, 60D);
            if(playersNearby > 1) {
                return playersNearby - 1;
            }
        }
        return 0;
    }


    /**
     * Used for resetting bosses if they have killed a player and there is no current active targets
     * @param actor
     * @param world
     * @return
     */
    public static int getPlayersForReset(EntityLivingBase actor, World world) {
        if(!world.isRemote) {
            double range = actor.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue();
            return countNearbyPlayers(actor, range);
        }

        return 0;
    }

    public static class ScaledStats {
        public final double attackDamage;
        public final float health;
        public final double maxHealth;

        private ScaledStats(double attackDamage, float health, double maxHealth) {
            this.attackDamage = attackDamage;
            this.health = health;
            this.maxHealth = maxHealth;
        }
    }

}

