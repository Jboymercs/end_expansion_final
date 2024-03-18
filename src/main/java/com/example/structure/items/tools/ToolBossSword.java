package com.example.structure.items.tools;

import com.example.structure.config.ModConfig;
import com.example.structure.init.ModCreativeTabs;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ModSoundHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class ToolBossSword extends ToolSword implements ISweepAttackOverride, IAnimatable {

    private final String ANIM_DASH = "strike";
    private final String IDLE = "idle";
    private String info_loc;
    public AnimationFactory factory = new AnimationFactory(this);

    public ToolBossSword(String name, String info_loc, ToolMaterial material) {
        super(name, material);
        super.setCreativeTab(ModCreativeTabs.ITEMS);
        this.setMaxDamage(250);
        this.info_loc = info_loc;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.LIGHT_PURPLE + ModUtils.translateDesc(info_loc));
    }

    @Override
    public void doSweepAttack(EntityPlayer player, EntityLivingBase target) {
        ModUtils.doSweepAttack(player, target, (e) -> {
            e.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 10, 1));
        });

        if (target != null) {
            target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 10, 1));

        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        int SwordCoolDown = ModConfig.sword_cooldown * 20;
        if(!worldIn.isRemote && !player.getCooldownTracker().hasCooldown(this)) {
            worldIn.playSound(null, player.posX, player.posY, player.posZ, ModSoundHandler.BOSS_DASH, SoundCategory.PLAYERS, 1.0F, 1.0F);
            Vec3d moveVec = player.getLookVec().scale(ModConfig.sword_velocity);
            if(player.canBePushed()) {
                player.motionX = moveVec.x;
                player.motionY = moveVec.y * 0.5;
                player.getCooldownTracker().setCooldown(this, SwordCoolDown);
                player.motionZ = moveVec.z;
                player.velocityChanged = true;
            }
            stack.damageItem(2, player);
            this.registerDamage(player);
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    public void registerDamage(EntityPlayer player) {
        for(int i = 0; i < 50; i += 5) {
            for (EntityLivingBase entitylivingbase : player.getEntityWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(player.getPosition()).grow(1, 1, 1))) {
                if (entitylivingbase != player && (entitylivingbase.getPassengers().isEmpty() || !(entitylivingbase.getPassengers().get(0) == player))) {
                    Vec3d offset = player.getPositionVector().add(ModUtils.getRelativeOffset(player, new Vec3d(0,1.2,0)));
                    DamageSource source = ModDamageSource.builder().type(ModDamageSource.PLAYER).directEntity(player).build();
                    float damage = ModConfig.sword_dash_damage;
                    ModUtils.handleAreaImpact(2.0f, (e)-> damage, player, offset, source, 0.8f, 0, false);
                    i++;
                }
            }
        }
    }


    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
    }
    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(IDLE, true));
        return PlayState.CONTINUE;
    }



    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
