package com.example.structure.world.api.structures;

import com.example.structure.config.ModConfig;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import com.example.structure.world.misc.GenUtils;
import com.google.common.collect.Lists;
import git.jbredwards.nether_api.api.world.INetherAPIChunkGenerator;
import git.jbredwards.nether_api.mod.common.world.gen.ChunkGeneratorTheEnd;
import net.minecraft.util.Rotation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;
import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.List;

public class KingFortress {
    private List<StructureComponent> components;
    private World world;
    private TemplateManager manager;
    private INetherAPIChunkGenerator provider;


    private static final int SIZE = ModConfig.fortress_size;

    private static int BUILDINGS = 0;

    private static final List<Tuple<Rotation, BlockPos>> FORTRESS_POS = Lists.newArrayList(new Tuple(Rotation.NONE, new BlockPos(0, 0, 0)),
            new Tuple(Rotation.CLOCKWISE_90, new BlockPos(60, 0, 0)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, 0, 60)));
    private static final List<Tuple<Rotation, BlockPos>> CROSS_POS = Lists.newArrayList(new Tuple(Rotation.NONE, new BlockPos(0, 0, 0)),
            new Tuple(Rotation.CLOCKWISE_90, new BlockPos(30, 0, 0)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, 0, 30)));


    //Goal is to make a 2 level structure that when breaking island side surfaces, it will change it's room layout. With one of the rooms holding the bossRoom keyBlock
    public KingFortress(World worldIn, TemplateManager manager, INetherAPIChunkGenerator provider, List<StructureComponent> components) {
        this.world = worldIn;
        this.manager = manager;
        this.provider = provider;
        this.components = components;

    }

    public void startFortress(BlockPos pos, Rotation rot) {

        FortressTemplate templateStart = new FortressTemplate(manager, "start", pos, rot, 0, true);
            components.add(templateStart);
            BUILDINGS = 0;
            //generateCross(templateStart, BlockPos.ORIGIN.add(0, 5, 0), rot);
            FortressTemplate.resetTemplateCount();

        int failedSTarts = 0;
        for(Tuple<Rotation, BlockPos> tuple : FORTRESS_POS) {
            if(!generateCross(templateStart, tuple.getSecond().add(0, 5, 0), rot.add(tuple.getFirst()))) {
                failedSTarts++;
            }

        }
        List<StructureComponent> structures = new ArrayList<>(components);
        if(failedSTarts > 3) {
            components.clear();
            components.addAll(structures);
            generateEnd(templateStart, pos.add(0, 5, 0), rot);
        }
    }

    private boolean generateBuilding(FortressTemplate parent, BlockPos pos, Rotation rotation) {
        String[] buiilding_types = {"tower_1", "tower_2", "tower_3"};
        FortressTemplate template = addAdjustedPiece(parent, pos, ModRand.choice(buiilding_types), rotation);

        if(template.isCollidingExcParent(manager, parent, components) || template.getDistance() > SIZE || BUILDINGS >= 3) {
            return false;
        }


        //For the Lower Building
        String[] lower_building_types = {"lower_tower_1", "lower_tower_2"};
        FortressTemplate lowerTowerTemplate = addAdjustedPiece(parent, pos.add(0, -31, 0), ModRand.choice(lower_building_types), rotation);

        //Add CurrentCross
        List<StructureComponent> structures = new ArrayList<>(components);
        components.add(template);
        components.add(lowerTowerTemplate);
        BUILDINGS++;


        int failedHalls = 0;
        for(Tuple<Rotation, BlockPos> tuple : CROSS_POS) {
            if(!generateHall(template, tuple.getSecond(), rotation.add(tuple.getFirst()))) {
                failedHalls++;
            }
        }

        if(failedHalls > 3) {
            components.clear();
            components.addAll(structures);
            return this.generateEnd(parent, pos, rotation);
        }
        return true;

    }
    private boolean generateCross(FortressTemplate parent, BlockPos pos, Rotation rot) {
        //Basic Cross Design
                String[] cross_types = {"cross_1", "cross_2", "cross_3", "cross_4"};
                FortressTemplate template = addAdjustedPiece(parent, pos, ModRand.choice(cross_types), rot);

                if(template.isCollidingExcParent(manager, parent, components) || template.getDistance() > SIZE) {
                    return false;
                }

                //Add Current Cross
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

    private boolean generateHall(FortressTemplate parent, BlockPos pos, Rotation rot) {
            //Basic Hallway
            String[] hall_types = {"straight_1", "straight_2", "straight_3", "straight_4"};
            FortressTemplate template = addAdjustedPiece(parent, pos, ModRand.choice(hall_types), rot);

            if(template.isCollidingExcParent(manager, parent, components) || template.getDistance() > SIZE) {
                return false;
            }

            //Add current Hallway
            components.add(template);

            int r = world.rand.nextInt(3);
            boolean genSuccess;

            if(r == 0) {
                genSuccess = generateHall(template, BlockPos.ORIGIN, rot);
            }
            else {
                //This check is to add the way of choosing between a regular cross and a building
                if(BUILDINGS == 0) {
                    //Always generate atleast 1 Building/Afterwards it is a chance for a second one to spawn
                    genSuccess = generateBuilding(template, BlockPos.ORIGIN, rot);
                } else {

                    int b = world.rand.nextInt(3);
                    if (b == 0 && BUILDINGS <= 3) {
                        genSuccess = generateBuilding(template, BlockPos.ORIGIN, rot);
                    } else {
                        genSuccess = generateCross(template, BlockPos.ORIGIN, rot);
                    }
                }
            }

            if(!genSuccess) {
                components.remove(template);
                return this.generateEnd(parent, pos, rot);
            }


        return true;
    }

    private boolean generateEnd(FortressTemplate parent, BlockPos pos, Rotation rot) {
            String[] end_types = {"end_1", "end_2", "end_2"};
            FortressTemplate template = addAdjustedPiece(parent, pos, ModRand.choice(end_types), rot);
            if(template.isCollidingExcParent(manager, parent, components) || template.getDistance() > SIZE) {
                return false;
            }
            components.add(template);

        return true;
    }


    /**
     * Adds a new piece, with the previous template a reference for position and
     * rotation
     */
    private FortressTemplate addAdjustedPiece(FortressTemplate parent, BlockPos pos, String type, Rotation rot) {
        FortressTemplate newTemplate = new FortressTemplate(manager, type, parent.getTemplatePosition(), rot, parent.getDistance() + 1, true);
        BlockPos blockpos = parent.getTemplate().calculateConnectedPos(parent.getPlacementSettings(), pos, newTemplate.getPlacementSettings(), BlockPos.ORIGIN);
        newTemplate.offset(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        adjustAndCenter(parent, newTemplate, rot);
        return newTemplate;
    }

    /**
     * Centers a template to line up on the x, and in the center with z
     */
    private void adjustAndCenter(FortressTemplate parent, FortressTemplate child, Rotation rot) {
        BlockPos adjustedPos = new BlockPos(parent.getTemplate().getSize().getX(), 0, (parent.getTemplate().getSize().getZ() - child.getTemplate().getSize().getZ()) / 2f)
                .rotate(rot);
        child.offset(adjustedPos.getX(), adjustedPos.getY(), adjustedPos.getZ());
    }
}
