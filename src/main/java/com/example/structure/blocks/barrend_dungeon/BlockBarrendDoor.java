package com.example.structure.blocks.barrend_dungeon;

import com.example.structure.blocks.BlockBase;
import com.example.structure.blocks.IBlockUpdater;
import com.example.structure.config.ProgressionConfig;
import com.example.structure.entity.tileentity.TileEntityUpdater;
import com.example.structure.init.ModBlocks;
import com.example.structure.util.ModColors;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ParticleManager;
import com.google.common.base.Predicate;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockBarrendDoor extends BlockBase implements IBlockUpdater, ITileEntityProvider {

    private Item activationItem;
    int counter = 0;

    public BlockBarrendDoor(String name, Material material) {
        super(name, material);
    }

    public BlockBarrendDoor(String name, Material material, float hardness, float resistance, SoundType soundType, Item activationItem) {
        super(name, material, hardness, resistance, soundType);
        this.activationItem = activationItem;
    }

    @Override
    public void update(World world, BlockPos pos) {
        counter++;
        if (counter % 5 == 0) {
            List<EntityPlayerSP> list = world.<EntityPlayerSP>getPlayers(EntityPlayerSP.class, new Predicate<EntityPlayerSP>() {
                @Override
                public boolean apply(@Nullable EntityPlayerSP player) {
                    return player.getHeldItem(EnumHand.MAIN_HAND).getItem() == activationItem;
                }
            });

            if (list.size() > 0) {
                ModUtils.performNTimes(10, (i) -> {
                     ParticleManager.spawnFirework(world, new Vec3d(pos).add(new Vec3d(0.5, 25 + i, 0.5)), ModColors.GREEN, ModUtils.yVec(-0.1f));
                });
            }
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        if(playerIn.getHeldItemMainhand().getItem() == this.activationItem && ModUtils.getAdvancementCompletionAsList(playerIn, ProgressionConfig.barrend_crypt_progress_stage)) {
            if(!worldIn.isRemote) {
                playerIn.getHeldItemMainhand().damageItem(1, playerIn);
                for(int i = 0; i <= 9; i++) {
                    //center
                    AxisAlignedBB box = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                    //find block func
                    BlockPos posToo = ModUtils.searchForBlocks(box.grow(4.0, 4.0, 4.0), playerIn.world, ModBlocks.BARE_DOOR_CONNECTOR.getDefaultState());
                    //repeat and destroy other blocks
                    if(posToo != null) {
                        worldIn.setBlockToAir(posToo);
                    }
                }
                worldIn.setBlockToAir(pos);
            }
        } else {
            playerIn.sendStatusMessage(new TextComponentTranslation(ProgressionConfig.barrend_crypt_locked_message, new Object[0]), true);
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityUpdater();
    }
}
