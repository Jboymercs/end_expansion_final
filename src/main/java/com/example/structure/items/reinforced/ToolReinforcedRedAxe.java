package com.example.structure.items.reinforced;

import com.example.structure.Main;
import com.example.structure.init.ModCreativeTabs;
import com.example.structure.init.ModItems;
import com.example.structure.util.IHasModel;
import com.example.structure.util.ModUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ToolReinforcedRedAxe extends ItemAxe implements IHasModel {
    private String info_loc;

    public ToolReinforcedRedAxe(String name, Item.ToolMaterial material) {
        super(material);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(ModCreativeTabs.ITEMS);
        ModItems.ITEMS.add(this);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        int SwordCoolDown = 7 * 20;
        if(!worldIn.isRemote && !player.getCooldownTracker().hasCooldown(this)) {
            player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 60, 1));
            player.getCooldownTracker().setCooldown(this, SwordCoolDown);
            stack.damageItem(2, player);

        }

        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.RED + ModUtils.translateDesc(info_loc));
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.RARE;
    }


    public ToolReinforcedRedAxe(String name, Item.ToolMaterial material, float damage, float speed, String info_loc) {
        super(material, damage, speed);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(ModCreativeTabs.ITEMS);
        ModItems.ITEMS.add(this);
        this.info_loc = info_loc;
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
