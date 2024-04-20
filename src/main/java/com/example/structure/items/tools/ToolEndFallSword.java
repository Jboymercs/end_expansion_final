package com.example.structure.items.tools;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityGhostArm;
import com.example.structure.init.ModCreativeTabs;
import com.example.structure.util.ModUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;

public class ToolEndFallSword extends ToolSword implements IAnimatable {

    private final String ANIM_ABILITY = "ability";
    private final String IDLE = "idle";

    private String controllerName = "attack_controller";
    private String info_loc;
    public AnimationFactory factory = new AnimationFactory(this);

    public ToolEndFallSword(String name, String info_loc, ToolMaterial material) {
        super(name, material);
        super.setCreativeTab(ModCreativeTabs.ITEMS);
        this.info_loc = info_loc;
        this.setMaxDamage(800);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.RED + ModUtils.translateDesc(info_loc));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        int SwordCoolDown = ModConfig.endfall_sword_cooldown * 20;
        if(!worldIn.isRemote && !player.getCooldownTracker().hasCooldown(this)) {
            AnimationController<?> controller = GeckoLibUtil.getControllerForStack(this.factory, stack, controllerName);
            EntityGhostArm eyeTooSummon = new EntityGhostArm(worldIn, (float) player.posX,(float) player.posY, (float) player.posZ, player);
            eyeTooSummon.setPosition(player.posX, player.posY, player.posZ);
            worldIn.spawnEntity(eyeTooSummon);

            if(controller.getAnimationState() == AnimationState.Stopped) {
                controller.markNeedsReload();
                controller.setAnimation(new AnimationBuilder().addAnimation(ANIM_ABILITY, false));
            }

            player.getCooldownTracker().setCooldown(this, SwordCoolDown);
            stack.damageItem(2, player);

        }

        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(IDLE, true));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        //This second one is for the quick scale up when using the sword
        AnimationController<ToolEndFallSword> controller = new AnimationController<ToolEndFallSword>(this,
                controllerName, 5, this::predicateAttack);

        animationData.addAnimationController(controller);
    }

    private <E extends IAnimatable>PlayState predicateAttack(AnimationEvent<E> event) {
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
