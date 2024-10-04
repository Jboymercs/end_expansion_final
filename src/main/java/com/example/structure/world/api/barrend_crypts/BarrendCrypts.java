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

import java.util.List;

public class BarrendCrypts {

    private List<StructureComponent> components;
    private World world;
    private TemplateManager manager;

    private static final int SIZE = 0;

    private static final List<Tuple<Rotation, BlockPos>> HOLD_CROSS_POS = Lists.newArrayList(new Tuple(Rotation.NONE, new BlockPos(0, 0, 0)),
            new Tuple(Rotation.CLOCKWISE_90, new BlockPos(29, 0, 0)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, 0, 29)));

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
        return true;
    }

    private boolean generateSecondHoldLayer(BarrendCryptTemplate parent, BlockPos pos, Rotation rot) {
        String[] layer_1 = {"hold_3", "hold_4"};
        BarrendCryptTemplate template = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(-31, -9, 0), ModRand.choice(layer_1), rot);
        components.add(template);
        generateThirdHoldLayer(template, pos, rot);
        return true;
    }


    private boolean generateThirdHoldLayer(BarrendCryptTemplate parent, BlockPos pos, Rotation rot) {
        String[] layer_1 = {"hold_5", "hold_6"};
        BarrendCryptTemplate template = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(-31, -9, 0), ModRand.choice(layer_1), rot);
        components.add(template);
        generateGroundLayer(template, pos, rot);
        return true;
    }


    private boolean generateGroundLayer(BarrendCryptTemplate parent, BlockPos pos, Rotation rot) {
        BarrendCryptTemplate template = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(-31, -10, 0), "drop_end", rot);
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
