package com.example.structure.world.api.ashtower;

import com.example.structure.config.ModConfig;
import com.example.structure.util.ModRand;
import com.example.structure.world.api.vaults.VaultTemplate;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.List;

public class AshTower {

    private List<StructureComponent> components;
    private World world;
    private TemplateManager manager;






    private static final int SIZE = 4;

    public AshTower(World worldIn, TemplateManager template, List<StructureComponent> components) {
        this.world = worldIn;
        this.manager = template;
        this.components = components;

    }

    public void startBaseTower(BlockPos pos, Rotation rot) {
        System.out.println("Generating Tower at" + pos);
        String[] base_types = {"base_1", "base_2"};
        AshTowerTemplate template = new AshTowerTemplate(manager, "base_2", pos, rot, 0, true);
        components.add(template);
        VaultTemplate.resetTemplateCount();
        generateFirstLayer(template, pos.add(0, 16, 0), rot);
    }



    public boolean generateFirstLayer(AshTowerTemplate parent, BlockPos pos, Rotation rot) {
        String[] lower_level_layers = {"layer_1", "layer_2"};
        AshTowerTemplate template = addAdjustedPiece(parent, pos, ModRand.choice(lower_level_layers), rot);
        if(template.isCollidingExcParent(manager, parent, components)) {
            System.out.println("collided With Another Part");
            return false;

        }
        components.add(template);
        //Generate Second
        generateSecondLayer(template, pos.add(0, 16, 0), rot);

        return true;
    }

    public boolean generateSecondLayer(AshTowerTemplate parent, BlockPos pos, Rotation rot) {
        String[] upper_level_layers = {"layer_3", "layer_4"};
        AshTowerTemplate template = addAdjustedPiece(parent, pos, ModRand.choice(upper_level_layers), rot);
        if(template.isCollidingExcParent(manager, parent, components)) {
            System.out.println("collided With Another Part");
            return false;

        }
        components.add(template);
        //Generate Second
        generateThirdLayer(template, pos.add(0, 16, 0), rot);

        return true;
    }

    public boolean generateThirdLayer(AshTowerTemplate parent, BlockPos pos, Rotation rot) {
        String[] highest_level_layers = {"layer_5"};

        AshTowerTemplate template = addAdjustedPiece(parent, pos, ModRand.choice(highest_level_layers), rot);
        if(template.isCollidingExcParent(manager, parent, components)) {
            System.out.println("collided With Another Part");
            return false;

        }
        components.add(template);
        //Generate Roof or contnue on this layer
        if(SIZE >= template.getDistance()) {
            generateTopLayer(template, pos.add(0, 16, 0), rot);
        } else {
            generateThirdLayer(template, pos.add(0, 16, 0), rot);
        }


        return true;
    }

    public boolean generateTopLayer(AshTowerTemplate parent, BlockPos pos, Rotation rot) {
        String[] roof_level_layers = {"top_1"};
        AshTowerTemplate template = addAdjustedPiece(parent, pos, ModRand.choice(roof_level_layers), rot);
        if(template.isCollidingExcParent(manager, parent, components)) {
            System.out.println("collided With Another Part");
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
