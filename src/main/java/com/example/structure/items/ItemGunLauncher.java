package com.example.structure.items;

import com.example.structure.Main;
import com.example.structure.config.ModConfig;
import com.example.structure.entity.ProjectilePurple;
import com.example.structure.init.ModCreativeTabs;
import com.example.structure.util.IHasModel;
import com.example.structure.util.ModReference;
import com.example.structure.util.ModUtils;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

import java.util.ArrayList;
import java.util.List;

public class ItemGunLauncher extends ItemAbstractMultiModel implements IHasModel {

    private String info_loc;
    public ItemGunLauncher(String name , String info_loc) {
        super(name);
        this.setMaxDamage(128);
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
        this.setCreativeTab(ModCreativeTabs.ITEMS);
        this.info_loc = info_loc;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        int SwordCoolDown = ModConfig.seeker_gun_cooldown * 20;
        if(!worldIn.isRemote && !player.getCooldownTracker().hasCooldown(this) && !hasFired) {
            //Summon Crystals
            Vec3d playerLookVec = player.getLookVec();
            Vec3d playerPos = new Vec3d(player.posX + playerLookVec.x * 1.4D,player.posY + playerLookVec.y + player.getEyeHeight(), player. posZ + playerLookVec.z * 1.4D);
            ProjectilePurple projectile = new ProjectilePurple(player.world, player, ModConfig.purp_projectile);
            ModUtils.setEntityPosition(projectile, playerPos);
            player.world.spawnEntity(projectile);
            projectile.setTravelRange(20f);
            projectile.shoot(playerLookVec.x, playerLookVec.y, playerLookVec.z, 1.5f, 1.0f);
            this.hasFired = true;
            player.getCooldownTracker().setCooldown(this, SwordCoolDown);
            stack.damageItem(1, player);
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    int firstTick = 0;

    public boolean hasFired = false;
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);

        if(this.hasFired) {
            System.out.println(firstTick);
            if(firstTick == 22) {
                if(!worldIn.isRemote && entityIn instanceof EntityPlayer) {
                    this.repeatShoot((EntityPlayer) entityIn, worldIn);
                }
            } if(firstTick == 42) {
                if(!worldIn.isRemote && entityIn instanceof EntityPlayer) {
                    this.repeatShoot((EntityPlayer) entityIn, worldIn);
                }
            }

                firstTick++;

            if(firstTick > 43) {
                this.hasFired = false;
                firstTick = 0;
            }
        }

    }

    public void repeatShoot(EntityPlayer player, World world) {
        if(!world.isRemote) {
                    Vec3d playerLookVec2 = player.getLookVec();
                    Vec3d playerPos2 = new Vec3d(player.posX + playerLookVec2.x * 1.4D, player.posY + playerLookVec2.y + player.getEyeHeight(), player.posZ + playerLookVec2.z * 1.4D);
                    ProjectilePurple projectile2 = new ProjectilePurple(player.world, player, ModConfig.purp_projectile);
                    ModUtils.setEntityPosition(projectile2, playerPos2);
                    player.world.spawnEntity(projectile2);
                    projectile2.setTravelRange(20f);
                    projectile2.shoot(playerLookVec2.x, playerLookVec2.y, playerLookVec2.z, 1.5f, 1.0f);
            }
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.LIGHT_PURPLE + ModUtils.translateDesc(info_loc));
    }

    @Override
    public void registerModels() {
        List<ModelResourceLocation> mrls = new ArrayList<>();
        ImmutableList<ModelResourceLocation> MList = ImmutableList.of(new ModelResourceLocation("ee:gun", "inventory"),
                new ModelResourceLocation("ee:gun_model", "inventory"));
        mrls.addAll(MList);

        ModelBakery.registerItemVariants(this, mrls.toArray(new ModelResourceLocation[0]));

        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(new ResourceLocation(ModReference.MOD_ID,"gun_dummy"), "inventory")); //d stands for dummy

    }
}
