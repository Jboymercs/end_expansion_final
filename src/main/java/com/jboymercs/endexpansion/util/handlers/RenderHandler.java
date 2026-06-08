package com.jboymercs.endexpansion.util.handlers;

import com.jboymercs.endexpansion.entity.*;
import com.jboymercs.endexpansion.entity.arrow.EntityChomperArrow;
import com.jboymercs.endexpansion.entity.arrow.EntityGreenArrow;
import com.jboymercs.endexpansion.entity.arrow.EntityUnholyArrow;
import com.jboymercs.endexpansion.entity.barrend.*;
import com.jboymercs.endexpansion.entity.barrend.guard.EntityBarrendGuard;
import com.jboymercs.endexpansion.entity.barrend.ultraparasite.EntityMoveTile;
import com.jboymercs.endexpansion.entity.barrend.ultraparasite.EntityParasiteBombAOE;
import com.jboymercs.endexpansion.entity.endking.*;
import com.jboymercs.endexpansion.entity.endking.friendly.EntityFriendKing;
import com.jboymercs.endexpansion.entity.endking.ghosts.EntityGhostPhase;
import com.jboymercs.endexpansion.entity.endking.ghosts.EntityPermanantGhost;
import com.jboymercs.endexpansion.entity.knighthouse.EntityEnderMage;
import com.jboymercs.endexpansion.entity.knighthouse.EntityEnderShield;
import com.jboymercs.endexpansion.entity.knighthouse.EntityHealAura;
import com.jboymercs.endexpansion.entity.knighthouse.EntityKnightLord;
import com.jboymercs.endexpansion.entity.knighthouse.knightlord.EntityBloodSlash;
import com.jboymercs.endexpansion.entity.lamentorUtil.EntityLamentorWave;
import com.jboymercs.endexpansion.entity.painting.EntityEEPainting;
import com.jboymercs.endexpansion.entity.painting.RenderEEPainting;
import com.jboymercs.endexpansion.entity.render.*;
import com.jboymercs.endexpansion.entity.render.arrow.RenderArrowBase;
import com.jboymercs.endexpansion.entity.render.moveTile.RenderMoveTile;
import com.jboymercs.endexpansion.entity.render.shadowPlayer.RenderMadnessCube;
import com.jboymercs.endexpansion.entity.render.shadowPlayer.RenderShadowPlayer;
import com.jboymercs.endexpansion.entity.seekers.EndSeeker;
import com.jboymercs.endexpansion.entity.seekers.EndSeekerPrime;
import com.jboymercs.endexpansion.entity.shadowPlayer.EntityMadnessCube;
import com.jboymercs.endexpansion.entity.shadowPlayer.EntityShadowPlayer;
import com.jboymercs.endexpansion.entity.trader.*;
import com.jboymercs.endexpansion.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import java.util.function.Function;

public class RenderHandler {


    private static <T extends Entity, U extends ModelBase, V extends RenderModEntity> void registerModEntityRenderer(Class<T> entityClass, U model, String... textures) {
        registerModEntityRenderer(entityClass, (manager) -> new RenderModEntity(manager, model, textures));
    }

    private static <T extends Entity, U extends ModelBase, V extends RenderModEntity> void registerModEntityRenderer(Class<T> entityClass, Function<RenderManager, Render<? super T>> renderClass) {
        RenderingRegistry.registerEntityRenderingHandler(entityClass, new IRenderFactory<T>() {
            @Override
            public Render<? super T> createRenderFor(RenderManager manager) {
                return renderClass.apply(manager);
            }
        });
    }

    private static <T extends Entity> void registerProjectileRenderer(Class<T> projectileClass) {
        registerProjectileRenderer(projectileClass, null);
    }

    /**
     * Makes a projectile render with the given item
     *
     * @param projectileClass
     */
    private static <T extends Entity> void registerProjectileRenderer(Class<T> projectileClass, Item item) {
        RenderingRegistry.registerEntityRenderingHandler(projectileClass, new IRenderFactory<T>() {
            @Override
            public Render<? super T> createRenderFor(RenderManager manager) {
                return new RenderProjectile<T>(manager, Minecraft.getMinecraft().getRenderItem(), item);
            }
        });
    }


