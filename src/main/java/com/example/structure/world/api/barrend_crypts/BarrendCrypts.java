package com.example.structure.world.api.barrend_crypts;

import com.example.structure.config.WorldConfig;
import com.example.structure.util.ModRand;
import com.example.structure.world.api.vaults.VaultTemplate;
import com.google.common.collect.Lists;
import net.minecraft.util.Rotation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.ArrayList;
import java.util.List;

public class BarrendCrypts {

    private List<StructureComponent> components;
    private World world;
    private TemplateManager manager;

    private static final int SIZE = 4;

    private static final List<Tuple<Rotation, BlockPos>> HOLD_CROSS_POS = Lists.newArrayList(new Tuple(Rotation.NONE, new BlockPos(-1, -1, 0)),
            new Tuple(Rotation.CLOCKWISE_90, new BlockPos(30, -1, -2)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, -1, 31)));

    private static final List<Tuple<Rotation, BlockPos>> LARGE_CROSS_POS = Lists.newArrayList(new Tuple(Rotation.NONE, new BlockPos(0, 0, 0)),
            new Tuple(Rotation.CLOCKWISE_90, new BlockPos(20, 0, 0)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, 0, 20)));

    public BarrendCrypts(World worldIn, TemplateManager template, List<StructureComponent> components) {
        this.world = worldIn;
        this.manager = template;
        this.components = components;
    }

    public void startCrypt(BlockPos pos, Rotation rot) {
        BarrendCryptTemplate startTemplate = new BarrendCryptTemplate(manager, "start", pos, rot, 0, true);
        components.add(startTemplate);
        BarrendCryptTemplate template = addAdjustedPieceWithoutDistance(startTemplate, BlockPos.ORIGIN.add(-21,-1,0), "begin_drop", rot);
        components.add(template);
        System.out.println("Generated Crypt at" + pos);
        generateFirstHoldLayer(template, pos, rot);

    }

    private boolean generateFirstHoldLayer(BarrendCryptTemplate parent, BlockPos pos, Rotation rot) {
        String[] layer_1 = {"hold_1", "hold_2"};
        BarrendCryptTemplate template = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(-31, -9, 0), ModRand.choice(layer_1), rot);
        components.add(template);
        generateSecondHoldLayer(template, pos, rot);

        boolean hasGeneratedOpenDoor = false;
        int failedHalls = 0;
        List<StructureComponent> structures = new ArrayList<>(components);

        for(Tuple<Rotation, BlockPos> tuple : HOLD_CROSS_POS) {

            if(!hasGeneratedOpenDoor && generateDoorOpen(template, tuple.getSecond(), rot.add(tuple.getFirst()))) {
                hasGeneratedOpenDoor = true;
            } else {
                if(!generatePuzzleDoor(template, tuple.getSecond(), rot.add(tuple.getFirst()))) {
                    failedHalls++;
                }
            }
        }

        if(failedHalls > 3) {
            components.clear();
            components.addAll(structures);
           // return this.generateEnd(parent, pos, rot);
        }

        return true;
    }

    private boolean generateSecondHoldLayer(BarrendCryptTemplate parent, BlockPos pos, Rotation rot) {
        String[] layer_1 = {"hold_3", "hold_4"};
        BarrendCryptTemplate template = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(-31, -9, 0), ModRand.choice(layer_1), rot);
        components.add(template);
        generateThirdHoldLayer(template, pos, rot);

        boolean hasGeneratedOpenDoor = false;
        int failedHalls = 0;
        List<StructureComponent> structures = new ArrayList<>(components);

        for(Tuple<Rotation, BlockPos> tuple : HOLD_CROSS_POS) {

            if(!hasGeneratedOpenDoor && generateDoorOpen(template, tuple.getSecond(), rot.add(tuple.getFirst()))) {
                hasGeneratedOpenDoor = true;
            } else {
                if(!generatePuzzleDoor(template, tuple.getSecond(), rot.add(tuple.getFirst()))) {
                    failedHalls++;
                }
            }
        }

        if(failedHalls > 3) {
            components.clear();
            components.addAll(structures);
            // return this.generateEnd(parent, pos, rot);
        }
        return true;
    }


    private boolean generateThirdHoldLayer(BarrendCryptTemplate parent, BlockPos pos, Rotation rot) {
        String[] layer_1 = {"hold_5", "hold_6"};
        BarrendCryptTemplate template = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(-31, -9, 0), ModRand.choice(layer_1), rot);
        components.add(template);
        generateGroundLayer(template, pos, rot);

        boolean hasGeneratedOpenDoor = false;
        int failedHalls = 0;
        List<StructureComponent> structures = new ArrayList<>(components);

        for(Tuple<Rotation, BlockPos> tuple : HOLD_CROSS_POS) {

            if(!hasGeneratedOpenDoor && generateDoorOpen(template, tuple.getSecond(), rot.add(tuple.getFirst()))) {
                hasGeneratedOpenDoor = true;
            } else {
                if(!generatePuzzleDoor(template, tuple.getSecond(), rot.add(tuple.getFirst()))) {
                    failedHalls++;
                }
            }
        }

        if(failedHalls > 3) {
            components.clear();
            components.addAll(structures);
            // return this.generateEnd(parent, pos, rot);
        }
        return true;
    }


    private boolean generateGroundLayer(BarrendCryptTemplate parent, BlockPos pos, Rotation rot) {
        BarrendCryptTemplate template = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(-31, -10, 0), "drop_end", rot);
        components.add(template);

        boolean hasGeneratedOpenDoor = false;
        int failedHalls = 0;
        List<StructureComponent> structures = new ArrayList<>(components);

        for(Tuple<Rotation, BlockPos> tuple : HOLD_CROSS_POS) {

            if(!hasGeneratedOpenDoor && generateDoorOpen(template, tuple.getSecond().add(0,1,0), rot.add(tuple.getFirst()))) {
                hasGeneratedOpenDoor = true;
            } else {
                if(!generatePuzzleDoor(template, tuple.getSecond().add(0,1,0), rot.add(tuple.getFirst()))) {
                    failedHalls++;
                }
            }
        }

        if(failedHalls > 3) {
            components.clear();
            components.addAll(structures);
            // return this.generateEnd(parent, pos, rot);
        }
        return true;
    }


    private boolean generateDoorOpen(BarrendCryptTemplate parent, BlockPos pos, Rotation rot) {
        BarrendCryptTemplate template = addAdjustedPieceWithoutDistance(parent, pos, "doors/door_open", rot);
        components.add(template);
        generateBaseStraight(template, BlockPos.ORIGIN, rot);

        return true;
    }

    private boolean generatePuzzleDoor(BarrendCryptTemplate parent, BlockPos pos, Rotation rot) {
        String[] doors = {"doors/door_1"};
        BarrendCryptTemplate template = addAdjustedPieceWithoutDistance(parent, pos, ModRand.choice(doors), rot);
        components.add(template);
        generateBaseStraight(template, BlockPos.ORIGIN, rot);
        return true;
    }


    private boolean generateBaseStraight(BarrendCryptTemplate parent, BlockPos pos, Rotation rot) {
        String[] straight_types = {"tiles/straight_1", "tiles/straight_2", "tiles/straight_3", "tiles/straight_4", "tiles/straight_5", "tiles/straight_6", "tile/straight_7"};
        BarrendCryptTemplate straight_template = addAdjustedPiece(parent, pos, ModRand.choice(straight_types), rot);

        if(straight_template.getDistance() > SIZE || straight_template.isCollidingExcParent(manager, parent, components)) {
            //generate End
            return this.generateQuickEnd(parent, pos, rot);
        }

        components.add(straight_template);

        int r = world.rand.nextInt(3);
        boolean genSuccess;

        if(r == 0) {
            genSuccess = generateBaseStraight(straight_template, BlockPos.ORIGIN, rot);
        }
        else {
                genSuccess = generateCross(straight_template, BlockPos.ORIGIN, rot);
        }

        if(!genSuccess) {
            components.remove(straight_template);
           // return this.generateEnd(parent, pos, rot);
            return this.generateQuickEnd(parent, pos, rot);
        }

        return true;
    }

    private boolean generateCross(BarrendCryptTemplate parent, BlockPos pos, Rotation rot) {
        String[] cross_types = {"tiles/cross_1", "tiles/cross_2", "tiles/cross_3"};
        BarrendCryptTemplate cross_template = addAdjustedPiece(parent, pos, ModRand.choice(cross_types), rot);

        if(cross_template.getDistance() > SIZE || cross_template.isCollidingExcParent(manager, parent, components)) {
            //generate End
            return this.generatePiecePart(parent, pos, rot);
        }

        components.add(cross_template);

        int failedSTarts = 0;
        for(Tuple<Rotation, BlockPos> tuple : LARGE_CROSS_POS) {
            if(!generateBaseStraight(cross_template, tuple.getSecond(), rot.add(tuple.getFirst()))) {
                failedSTarts++;
            }

        }
        List<StructureComponent> structures = new ArrayList<>(components);
        if(failedSTarts > 3) {
            components.clear();
            components.addAll(structures);
           // generateEnd(templateAdjusted, pos, rot);
            return this.generatePiecePart(parent, pos, rot);
        }

        return true;
    }

    private int chamberNumber = 0;
    private boolean generatedBossChamber = false;

    private boolean generateChamber(BarrendCryptTemplate parent, BlockPos pos, Rotation rot, int ID) {
        BarrendCryptTemplate chamber = null;

        if(ID == 1) {
            chamber = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(0,-4,0), "chamber/chamber_1", rot);
        } else if (ID == 2) {
            chamber = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(0,0,0), "chamber/chamber_2", rot);
        } else if (ID == 3) {
            chamber = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(0,-4,0), "chamber/chamber_3", rot);
        } else if (ID == 4) {
            chamber = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(0,-2,0), "chamber/chamber_4", rot);
        }

        if(chamber != null) {

            if(chamberNumber > 2 && !generatedBossChamber) {
                chamber = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(0,-9,0), "chamber/boss_chamber", rot);

                if(chamber.isCollidingExcParent(manager, parent, components)) {
                    return this.generateHelperStraight(parent, pos, rot);
                }

                chamberNumber++;
                components.add(chamber);
                generatedBossChamber = true;
                return true;
            }

            if(chamber.isCollidingExcParent(manager, parent, components)) {
                return this.generateHelperStraight(parent, pos, rot);
            }

            chamberNumber++;
            components.add(chamber);
            return true;
        }


        return false;
    }

    private boolean generateHelperStraight(BarrendCryptTemplate parent, BlockPos pos, Rotation rot) {
        String[] helper_straights = {"help_straight_1", "help_straight_2", "help_straight_3"};
        BarrendCryptTemplate straight = addAdjustedPieceWithoutDistance(parent, pos, ModRand.choice(helper_straights), rot);

        if(straight.isCollidingExcParent(manager, parent, components)) {
            return false;
        }

        int randChamberEvent = ModRand.range(1, 5);
        if(!generateChamberAttemptTwo(straight, pos, rot, randChamberEvent)) {
            return this.generateQuickEndAttempted(parent, pos, rot);
        }

        components.add(straight);
        return true;
    }

    private boolean generateChamberAttemptTwo(BarrendCryptTemplate parent, BlockPos pos, Rotation rot, int ID) {
        BarrendCryptTemplate chamber = null;

        if(ID == 1) {
            chamber = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(0,-4,0), "chamber/chamber_1", rot);
        } else if (ID == 2) {
            chamber = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(0,0,0), "chamber/chamber_2", rot);
        } else if (ID == 3) {
            chamber = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(0,-4,0), "chamber/chamber_3", rot);
        } else if (ID == 4) {
            chamber = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(0,-2,0), "chamber/chamber_4", rot);
        }

        if(chamber != null) {

            if(chamberNumber > 2 && !generatedBossChamber) {
                chamber = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(0,-9,0), "chamber/boss_chamber", rot);

                if(chamber.isCollidingExcParent(manager, parent, components)) {
                    return this.generateHelperStraight(parent, pos, rot);
                }

                chamberNumber++;
                components.add(chamber);
                generatedBossChamber = true;
                return true;
            }

            if(chamber.isCollidingExcParent(manager, parent, components)) {
                return false;
            }

            chamberNumber++;
            components.add(chamber);
            return true;
        }


        return false;
    }

    private boolean generateQuickEnd(BarrendCryptTemplate parent, BlockPos pos, Rotation rot) {
        //Seals all entryways from the Crypt
        if(world.rand.nextInt(3) == 0) {
            int randChamberEvent = ModRand.range(1, 5);
            return this.generateChamber(parent, pos, rot, randChamberEvent);
        }

        BarrendCryptTemplate template = addAdjustedPieceWithoutDistance(parent, pos, "quick_end", rot);
        components.add(template);
        return true;
    }

    private boolean generatePiecePart(BarrendCryptTemplate parent, BlockPos pos, Rotation rot) {
        //creates a divended between an endpoint and the the connector, used to keep chambers from offsetting and screwing up structure gen

        BarrendCryptTemplate template = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN, "piece_part", rot);
        components.add(template);
        generateQuickEnd(template, BlockPos.ORIGIN, rot);
        return true;
    }

    private boolean generateQuickEndAttempted(BarrendCryptTemplate parent, BlockPos pos, Rotation rot) {
        //Seals all entryways from the Crypt After attempting to spawn a chamber
        BarrendCryptTemplate template = addAdjustedPieceWithoutDistance(parent, pos, "quick_end", rot);
        components.add(template);
        return true;
    }





    /**
     * Adds a new piece, with the previous template a reference for position and
     * rotation
     */
    private BarrendCryptTemplate addAdjustedPiece(BarrendCryptTemplate parent, BlockPos pos, String type, Rotation rot) {
        BarrendCryptTemplate newTemplate = new BarrendCryptTemplate(manager, type, parent.getTemplatePosition(), rot, parent.getDistance() + 1, true);
        BlockPos blockpos = parent.getTemplate().calculateConnectedPos(parent.getPlacementSettings(), pos, newTemplate.getPlacementSettings(), BlockPos.ORIGIN);
        newTemplate.offset(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        adjustAndCenter(parent, newTemplate, rot);
        return newTemplate;
    }

    /**
     * Adds a new piece, with the previous template a reference for position and
     * rotation
     */
    private BarrendCryptTemplate addAdjustedPieceWithoutDistance(BarrendCryptTemplate parent, BlockPos pos, String type, Rotation rot) {
        BarrendCryptTemplate newTemplate = new BarrendCryptTemplate(manager, type, parent.getTemplatePosition(), rot, parent.getDistance(), true);
        BlockPos blockpos = parent.getTemplate().calculateConnectedPos(parent.getPlacementSettings(), pos, newTemplate.getPlacementSettings(), BlockPos.ORIGIN);
        newTemplate.offset(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        adjustAndCenter(parent, newTemplate, rot);
        return newTemplate;
    }


    /**
     * Centers a template to line up on the x, and in the center with z
     */
    private void adjustAndCenter(BarrendCryptTemplate parent, BarrendCryptTemplate child, Rotation rot) {
        BlockPos adjustedPos = new BlockPos(parent.getTemplate().getSize().getX(), 0, (parent.getTemplate().getSize().getZ() - child.getTemplate().getSize().getZ()) / 2f)
                .rotate(rot);
        child.offset(adjustedPos.getX(), adjustedPos.getY(), adjustedPos.getZ());
    }
}
