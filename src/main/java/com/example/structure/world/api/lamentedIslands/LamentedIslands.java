package com.example.structure.world.api.lamentedIslands;

import com.example.structure.config.ModConfig;
import com.example.structure.config.WorldConfig;
import com.example.structure.init.ModBlocks;
import com.example.structure.util.ModRand;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.List;

public class LamentedIslands {
    /**
     * The Lamented Islands, where the Lamentor resides
     */

    private List<StructureComponent> components;
    private World world;
    private TemplateManager manager;

    private static final int SIZE = WorldConfig.islands_size;


    private static final List<Tuple<Rotation, BlockPos>> CROSS_POS_MAIN = Lists.newArrayList(new Tuple(Rotation.NONE, new BlockPos(0, 30, 0)),
            new Tuple(Rotation.CLOCKWISE_90, new BlockPos(36, 30, 0)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, 30, 36)));

    private static final List<Tuple<Rotation, BlockPos>> CROSS_POS = Lists.newArrayList(new Tuple(Rotation.NONE, new BlockPos(0, 0, 0)),
            new Tuple(Rotation.CLOCKWISE_90, new BlockPos(30, 0, 0)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, 0, 30)));


    public LamentedIslands(World worldIn, TemplateManager template, List<StructureComponent> components) {
        this.world = worldIn;
        this.manager = template;
        this.components = components;
    }


    public void startIslands(BlockPos pos, Rotation rot) {
        LamentedIslandsTemplate template = new LamentedIslandsTemplate(manager, "start", pos, rot, 0, true);
        components.add(template);
        //Start to rotate the rest of the Tiles
        generateSpecialCross(template, BlockPos.ORIGIN, rot);
        int failedStarts = 0;
        for(Tuple<Rotation, BlockPos> tuple : CROSS_POS_MAIN) {

            if(!generateCross(template, tuple.getSecond(), rot.add(tuple.getFirst()))) {
                failedStarts++;
            }

        }

        List<StructureComponent> structures = new ArrayList<>(components);
        if(failedStarts > 3) {
            components.clear();
            components.addAll(structures);
        }
    }

    public boolean generateSpecialCross(LamentedIslandsTemplate parent, BlockPos pos, Rotation rot) {
        LamentedIslandsTemplate template = addAdjustedPiece(parent, pos.add(0, 30, 0), "cross_2", rot);
        LamentedIslandsTemplate template2 = addAdjustedPieceBuilding(parent, pos.add(0, 41, 0), "special_tile", rot);
        if(template.isCollidingExcParent(manager, parent, components)) {
            return false;
        }

        List<StructureComponent> structures = new ArrayList<>(components);
        components.add(template);
        components.add(template2);


        int failedHalls = 0;
        for(Tuple<Rotation, BlockPos> tuple : CROSS_POS) {
            if(!generateStraight(template, tuple.getSecond(), rot.add(tuple.getFirst()))) {
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

    public boolean generateCross(LamentedIslandsTemplate parent, BlockPos pos, Rotation rot) {
        String[] cross_types = {"cross_1", "cross_2"};
        LamentedIslandsTemplate template = addAdjustedPiece(parent, pos, ModRand.choice(cross_types), rot);
        String[] building_types = {"tile_1", "tile_2","tile_4", "tile_5", "tile_6", "tile_7", "tile_8", "tile_9"};
        LamentedIslandsTemplate template2 = addAdjustedPieceBuilding(parent, pos.add(0, 11, 0), ModRand.choice(building_types), rot);
        if(template.isCollidingExcParent(manager, parent, components) || template.getDistance() > SIZE) {
            return false;
        }

        List<StructureComponent> structures = new ArrayList<>(components);
        components.add(template);
        components.add(template2);


        int failedHalls = 0;
        for(Tuple<Rotation, BlockPos> tuple : CROSS_POS) {
            if(!generateStraight(template, tuple.getSecond(), rot.add(tuple.getFirst()))) {
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

    public boolean generateStraight(LamentedIslandsTemplate parent, BlockPos pos, Rotation rot) {
        String[] straight_types = {"straight_1", "straight_2"};
        LamentedIslandsTemplate template = addAdjustedPiece(parent, pos, ModRand.choice(straight_types), rot);
        String[] building_types = {"tile_1", "tile_2","tile_4", "tile_5", "tile_6", "tile_7", "tile_8", "tile_9"};
        LamentedIslandsTemplate template2 = addAdjustedPieceBuilding(parent, pos.add(0, 11, 0), ModRand.choice(building_types), rot);
        if(template.isCollidingExcParent(manager, parent, components) || template.getDistance() > SIZE) {
            return false;
        }
        components.add(template);
        components.add(template2);
        int r = world.rand.nextInt(2);
        boolean genSuccess;

        if(r == 0) {
            genSuccess = generateStraight(template, BlockPos.ORIGIN, rot);
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

    public boolean generateEnd(LamentedIslandsTemplate parent, BlockPos pos, Rotation rot) {
        String[] end_types = {"end_1", "end_2"};
        LamentedIslandsTemplate template = addAdjustedPiece(parent, pos, ModRand.choice(end_types), rot);
        String[] building_types = {"tile_1", "tile_2","tile_4", "tile_5", "tile_6", "tile_7", "tile_8", "tile_9"};
        LamentedIslandsTemplate template2 = addAdjustedPieceBuilding(parent, pos.add(0, 11, 0), ModRand.choice(building_types), rot);
        if(template.isCollidingExcParent(manager, parent, components) || template.getDistance() > SIZE) {
            return false;
        }
        components.add(template);
        components.add(template2);

        return true;
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


    private LamentedIslandsTemplate addAdjustedPieceBuilding(LamentedIslandsTemplate parent, BlockPos pos, String type, Rotation rot) {
        LamentedIslandsTemplate newTemplate = new LamentedIslandsTemplate(manager, type, parent.getTemplatePosition(), rot, parent.getDistance(), true);
        BlockPos blockpos = parent.getTemplate().calculateConnectedPos(parent.getPlacementSettings(), pos, newTemplate.getPlacementSettings(), BlockPos.ORIGIN);
        newTemplate.offset(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        adjustAndCenter(parent, newTemplate, rot);
        return newTemplate;
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
