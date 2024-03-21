package com.example.structure.blocks;

import com.example.structure.config.ModConfig;
import com.example.structure.util.ModColors;
import com.example.structure.util.handlers.ParticleManager;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockAsh extends BlockBase{
    public BlockAsh(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (!ModConfig.disable_end_ash_particles) {
            if (rand.nextInt(3) == 0) {
                ParticleManager.spawnColoredSmoke(worldIn, new Vec3d(pos.getX() + rand.nextDouble(), pos.getY() + 1.1f, pos.getZ() + rand.nextDouble()), ModColors.RANDOM_GREY, new Vec3d(0, 0.1, 0));
            }
        }
    }
}
