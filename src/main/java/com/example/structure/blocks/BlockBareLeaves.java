package com.example.structure.blocks;
import com.example.structure.Main;
import com.example.structure.config.ModConfig;
import com.example.structure.util.ModColors;
import com.example.structure.util.ModRand;
import com.example.structure.util.handlers.ParticleManager;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockBareLeaves extends BlockLeavesBase{

    public BlockBareLeaves(String name, float hardness, float resistance, SoundType soundType) {
        super(name, hardness, resistance, soundType);
    }

    @Override
    public void beginLeavesDecay(IBlockState state, World world, BlockPos pos)
    {
        if (!(Boolean)state.getValue(CHECK_DECAY))
        {
            world.setBlockState(pos, state.withProperty(CHECK_DECAY, false), 4);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (!ModConfig.disable_end_ash_particles) {
            if (rand.nextInt(12) == 0) {

                Main.proxy.spawnParticle(1, worldIn, pos.getX() + rand.nextFloat(), pos.getY() + rand.nextFloat(), pos.getZ() + rand.nextFloat(), rand.nextFloat() - 0.5, -0.4, rand.nextFloat() - 0.5, ModRand.range(15, 30));
            }
        }
    }

}
