package com.example.structure.world.stronghold;

import com.example.structure.config.ModConfig;
import com.example.structure.config.WorldConfig;
import com.example.structure.util.ModRand;
import com.google.common.collect.Lists;
import net.minecraft.util.Rotation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.ArrayList;
import java.util.List;

public class BetterStronghold {

    /**
     * An Addition in replacement of the vanilla Stronghold
     */
    private List<StructureComponent> components;
    private World world;
    private TemplateManager manager;

    private static final int SIZE = WorldConfig.stronghold_size;
    private static final int SECOND_SIZE = WorldConfig.stronghold_size * 2;
    protected boolean hasGeneratedLibrary = false;
    protected boolean hasSpawnedSecondLevel = false;
    private static final List<Tuple<Rotation, BlockPos>> CROSS_POS = Lists.newArrayList(new Tuple(Rotation.NONE, new BlockPos(0, 0, 0)),
            new Tuple(Rotation.CLOCKWISE_90, new BlockPos(30, 0, 0)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, 0, 30)));

    public BetterStronghold(World worldIn, TemplateManager template, List<StructureComponent> components) {
        this.world = worldIn;
        this.manager = template;
        this.components = components;
    }


    public void startStronghold(BlockPos pos, Rotation rot) {
        BetterStrongholdTemplate template = new BetterStrongholdTemplate(manager, "portal_room", pos, rot, 0, true);
        components.add(template);
        generateCross(template, BlockPos.ORIGIN, rot);
    }

    public boolean generateCross(BetterStrongholdTemplate parent, BlockPos pos, Rotation rot) {
        String[] cross_types = {"cross_1", "cross_2", "cross_3", "cross_4"};
        BetterStrongholdTemplate template = addAdjustedPiece(parent, pos, ModRand.choice(cross_types), rot);


        if(template.isCollidingExcParent(manager, parent, components) || template.getDistance() > SIZE) {
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

    public boolean generateHall(BetterStrongholdTemplate parent, BlockPos pos, Rotation rot) {
        String[] hall_types = {"straight_1", "straight_2", "straight_3", "straight_4", "straight_5", "straight_6", "straight_7"};
        BetterStrongholdTemplate template = addAdjustedPiece(parent, pos, ModRand.choice(hall_types), rot);
        if(template.isCollidingExcParent(manager, parent, components) || template.getDistance() > SIZE) {
            return false;
        }

        //random chance to generate a library, it will usually always generate a library just in a different location
        if(!hasGeneratedLibrary && world.rand.nextInt(2) == 0) {
            template = addAdjustedPiece(parent, pos, "library", rot);
            components.add(template);
            hasGeneratedLibrary = true;
        } else {
            components.add(template);
        }

        int r = world.rand.nextInt(2);
        boolean genSuccess;


        if(r == 0) {
            genSuccess = generateHall(template, BlockPos.ORIGIN, rot);
        }
        else {
            int r2 = world.rand.nextInt(2);
            if(r2 != 0 && !hasSpawnedSecondLevel) {
                genSuccess = generateSecondLevelHall(template, BlockPos.ORIGIN, rot);
            } else {
                genSuccess = generateCross(template, BlockPos.ORIGIN, rot);
            }
        }

        if(!genSuccess) {
            components.remove(template);
           return this.generateEnd(parent, pos, rot);
        }
        return true;
    }


    //Starts the Second level
    public boolean generateSecondLevelHall(BetterStrongholdTemplate parent, BlockPos pos, Rotation rot) {
        BetterStrongholdTemplate template_base = addAdjustedPiece(parent, pos, "straight_level", rot);
        BetterStrongholdTemplate template_top = addAdjustedPiece(parent, pos.add(0, 31, 0), "cross_level", rot);

        if(template_base.isCollidingExcParent(manager, parent, components) || template_base.getDistance() > SECOND_SIZE) {
            return false;
        }
        hasSpawnedSecondLevel = true;
        components.add(template_base);
        components.add(template_top);
        List<StructureComponent> structures = new ArrayList<>(components);

        int failedSTarts = 0;
        for(Tuple<Rotation, BlockPos> tuple : CROSS_POS) {
            if(!generateCrossLevel(template_base, tuple.getSecond().add(0, 31, 0), rot.add(tuple.getFirst()))) {
                failedSTarts++;
            }

        }

        if(failedSTarts > 3) {
            components.clear();
            components.addAll(structures);
          generateEnd(parent, pos.add(0, 31, 0), rot);
        }

        return true;
    }

    public boolean generateCrossLevel(BetterStrongholdTemplate parent, BlockPos pos, Rotation rot) {
        String[] cross_types = {"cross_1", "cross_2", "cross_3", "cross_4"};
        BetterStrongholdTemplate template = addAdjustedPiece(parent, pos, ModRand.choice(cross_types), rot);

        if(template.isCollidingExcParent(manager, parent, components) || template.getDistance() > SECOND_SIZE) {
            return false;
        }

        List<StructureComponent> structures = new ArrayList<>(components);
        components.add(template);


        int failedHalls = 0;
        for(Tuple<Rotation, BlockPos> tuple : CROSS_POS) {
            if(!generateHallLevel(template, tuple.getSecond(), rot.add(tuple.getFirst()))) {
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

    public boolean generateHallLevel(BetterStrongholdTemplate parent, BlockPos pos, Rotation rot) {
        String[] hall_types = {"straight_1", "straight_2", "straight_3", "straight_4", "straight_5", "straight_6", "straight_7"};
        BetterStrongholdTemplate template = addAdjustedPiece(parent, pos, ModRand.choice(hall_types), rot);
        if(template.isCollidingExcParent(manager, parent, components) || template.getDistance() > SECOND_SIZE) {
            return false;
        }

        //random change to generate a library
        if(!hasGeneratedLibrary && world.rand.nextInt(2) == 0) {
            template = addAdjustedPiece(parent, pos, "library", rot);
            components.add(template);
            hasGeneratedLibrary = true;
        } else {
            components.add(template);
        }

        int r = world.rand.nextInt(2);
        boolean genSuccess;


        if(r == 0) {
            genSuccess = generateHallLevel(template, BlockPos.ORIGIN, rot);
        }
        else {
                genSuccess = generateCrossLevel(template, BlockPos.ORIGIN, rot);
        }

        if(!genSuccess) {
            components.remove(template);
            return this.generateEnd(parent, pos, rot);
        }
        return true;
    }

    //Generates End points for the Stronghold, they do not add too the count of the structure
    public boolean generateEnd(BetterStrongholdTemplate parent, BlockPos pos, Rotation rot) {
        String[] end_types = {"end_1","end_2","end_3", "end_4", "end_5", "end_6","end_7", "end_8", "end_9"};
        BetterStrongholdTemplate template = addAdjustedPieceWithoutAdding(parent, pos, ModRand.choice(end_types), rot);
        if(template.isCollidingExcParent(manager, parent, components)) {
            return false;
        }
        components.add(template);
        return true;
    }


    private BetterStrongholdTemplate addAdjustedPieceWithoutAdding(BetterStrongholdTemplate parent, BlockPos pos, String type, Rotation rot) {
        BetterStrongholdTemplate newTemplate = new BetterStrongholdTemplate(manager, type, parent.getTemplatePosition(), rot, parent.getDistance(), true);
        BlockPos blockpos = parent.getTemplate().calculateConnectedPos(parent.getPlacementSettings(), pos, newTemplate.getPlacementSettings(), BlockPos.ORIGIN);
        newTemplate.offset(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        adjustAndCenter(parent, newTemplate, rot);
        return newTemplate;
    }

    /**
     * Adds a new piece, with the previous template a reference for position and
     * rotation
     */
    private BetterStrongholdTemplate addAdjustedPiece(BetterStrongholdTemplate parent, BlockPos pos, String type, Rotation rot) {
        BetterStrongholdTemplate newTemplate = new BetterStrongholdTemplate(manager, type, parent.getTemplatePosition(), rot, parent.getDistance() + 1, true);
        BlockPos blockpos = parent.getTemplate().calculateConnectedPos(parent.getPlacementSettings(), pos, newTemplate.getPlacementSettings(), BlockPos.ORIGIN);
        newTemplate.offset(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        adjustAndCenter(parent, newTemplate, rot);
        return newTemplate;
    }



    /**
     * Centers a template to line up on the x, and in the center with z
     */
    private void adjustAndCenter(BetterStrongholdTemplate parent, BetterStrongholdTemplate child, Rotation rot) {
        BlockPos adjustedPos = new BlockPos(parent.getTemplate().getSize().getX(), 0, (parent.getTemplate().getSize().getZ() - child.getTemplate().getSize().getZ()) / 2f)
                .rotate(rot);
        child.offset(adjustedPos.getX(), adjustedPos.getY(), adjustedPos.getZ());
    }
}