    public static void registerGeoEntityRenderers() {
        //Crystal Knight Boss
        RenderingRegistry.registerEntityRenderingHandler(EntityCrystalKnight.class, RenderCrystalBoss::new);
        //Shulker COnstructor
        RenderingRegistry.registerEntityRenderingHandler(EntityBuffker.class, RenderBuffker::new);
        //Ground Crystal - Utility
        RenderingRegistry.registerEntityRenderingHandler(EntityGroundCrystal.class, RenderGroundCrystal::new);
        //Crystal Ball - Utility
        registerProjectileRenderer(EntityCrystalSpikeSmall.class);
        //Idle Entity - Utility
        registerProjectileRenderer(EntityExplosion.class);
        //Quake
        registerProjectileRenderer(ProjectileQuake.class);
        //Ender Knight
       // registerModEntityRenderer(EntityEnderKnight.class, RenderEnderKnight::new);
        //End King
        RenderingRegistry.registerEntityRenderingHandler(EntityEndKing.class, RenderEntityKing::new);
        //Red Crystal
        RenderingRegistry.registerEntityRenderingHandler(EntityRedCrystal.class, RenderRedCrystal::new);
        //Red Sword
        registerProjectileRenderer(ProjectileSpinSword.class);
        //Fire ball
        RenderingRegistry.registerEntityRenderingHandler(EntityFireBall.class, RenderFireball::new);
        //Nuclear Explosion
        RenderingRegistry.registerEntityRenderingHandler(EntityNuclearExplosion.class, RenderNuclearExplosion::new);
        //End Bug
        RenderingRegistry.registerEntityRenderingHandler(EntityEndBug.class, RenderEndBug::new);
        //Ender Mage
        RenderingRegistry.registerEntityRenderingHandler(EntityEnderMage.class, RenderEnderMage::new);
        //Heal Aura
        RenderingRegistry.registerEntityRenderingHandler(EntityHealAura.class, RenderHealingAura::new);
        //Ender Shield
        RenderingRegistry.registerEntityRenderingHandler(EntityEnderShield.class, RenderEnderShield::new);
        //Ender Lord
        RenderingRegistry.registerEntityRenderingHandler(EntityKnightLord.class, RenderKnightLord::new);
        //Snatcher
        RenderingRegistry.registerEntityRenderingHandler(EntitySnatcher.class, RenderSnatcher::new);
        //Ghost Phase 2
        RenderingRegistry.registerEntityRenderingHandler(EntityGhostPhase.class, RenderGhostPhase::new);
        // Ring Eye Phase 1
        RenderingRegistry.registerEntityRenderingHandler(EntityEye.class, RenderEye::new);
        //FireBall esc
        registerProjectileRenderer(ProjectileFireBalls.class, Items.FIRE_CHARGE);
        //Wall
        RenderingRegistry.registerEntityRenderingHandler(EntityWall.class, RenderWall::new);
        //Alien Controller
        RenderingRegistry.registerEntityRenderingHandler(EntityController.class, RenderController::new);
        //Lamented Eye - Utitlity
        RenderingRegistry.registerEntityRenderingHandler(EntityLamentedEye.class, RenderLamentedEye::new);
        //Ghost Arm - Utility
        RenderingRegistry.registerEntityRenderingHandler(EntityGhostArm.class, RenderGhostArm::new);
        //Mini-Buke - Utility
        RenderingRegistry.registerEntityRenderingHandler(EntityMiniNuke.class, RenderMiniNuke::new);
        //Purple Projectile - Utility
        registerProjectileRenderer(ProjectilePurple.class);
        //End King Ghost - Permananant - Utility
        RenderingRegistry.registerEntityRenderingHandler(EntityPermanantGhost.class, RenderPermanantGhost::new);
        //Ground Sword - Utility
        RenderingRegistry.registerEntityRenderingHandler(EntityGroundSword.class, RenderGroundSword::new);
        //End Seeker - Mob
        RenderingRegistry.registerEntityRenderingHandler(EndSeeker.class, RenderEnderSeeker::new);
        //Ender Eye
        RenderingRegistry.registerEntityRenderingHandler(EntityEnderEyeFly.class, RenderEnderEyeFly::new);
        //End Seeker Prime
        RenderingRegistry.registerEntityRenderingHandler(EndSeekerPrime.class, RenderSeekerPrime::new);
        //Friendly End King - Item Summon
        RenderingRegistry.registerEntityRenderingHandler(EntityFriendKing.class, RenderFriendKing::new);
        //Barrend Golem - Mini-boss
        RenderingRegistry.registerEntityRenderingHandler(EntityBarrendGolem.class, RenderBarrendGolem::new);
        //Redone End Knight
        RenderingRegistry.registerEntityRenderingHandler(EntityEnderKnight.class, RenderEndKnightRedone::new);
        //Chomper - Ashed Biome
        RenderingRegistry.registerEntityRenderingHandler(EntityChomper.class, RenderChomper::new);
        //Large AOE - Utility
        RenderingRegistry.registerEntityRenderingHandler(EntityLargeAOEEffect.class, RenderLargeAOEEffect::new);
        //Unholy Arrow
        RenderingRegistry.registerEntityRenderingHandler(EntityUnholyArrow.class, RenderArrowBase::new);
        //Chomper Arrow
        RenderingRegistry.registerEntityRenderingHandler(EntityChomperArrow.class, RenderArrowBase::new);
        //Green Arrow
        RenderingRegistry.registerEntityRenderingHandler(EntityGreenArrow.class, RenderArrowBase::new);
        //Sword Spike
        RenderingRegistry.registerEntityRenderingHandler(EntitySwordSpike.class, RenderSwordSpike::new);
        //Avalon Trader
        RenderingRegistry.registerEntityRenderingHandler(EntityAvalon.class, RenderAvalon::new);
        //Mini-Avalon
        RenderingRegistry.registerEntityRenderingHandler(EntityMiniValon.class, RenderValon::new);
        //AOE Avalon
        RenderingRegistry.registerEntityRenderingHandler(EntityAOEArena.class, RenderAvalonAOE::new);
        //Projectile Bomb
        registerProjectileRenderer(ProjectileBomb.class);
        //Controller Lift
        RenderingRegistry.registerEntityRenderingHandler(EntityControllerLift.class, RenderControllerLift::new);
        //Projectile Acid
        registerProjectileRenderer(ProjectileAcid.class);
        //Barrend Parasite
        RenderingRegistry.registerEntityRenderingHandler(EntityBarrendParasite.class, RenderBarrendParasite::new);
        //Lamentor Wave
        RenderingRegistry.registerEntityRenderingHandler(EntityLamentorWave.class, RenderLamentorWave::new);
        //End Expansion Painting
        RenderingRegistry.registerEntityRenderingHandler(EntityEEPainting.class, RenderEEPainting::new);
        //Lidoped
        RenderingRegistry.registerEntityRenderingHandler(EntityLidoped.class, RenderLidoped::new);
        //Mad Spirit
        RenderingRegistry.registerEntityRenderingHandler(EntityMadSpirit.class, RenderMadSpirit::new);
        //Blood Slash
        registerProjectileRenderer(EntityBloodSlash.class, ModItems.INVISIBLE);

            //Void Tripod
            RenderingRegistry.registerEntityRenderingHandler(EntityVoidTripod.class, RenderTripod::new);
            //Ultra Parasite
            RenderingRegistry.registerEntityRenderingHandler(EntityUltraParasite.class, RenderUltraParasite::new);
            //Move Tile
            RenderingRegistry.registerEntityRenderingHandler(EntityMoveTile.class, RenderMoveTile::new);
            //Parasite Bomb
            registerProjectileRenderer(ProjectileParasiteBomb.class, ModItems.PARASITE_PROJECTILE);
            //Parasite Bomb AOE
            RenderingRegistry.registerEntityRenderingHandler(EntityParasiteBombAOE.class, RenderParasiteBombAOE::new);
            //Barrend Guard
            RenderingRegistry.registerEntityRenderingHandler(EntityBarrendGuard.class, RenderBarrendGuard::new);
            //Shadow Player Boss
            RenderingRegistry.registerEntityRenderingHandler(EntityShadowPlayer.class, RenderShadowPlayer::new);
            //Madness Cube
            RenderingRegistry.registerEntityRenderingHandler(EntityMadnessCube.class, RenderMadnessCube::new);
    }
}
