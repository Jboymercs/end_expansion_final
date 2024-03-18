package com.example.structure.blocks;

import com.example.structure.Main;
import com.example.structure.entity.EntityMiniNuke;
import com.example.structure.init.ModBlocks;
import com.example.structure.init.ModCreativeTabs;
import com.example.structure.init.ModItems;
import com.example.structure.util.IHasModel;
import com.example.structure.util.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.RenderTNTPrimed;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.List;

public class BlockMiniNuke extends BlockTNT implements IHasModel {
    /**
     * Mini-Nuke Class based from TNT with minor changes
     */
    public static final PropertyBool NUKE = PropertyBool.create("explode");
    private String info_loc;

    public BlockMiniNuke(String name, String info_loc) {
        super();
        setTranslationKey(name);
        setRegistryName(name);
        this.info_loc = info_loc;
        this.setCreativeTab(ModCreativeTabs.ITEMS);
        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
        this.setDefaultState(this.getDefaultState().withProperty(NUKE, Boolean.valueOf(false)));
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.RED + ModUtils.translateDesc(info_loc));
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        super.onBlockAdded(worldIn, pos, state);

        if (worldIn.isBlockPowered(pos))
        {
            this.onPlayerDestroy(worldIn, pos, state.withProperty(NUKE, Boolean.valueOf(true)));
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (worldIn.isBlockPowered(pos))
        {
            this.onPlayerDestroy(worldIn, pos, state.withProperty(NUKE, Boolean.valueOf(true)));
            worldIn.setBlockToAir(pos);
        }
    }


    @Override
    public void onPlayerDestroy(World worldIn, BlockPos pos, IBlockState state)
    {
        this.explode(worldIn, pos, state, (EntityLivingBase)null);
    }


    public void explode(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase igniter)
    {
        if (!worldIn.isRemote)
        {
            if (((Boolean)state.getValue(NUKE)).booleanValue())
            {
                EntityMiniNuke entityNuke = new EntityMiniNuke(worldIn, ((float)pos.getX() + 0.5F), pos.getY(), ((float)pos.getZ() + 0.5F), igniter);
                worldIn.spawnEntity(entityNuke);
                worldIn.playSound((EntityPlayer)null, entityNuke.posX, entityNuke.posY, entityNuke.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }
    }


    public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        if (!worldIn.isRemote && entityIn instanceof EntityArrow)
        {
            EntityArrow entityarrow = (EntityArrow)entityIn;

            if (entityarrow.isBurning())
            {
                this.explode(worldIn, pos, worldIn.getBlockState(pos).withProperty(NUKE, Boolean.valueOf(true)), entityarrow.shootingEntity instanceof EntityLivingBase ? (EntityLivingBase)entityarrow.shootingEntity : null);
                worldIn.setBlockToAir(pos);
            }
        }
    }

    @Override
    public boolean canDropFromExplosion(Explosion explosionIn)
    {
        return false;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(NUKE, Boolean.valueOf((meta & 1) > 0));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((Boolean)state.getValue(NUKE)).booleanValue() ? 1 : 0;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {NUKE});
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack itemstack = playerIn.getHeldItem(hand);

        if (!itemstack.isEmpty() && (itemstack.getItem() == Items.FLINT_AND_STEEL || itemstack.getItem() == Items.FIRE_CHARGE))
        {
            this.explode(worldIn, pos, state.withProperty(NUKE, Boolean.valueOf(true)), playerIn);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);

            if (itemstack.getItem() == Items.FLINT_AND_STEEL)
            {
                itemstack.damageItem(1, playerIn);
            }
            else if (!playerIn.capabilities.isCreativeMode)
            {
                itemstack.shrink(1);
            }

            return true;
        }
        else
        {
            return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
        }
    }

    @Override
    public void registerModels() {
            Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }
}
