package com.example.structure.world.api.lamentedIslands;

import com.example.structure.config.ModConfig;
import com.example.structure.init.ModBlocks;
import com.example.structure.world.api.mines.MinesTemplate;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.List;

public class LamentedIslands {
    /**
     * The Lamented Islands, where the Lamentor resides
     */

    private List<StructureComponent> components;
    private World world;
    private TemplateManager manager;

    private static final int SIZE = 3;


    private static final List<Tuple<Rotation, BlockPos>> CROSS_POS_MAIN = Lists.newArrayList(new Tuple(Rotation.NONE, new BlockPos(0, 0, 0)),
            new Tuple(Rotation.CLOCKWISE_90, new BlockPos(14, 0, 0)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, 0, 14)));

    private static final List<Tuple<Rotation, BlockPos>> CROSS_POS = Lists.newArrayList(new Tuple(Rotation.NONE, new BlockPos(0, 0, 0)),
            new Tuple(Rotation.CLOCKWISE_90, new BlockPos(14, 0, 0)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, 0, 14)));


    public LamentedIslands(World worldIn, TemplateManager template, List<StructureComponent> components) {
        this.world = worldIn;
        this.manager = template;
        this.components = components;
    }


    public void startIslands(BlockPos pos, Rotation rot) {

    }


    //This will adjust the height of the Lamented Islands and also ensure that the it's not spawning
    public static int getGroundFromAbove(World world, int x, int z)
    {
        int y = 38;
        boolean foundGround = false;
        while(!foundGround && y-- >= 0)
        {
            Block blockAt = world.getBlockState(new BlockPos(x,y,z)).getBlock();
            foundGround =  blockAt == Blocks.END_STONE || blockAt == ModBlocks.END_ASH || blockAt == ModBlocks.BROWN_END_STONE;
        }

        return y;
    }

    /**
     * Adds a new piece, with the previous template a reference for position and
     * rotation
     */
    private LamentedIslandsTemplate addAdjustedPiece(LamentedIslandsTemplate parent, BlockPos pos, String type, Rotation rot) {
        LamentedIslandsTemplate newTemplate = new LamentedIslandsTemplate(manager, type, parent.getTemplatePosition(), rot, parent.getDistance() + 1, true);
        BlockPos blockpos = parent.getTemplate().calculateConnectedPos(parent.getPlacementSettings(), pos, newTemplate.getPlacementSettings(), BlockPos.ORIGIN);
        newTemplate.offset(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        adjustAndCenter(parent, newTemplate, rot);
        return newTemplate;
    }


    /**
     * Centers a template to line up on the x, and in the center with z
     */
    private void adjustAndCenter(LamentedIslandsTemplate parent, LamentedIslandsTemplate child, Rotation rot) {
        BlockPos adjustedPos = new BlockPos(parent.getTemplate().getSize().getX(), 0, (parent.getTemplate().getSize().getZ() - child.getTemplate().getSize().getZ()) / 2f)
                .rotate(rot);
        child.offset(adjustedPos.getX(), adjustedPos.getY(), adjustedPos.getZ());
    }

}
