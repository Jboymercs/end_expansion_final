package com.example.structure.world.api.ashtower;


import com.example.structure.util.ModRand;
import com.example.structure.util.integration.ModIntegration;
import com.google.common.collect.Lists;
import net.minecraft.util.Rotation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.ArrayList;
import java.util.List;

public class AshTower {

    private List<StructureComponent> components;
    private World world;
    private TemplateManager manager;


    private static final List<Tuple<Rotation, BlockPos>> CROSS_POS = Lists.newArrayList(new Tuple(Rotation.NONE, new BlockPos(0, 0, 0)),
            new Tuple(Rotation.CLOCKWISE_90, new BlockPos(20, 0, 0)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, 0, 20)));



    private static final int SIZE = 8;

    public AshTower(World worldIn, TemplateManager template, List<StructureComponent> components) {
        this.world = worldIn;
        this.manager = template;
        this.components = components;

    }

    public void startBaseTower(BlockPos pos, Rotation rot) {
        AshTowerTemplate template;

        if(ModIntegration.IS_DEEPER_DEPTHS_LOADED) {
            String[] base_types = {"compat/base_1", "compat/base_2"};
            template = new AshTowerTemplate(manager, ModRand.choice(base_types), pos, rot, 0, true);
        } else {
            String[] base_types = {"base_1", "base_2"};
            template = new AshTowerTemplate(manager, ModRand.choice(base_types), pos, rot, 0, true);
        }

        components.add(template);
        AshTowerTemplate.resetTemplateCount();
        generateLayer(template, BlockPos.ORIGIN, rot);

        generateWalkWays(template, BlockPos.ORIGIN, rot);
    }

    public boolean generateLayer(AshTowerTemplate parent, BlockPos pos, Rotation rotation) {
        BlockPos posToo = pos.add(-21, 16, 0);

        AshTowerTemplate template;
        if(ModIntegration.IS_DEEPER_DEPTHS_LOADED) {
            String[] lower_level_layers = {"compat/layer_1", "compat/layer_2", "compat/layer_7", "compat/layer_8"};
            template = addAdjustedPiece(parent, posToo, ModRand.choice(lower_level_layers), rotation);
        } else {
            String[] lower_level_layers = {"layer_1", "layer_2", "layer_7", "layer_8"};
            template = addAdjustedPiece(parent, posToo, ModRand.choice(lower_level_layers), rotation);
        }


        if(template.isCollidingExcParent(manager, parent, components)) {
            return false;
        }

        components.add(template);
        generateSecondLayer(template, pos, rotation);
        return true;
    }

    public boolean generateWalkWays(AshTowerTemplate parent, BlockPos pos, Rotation rot) {
        AshTowerTemplate template = addAdjustedPiece(parent, pos, "walkway_1", rot);

        if(template.isCollidingExcParent(manager, parent, components)) {
            return false;
        }
        components.add(template);
        return true;
    }


    public boolean generateSecondLayer(AshTowerTemplate parent, BlockPos pos, Rotation rot) {
        BlockPos posToo = pos.add(-21, 16, 0);
        AshTowerTemplate template;
        if(ModIntegration.IS_DEEPER_DEPTHS_LOADED) {
            String[] upper_level_layers = {"compat/layer_3", "compat/layer_4", "compat/layer_6", "compat/layer_8"};
            template = addAdjustedPiece(parent, posToo, ModRand.choice(upper_level_layers), rot);
        } else {
            String[] upper_level_layers = {"layer_3", "layer_4", "layer_6", "layer_8"};
            template = addAdjustedPiece(parent, posToo, ModRand.choice(upper_level_layers), rot);
        }

        if(template.isCollidingExcParent(manager, parent, components)) {
            return false;

        }

        if(template.getDistance() >= SIZE) {
            return generateTopLayer(template, pos, rot);
        }
        components.add(template);
        //Generate Second
       generateThirdLayer(template, pos, rot);

        return true;
    }

    public boolean generateThirdLayer(AshTowerTemplate parent, BlockPos pos, Rotation rot) {

        BlockPos posToo = pos.add(-21, 16, 0);
        AshTowerTemplate template;
        if(ModIntegration.IS_DEEPER_DEPTHS_LOADED) {
            String[] highest_level_layers = {"compat/layer_5", "compat/layer_3", "compat/layer_7", "compat/layer_6"};
            template = addAdjustedPiece(parent, posToo, ModRand.choice(highest_level_layers), rot);
        } else {
            String[] highest_level_layers = {"layer_5", "layer_3", "layer_7", "layer_6"};
            template = addAdjustedPiece(parent, posToo, ModRand.choice(highest_level_layers), rot);
        }

        if(template.isCollidingExcParent(manager, parent, components)) {
            return false;

        }



        components.add(template);
        //Generate Roof or contnue on this layer
        if(SIZE >= template.getDistance()) {
            generateTopLayer(template, pos, rot);
        } else {
            generateThirdLayer(template, pos, rot);
        }


        return true;
    }

    public boolean generateTopLayer(AshTowerTemplate parent, BlockPos pos, Rotation rot) {
        BlockPos posToo = pos.add(-21, 16, 0);
        AshTowerTemplate template;
        if(ModIntegration.IS_DEEPER_DEPTHS_LOADED) {
            String[] roof_level_layers = {"compat/top_1", "compat/top_2"};
            template = addAdjustedPiece(parent, posToo, ModRand.choice(roof_level_layers), rot);
        } else {
            String[] roof_level_layers = {"top_1", "top_2"};
            template = addAdjustedPiece(parent, posToo, ModRand.choice(roof_level_layers), rot);
        }

        if(template.isCollidingExcParent(manager, parent, components)) {
            return false;

        }
        components.add(template);
        //Generate Roof


        return true;
    }




    /**
     * Adds a new piece, with the previous template a reference for position and
     * rotation
     */
    private AshTowerTemplate addAdjustedPiece(AshTowerTemplate parent, BlockPos pos, String type, Rotation rot) {
        AshTowerTemplate newTemplate = new AshTowerTemplate(manager, type, parent.getTemplatePosition(), rot, parent.getDistance() + 1, true);
        BlockPos blockpos = parent.getTemplate().calculateConnectedPos(parent.getPlacementSettings(), pos, newTemplate.getPlacementSettings(), BlockPos.ORIGIN);
        newTemplate.offset(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        adjustAndCenter(parent, newTemplate, rot);
        return newTemplate;
    }


    /**
     * Centers a template to line up on the x, and in the center with z
     */
    private void adjustAndCenter(AshTowerTemplate parent, AshTowerTemplate child, Rotation rot) {
        BlockPos adjustedPos = new BlockPos(parent.getTemplate().getSize().getX(), 0, (parent.getTemplate().getSize().getZ() - child.getTemplate().getSize().getZ()) / 2f)
                .rotate(rot);
        child.offset(adjustedPos.getX(), adjustedPos.getY(), adjustedPos.getZ());
    }



}
