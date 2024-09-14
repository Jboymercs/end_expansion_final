package com.example.structure.items;

import com.example.structure.config.ItemConfig;
import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityGroundCrystal;
import com.example.structure.init.ModItems;
import com.example.structure.util.ModUtils;
import net.minecraft.block.BlockDispenser;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class DurableShield extends ItemShield implements IAnimatable {

    public AnimationFactory factory = new AnimationFactory(this);
    private String info_loc;

    private String ANIM_STRIKE = "shield_strike";

    private String controllerName = "attack_controller";
    public DurableShield(String name, CreativeTabs tabs, String info_loc) {
        super();
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(tabs);
        this.info_loc = info_loc;
        this.maxStackSize = 1;
        ModItems.ITEMS.add(this);
        this.addPropertyOverride(new ResourceLocation("blocking"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, ItemArmor.DISPENSER_BEHAVIOR);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        if (stack.getSubCompound("BlockEntityTag") != null)
        {
            EnumDyeColor enumdyecolor = TileEntityBanner.getColor(stack);
            return I18n.translateToLocal("ee:item.durable_shield." + enumdyecolor.getTranslationKey() + ".name");
        }
        else
        {
            return I18n.translateToLocal("ee.desc.shield_name_desc");
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.LIGHT_PURPLE + ModUtils.translateDesc(info_loc));
        ItemBanner.appendHoverTextFromTileEntityTag(stack, tooltip);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack stack = playerIn.getHeldItem(handIn);
        Vec3d playerPos = playerIn.getPositionVector();
        int SwordCoolDown = ItemConfig.shield_cooldown * 20;
        if( !worldIn.isRemote && !playerIn.getCooldownTracker().hasCooldown(this)) {
            AnimationController<?> controller = GeckoLibUtil.getControllerForStack(this.factory, stack, controllerName);
            ModUtils.circleCallback(2, 8, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(playerPos);
                EntityGroundCrystal spike = new EntityGroundCrystal(worldIn, playerIn);
                spike.setPosition(pos.x, pos.y, pos.z);
                worldIn.spawnEntity(spike);
            });
            playerIn.getCooldownTracker().setCooldown(this, SwordCoolDown);

            if(controller.getAnimationState() == AnimationState.Stopped) {
                controller.markNeedsReload();
                controller.setAnimation(new AnimationBuilder().addAnimation(ANIM_STRIKE, false));
            }
        }


        ItemStack itemstack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }

    @Override
    public boolean isShield(ItemStack stack, @Nullable EntityLivingBase entity)
    {
        return true;
    }


    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        return false;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BLOCK;
    }


    @Override
    public void registerControllers(AnimationData animationData) {
        AnimationController<DurableShield> controller = new AnimationController<DurableShield>(this,
                controllerName, 5, this::predicateAttack);

        animationData.addAnimationController(controller);
    }

    private <E extends IAnimatable> PlayState predicateAttack(AnimationEvent<E> event) {
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }


}
