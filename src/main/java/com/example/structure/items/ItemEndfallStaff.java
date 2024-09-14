package com.example.structure.items;

import com.example.structure.Main;
import com.example.structure.config.ItemConfig;
import com.example.structure.config.ModConfig;
import com.example.structure.init.ModPotions;
import com.example.structure.util.IHasModel;
import com.example.structure.util.ModColors;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ModSoundHandler;
import com.example.structure.util.handlers.ParticleManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
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

public class ItemEndfallStaff extends ItemBase implements IAnimatable, IHasModel {
    private String info_loc;

    private final String ANIM_IDLE = "spin";
    public AnimationFactory factory = new AnimationFactory(this);

    public ItemEndfallStaff(String name, CreativeTabs tab, String info_loc) {
        super(name, tab);
        this.setMaxStackSize(1);
        this.setMaxDamage(500);
        this.info_loc = info_loc;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
    }


    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.RED + ModUtils.translateDesc(info_loc));
    }



    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        int StaffCoolDown = ItemConfig.staff_cooldown * 20;
        ItemStack stack = playerIn.getHeldItem(handIn);
        if(!worldIn.isRemote && !playerIn.getCooldownTracker().hasCooldown(this)) {
            List<EntityLivingBase> nearbyEntities = worldIn.getEntitiesWithinAABB(EntityLivingBase.class, playerIn.getEntityBoundingBox().grow(4D), e -> !e.getIsInvulnerable());
            worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_ILLAGER_CAST_SPELL, SoundCategory.PLAYERS, 0.7F, 0.7F);
            if(!nearbyEntities.isEmpty()) {
                //Send Entities Away
                for(EntityLivingBase entity : nearbyEntities) {
                    if(entity != playerIn) {
                        Vec3d targetPos = entity.getPositionVector();
                        Vec3d currentPos = playerIn.getPositionVector();
                        double d0 = (targetPos.x - currentPos.x) * 1.6;
                        double d1 = (targetPos.y - currentPos.y) * 1.4;
                        double d2 = (targetPos.z - currentPos.z) * 1.6;
                        Vec3d vel = new Vec3d(d0, d1, d2);
                        ModUtils.addEntityVelocity(entity, vel);
                        entity.addPotionEffect(new PotionEffect(ModPotions.CORRUPTED, 200, 1));
                    }
                }
            }
            //bring Forth Particles
            doParticleEffects(worldIn, playerIn);
            stack.damageItem(3, playerIn);
            playerIn.getCooldownTracker().setCooldown(this, StaffCoolDown);
        }
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }


    protected void doParticleEffects(World world, EntityPlayer player) {
            ModUtils.circleCallback(1, 30, (pos) -> {
                pos = new Vec3d(pos.x, 0, pos.y);
                ParticleManager.spawnColoredSmoke(world, player.getPositionVector().add(ModUtils.getRelativeOffset(player, new Vec3d(0.5, 0.1, 0))), ModColors.RED, pos.normalize().scale(0.5).add(ModUtils.yVec(0)));
            });
    }
    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void registerModels() {
        {
            Main.proxy.registerItemRenderer(this, 0, "inventory");
        }}
}
