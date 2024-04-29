package com.example.structure.items.tools;

import com.example.structure.Main;
import com.example.structure.config.ModConfig;
import com.example.structure.entity.trader.action.player.ActionWaveFromPlayer;
import com.example.structure.init.ModCreativeTabs;
import com.example.structure.init.ModItems;
import com.example.structure.util.IHasModel;
import com.example.structure.util.ModUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemPureAxe extends ItemAxe implements IHasModel {

    private String info_loc;

    protected boolean setTooHasAir = false;

    protected ItemPureAxe(String name, ToolMaterial material) {
        super(material);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(ModCreativeTabs.ITEMS);
        ModItems.ITEMS.add(this);
    }

    public ItemPureAxe(String name, ToolMaterial material, float damage, float speed, String info_loc) {
        super(material, damage, speed);
        this.info_loc = info_loc;
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(ModCreativeTabs.ITEMS);
        ModItems.ITEMS.add(this);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        int SwordCoolDown = 13 * 20;
        if(!worldIn.isRemote && !player.getCooldownTracker().hasCooldown(this)) {
            Vec3d moveVec = player.getLookVec().scale(1.1);
            if(player.canBePushed()) {
                setTooHasAir = true;
                player.motionX = moveVec.x;
                player.motionY = (moveVec.y + 0.4) * 0.6;
                player.motionZ = moveVec.z;
                player.velocityChanged = true;
            }

            player.getCooldownTracker().setCooldown(this, SwordCoolDown);
            stack.damageItem(8, player);
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    int tackDelay = 5;
    int timeInAir = 0;

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);

        if(entityIn instanceof EntityPlayer && setTooHasAir && !worldIn.isRemote) {
            timeInAir++;
            if(tackDelay < 0) {
                boolean hasGround = worldIn.getBlockState(entityIn.getPosition().down()).isNormalCube();

                if (hasGround) {
                    new ActionWaveFromPlayer(timeInAir).performAction(entityIn, entityIn);
                    System.out.println("Entity has Ground");
                    setTooHasAir = false;
                    tackDelay = 5;
                    timeInAir = 0;
                }
            } else {
                tackDelay--;
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GOLD + ModUtils.translateDesc(info_loc));
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.RARE;
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
