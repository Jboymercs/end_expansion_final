package com.example.structure.world.api.mines;

import com.example.structure.config.ModConfig;
import com.example.structure.config.WorldConfig;
import com.example.structure.init.ModBlocks;
import com.example.structure.util.ModRand;
import com.example.structure.world.api.vaults.VaultTemplate;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.ArrayList;
import java.util.List;

public class AshedMines {

    /**
     * An Addition to the Ashed Wasteland's underground, a similarity to Minecraft's mineshafts
     */
    private List<StructureComponent> components;
    private World world;
    private TemplateManager manager;


    private int yAxel = 0;


    private int yAxel2Level = 0;

    private static final int SIZE = WorldConfig.ashed_mines_size;

    private static final int SECOND_SIZE = SIZE + 4;

    private boolean hasSpawnedSecondlevel = false;

    private static final List<Tuple<Rotation, BlockPos>> CROSS_POS = Lists.newArrayList(new Tuple(Rotation.NONE, new BlockPos(0, 0, 0)),
            new Tuple(Rotation.CLOCKWISE_90, new BlockPos(14, 0, 0)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, 0, 14)));


    public AshedMines(World worldIn, TemplateManager template, List<StructureComponent> components) {
        this.world = worldIn;
        this.manager = template;
        this.components = components;
    }


    public void startMines(BlockPos pos, Rotation rot) {
        yAxel = pos.getY() - 1;
        yAxel = pos.getY() - 10;
        MinesTemplate template = new MinesTemplate(manager, "start", pos, rot, 0, true);
        components.add(template);
        generateCross(template, BlockPos.ORIGIN, rot);
        int failedSTarts = 0;
        for(Tuple<Rotation, BlockPos> tuple : CROSS_POS) {
            if(!generateCross(template, tuple.getSecond(), rot.add(tuple.getFirst()))) {
                failedSTarts++;
            }

        }
        List<StructureComponent> structures = new ArrayList<>(components);
        if(failedSTarts > 3) {
            components.clear();
            components.addAll(structures);
            generateEnd(template, pos, rot);
        }
    }

    public boolean generateCross(MinesTemplate parent, BlockPos pos, Rotation rot) {
        String[] cross_types = {"cross_1", "cross_2", "cross_3"};
        MinesTemplate template = addAdjustedPiece(parent, pos, ModRand.choice(cross_types), rot);

        if(template.isCollidingExcParent(manager, parent, components) || template.getDistance() > SIZE || getGroundFromAbove(world, pos.getX(), pos.getZ()) == 0) {
            return false;
        }

        List<StructureComponent> structures = new ArrayList<>(components);
        components.add(template);

        int failedHalls = 0;
        for(Tuple<Rotation, BlockPos> tuple : CROSS_POS) {
            if(!generateHall(template, tuple.getSecond(), rot.add(tuple.getFirst()))) {
                failedHalls++;
            }
        }

        if(failedHalls > 3) {
            components.clear();
            components.addAll(structures);
            return this.generateEnd(parent, pos, rot);
        }
        return true;
    }

    private boolean generateHall(MinesTemplate parent, BlockPos pos, Rotation rot) {
        String[] hall_types = {"straight_1", "straight_2", "straight_3", "straight_4", "straight_5", "straight_6", "straight_7", "straight_8", "straight_9"};
        MinesTemplate template = addAdjustedPiece(parent, pos, ModRand.choice(hall_types), rot);

        if(template.isCollidingExcParent(manager, parent, components) || template.getDistance() > SIZE || getGroundFromAbove(world, pos.getX(), pos.getZ()) == 0) {
            return false;
        }

        components.add(template);

        int r = world.rand.nextInt(2);
        boolean genSuccess;

        if(r == 0) {
            genSuccess = generateHall(template, BlockPos.ORIGIN, rot);
        }
        else {
            genSuccess = generateCross(template, BlockPos.ORIGIN, rot);
        }

        if(!genSuccess) {
            components.remove(template);
            return this.generateEnd(parent, pos, rot);
        }

        return true;
    }

    private boolean generateEnd(MinesTemplate parent, BlockPos pos, Rotation rot) {
        String[] end_types = {"end_1", "end_2", "end_3", "end_4", "end_5"};
        MinesTemplate template = addAdjustedPiece(parent, pos, ModRand.choice(end_types), rot);
        if(template.isCollidingExcParent(manager, parent, components) || template.getDistance() > SIZE || getGroundFromAbove(world, pos.getX(), pos.getZ()) == 0) {
            return false;
        }
        components.add(template);

        return true;
    }


    public static int getGroundFromAbove(World world, int x, int z)
    {
        int y = 38;
        boolean foundGround = false;
        while(!foundGround && y-- >= 0)
        {
            Block blockAt = world.getBlockState(new BlockPos(x,y,z)).getBlock();
            foundGround =  blockAt == ModBlocks.END_ASH || blockAt == ModBlocks.BROWN_END_STONE;
        }

        return y;
    }


    public static int getGroundFromAboveSecondLevel(World world, int x, int z)
    {
        int y = 25;
        boolean foundGround = false;
        while(!foundGround && y-- >= 10)
        {
            Block blockAt = world.getBlockState(new BlockPos(x,y,z)).getBlock();
            foundGround =  blockAt == ModBlocks.END_ASH || blockAt == ModBlocks.BROWN_END_STONE;
        }

        return y;
    }

    /**
     * Adds a new piece, with the previous template a reference for position and
     * rotation
     */
    private MinesTemplate addAdjustedPiece(MinesTemplate parent, BlockPos pos, String type, Rotation rot) {
        MinesTemplate newTemplate = new MinesTemplate(manager, type, parent.getTemplatePosition(), rot, parent.getDistance() + 1, true);
        BlockPos blockpos = parent.getTemplate().calculateConnectedPos(parent.getPlacementSettings(), pos, newTemplate.getPlacementSettings(), BlockPos.ORIGIN);
        newTemplate.offset(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        adjustAndCenter(parent, newTemplate, rot);
        return newTemplate;
    }


    /**
     * Centers a template to line up on the x, and in the center with z
     */
    private void adjustAndCenter(MinesTemplate parent, MinesTemplate child, Rotation rot) {
        BlockPos adjustedPos = new BlockPos(parent.getTemplate().getSize().getX(), 0, (parent.getTemplate().getSize().getZ() - child.getTemplate().getSize().getZ()) / 2f)
                .rotate(rot);
        child.offset(adjustedPos.getX(), adjustedPos.getY(), adjustedPos.getZ());
    }
}
