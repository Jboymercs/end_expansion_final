package com.example.structure.world.Biome.altardungeon;

import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import com.example.structure.world.Biome.WorldChunkGeneratorEE;
import com.example.structure.world.misc.GenUtils;
import com.google.common.collect.Lists;
import net.minecraft.util.Rotation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;
import scala.xml.dtd.impl.Base;

import java.util.ArrayList;
import java.util.List;

public class AltarDungeon {

    private List<StructureComponent> components;
    private World world;
    private TemplateManager templateManager;
    private WorldChunkGeneratorEE provider;
    private static final int hall_size = 5;
    private static final int connecter_size = 4;
    private static final int SIZE = 7;
    private int groundRequired = 40;
    private int pieces;

    private static final List<Tuple<Rotation, BlockPos>> HALL_POS = Lists.newArrayList(new Tuple(Rotation.NONE, new BlockPos(0, 0, 0)),
            new Tuple(Rotation.CLOCKWISE_90, new BlockPos(4, 0, 0)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, 0, 4)));
    public AltarDungeon(World world, TemplateManager manager, WorldChunkGeneratorEE provider, List<StructureComponent> components) {
        this.components = components;
        this.provider = provider;
        this.templateManager = manager;
        this.world = world;
    }

    //Staircase
    public void startStaircase(BlockPos blockPos, Rotation rot) {

        if (provider.isIslandChunk(blockPos.getX(), blockPos.getZ())) {
            AltarTemplate template = new AltarTemplate(templateManager, "yellow_start", blockPos, rot, 0, true);
                components.add(template);
                //Keep Just in case

                generateCross(template, blockPos, rot);
                AltarTemplate.resetTemplateCount();

        } else {
            System.out.println("tried generating Altar in void");
        }


    }

    private boolean generateCross(AltarTemplate parent, BlockPos pos, Rotation rot) {
        String[] rooms = {"orange_cross"};
        AltarTemplate template = addAdjustedPiece(parent, pos, ModRand.choice(rooms), rot);

        if (template.isCollidingExcParent(templateManager, parent, components) || template.getDistance() > SIZE) {
            return false;
        }



        List<StructureComponent> structures = new ArrayList(components);
        pieces++;
        components.add(template);



            for (Tuple<Rotation, BlockPos> tuple : HALL_POS) {
                if (!generateHall(template, tuple.getSecond(), rot.add(tuple.getFirst()))) {
                    components.clear();
                    components.addAll(structures);
                    boolean successful = generateEnd(parent, pos.add(new BlockPos(0, 0, 0)), rot);
                    return successful;
                }
            }

        return true;
    }

    private boolean generateHall(AltarTemplate parent, BlockPos pos, Rotation rot) {
        String[] rooms = {"green_hall"};
        AltarTemplate template = addAdjustedPiece(parent, pos, ModRand.choice(rooms), rot);

        if (template.isCollidingExcParent(templateManager, parent, components) || template.getDistance() > SIZE) {
            return false;
        }



        components.add(template);
        pieces++;



        int r = world.rand.nextInt(3);
        boolean genSuccess;

        if (r == 0) {
            genSuccess = generateHall(template, BlockPos.ORIGIN, rot);
        } else {
            genSuccess = generateCross(template, BlockPos.ORIGIN, rot);
        }

        /*
         * If furthering the hallways failed, then turn this template into an end
         */
        if (!genSuccess) {
            components.remove(template);
            return this.generateEnd(parent, pos, rot);
        }

        return true;
    }




    private boolean generateEnd(AltarTemplate parent, BlockPos pos, Rotation rot) {
        AltarTemplate template = addAdjustedPiece(parent, pos, "purple_end", rot);
        if (template.isCollidingExcParent(templateManager, parent, components) || template.getDistance() > SIZE || ModUtils.chunksGenerated(template.getBoundingBox(), world)) {
            return false;
        }
        components.add(template);
        return true;
    }
    /**
     * Adds a new piece, with the previous template a reference for position and
     * rotation
     */
    private AltarTemplate addAdjustedPiece(AltarTemplate parent, BlockPos pos, String type, Rotation rot) {
        AltarTemplate newTemplate = new AltarTemplate(templateManager, type, parent.getTemplatePosition(), rot, parent.getDistance() + 1, true);
        BlockPos blockpos = parent.getTemplate().calculateConnectedPos(parent.getPlacementSettings(), pos, newTemplate.getPlacementSettings(), BlockPos.ORIGIN);
        newTemplate.offset(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        adjustAndCenter(parent, newTemplate, rot);
        return newTemplate;
    }

    /**
     * Centers a template to line up on the x, and in the center with z
     */
    private void adjustAndCenter(AltarTemplate parent, AltarTemplate child, Rotation rot) {
        BlockPos adjustedPos = new BlockPos(parent.getTemplate().getSize().getX(), 0, (parent.getTemplate().getSize().getZ() - child.getTemplate().getSize().getZ()) / 2f)
                .rotate(rot);
        child.offset(adjustedPos.getX(), adjustedPos.getY(), adjustedPos.getZ());
    }
}
