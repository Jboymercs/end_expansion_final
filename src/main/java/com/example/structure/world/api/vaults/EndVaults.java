package com.example.structure.world.api.vaults;

import com.example.structure.config.ModConfig;
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


import java.util.ArrayList;
import java.util.List;

public class EndVaults {

    private List<StructureComponent> components;
    private World world;
    private TemplateManager manager;


    private int yAxel = 0;


    private int yAxel2Level = 0;

    private static final int SIZE = ModConfig.vault_size;

    private static final int SECOND_SIZE = SIZE * 2;

    private boolean hasSpawnedSecondlevel = false;

    private boolean hasGeneratedBossRooms = false;

    private static final List<Tuple<Rotation, BlockPos>> CROSS_POS = Lists.newArrayList(new Tuple(Rotation.NONE, new BlockPos(0, 0, 0)),
            new Tuple(Rotation.CLOCKWISE_90, new BlockPos(13, 0, 0)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, 0, 13)));


    public EndVaults(World worldIn, TemplateManager template, List<StructureComponent> components) {
        this.world = worldIn;
        this.manager = template;
        this.components = components;

    }

    public void startVault(BlockPos pos, Rotation rot) {
            yAxel = pos.getY();
            yAxel2Level = pos.getY() - 9;
            VaultTemplate templateAdjusted = new VaultTemplate(manager, "start", pos, rot, 0, true);
            components.add(templateAdjusted);
        generateCross(templateAdjusted, BlockPos.ORIGIN, rot);
            VaultTemplate.resetTemplateCount();

        int failedSTarts = 0;
        for(Tuple<Rotation, BlockPos> tuple : CROSS_POS) {
            if(!generateCross(templateAdjusted, tuple.getSecond(), rot.add(tuple.getFirst()))) {
                failedSTarts++;
            }

        }
        List<StructureComponent> structures = new ArrayList<>(components);
        if(failedSTarts > 3) {
            components.clear();
            components.addAll(structures);
            generateEnd(templateAdjusted, pos, rot);
        }
    }



    public boolean generateCross(VaultTemplate parent, BlockPos pos, Rotation rot) {
        String[] cross_types = {"cross_1", "cross_2", "cross_3"};
        VaultTemplate template = addAdjustedPiece(parent, pos, ModRand.choice(cross_types), rot);
        if(template.isCollidingExcParent(manager, parent, components) || template.getDistance() > SIZE || getGroundFromAbove(world, pos.getX(), pos.getZ()) < yAxel) {
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


    private boolean generateHall(VaultTemplate parent, BlockPos pos, Rotation rot) {
        //Basic Hallway
        String[] hall_types = {"straight_1", "straight_2", "straight_3", "straight_4"};
        VaultTemplate template = addAdjustedPiece(parent, pos, ModRand.choice(hall_types), rot);

        if(template.isCollidingExcParent(manager, parent, components) || template.getDistance() > SIZE || getGroundFromAbove(world, pos.getX(), pos.getZ()) <= yAxel) {
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
            int r2 = world.rand.nextInt(2);
            if(r2 != 0 && !hasSpawnedSecondlevel) {
                genSuccess = generate2Start(template, BlockPos.ORIGIN, rot);
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

    private boolean generateEnd(VaultTemplate parent, BlockPos pos, Rotation rot) {
        String[] end_types = {"end_2", "end_3", "end_4", "end_6"};
        VaultTemplate template = addAdjustedPiece(parent, pos, ModRand.choice(end_types), rot);
        if(template.isCollidingExcParent(manager, parent, components) || template.getDistance() > SIZE) {
            return false;
        }
        components.add(template);

        return true;
    }


    private boolean generate2Start(VaultTemplate parent, BlockPos pos, Rotation rot) {
        VaultTemplate template = addAdjustedPiece(parent, pos, "2_start", rot);
        VaultTemplate templateConnect = addAdjustedPiece(parent, pos.add(0, -10, 0), "2_connect", rot);
        if(template.isCollidingExcParent(manager, parent, components) || template.getDistance() > SECOND_SIZE || getGroundFromAbove(world, pos.getX(), pos.getZ()) <= yAxel
        || getGroundFromAbove(world, pos.getX(), pos.getZ()) <= yAxel2Level || getGroundFromAbove(world, pos.getX() + 8, pos.getZ() + 8) <= yAxel2Level) {
            return false;
        }
        components.add(template);
        components.add(templateConnect);
        hasSpawnedSecondlevel = true;
        List<StructureComponent> structures = new ArrayList<>(components);


        int failedSTarts = 0;
        for(Tuple<Rotation, BlockPos> tuple : CROSS_POS) {
            if(!generateCrossLevel(template, tuple.getSecond().add(0, -10, 0), rot.add(tuple.getFirst())) || getGroundFromAbove(world, pos.getX(), pos.getZ()) <= yAxel2Level) {
                failedSTarts++;
            }

        }

        if(failedSTarts > 3) {
            components.clear();
            components.addAll(structures);
            generateEndLevel(template, pos.add(0, -10, 0), rot);
        }
        return true;
    }

    //Mini-Boss room
    public boolean generateBossRoom(VaultTemplate parent, BlockPos pos, Rotation rot) {
        VaultTemplate template = addAdjustedPiece(parent, pos, "boss_room", rot);

        if(template.isCollidingExcParent(manager, parent, components) || template.getDistance() > SECOND_SIZE || getGroundFromAbove(world, pos.getX(), pos.getZ()) <= yAxel2Level) {
            return false;

        }

        List<StructureComponent> structures = new ArrayList<>(components);
        hasGeneratedBossRooms = true;
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
            return this.generateEndLevel(parent, pos, rot);
        }
        return true;
    }

    public boolean generateCrossLevelAfterBoss(VaultTemplate parent, BlockPos pos, Rotation rot) {
        String[] cross_types = {"2_cross_1", "2_cross_2", "2_cross_3", "2_cross_4"};
        VaultTemplate template = addAdjustedPiece(parent, pos, ModRand.choice(cross_types), rot);
        if(template.isCollidingExcParent(manager, parent, components) || template.getDistance() > SECOND_SIZE || getGroundFromAbove(world, pos.getX(), pos.getZ()) <= yAxel2Level) {
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
            return this.generateEndLevel(parent, pos, rot);
        }
        return true;
    }

    public boolean generateCrossLevel(VaultTemplate parent, BlockPos pos, Rotation rot) {
        String[] cross_types = {"2_cross_1", "2_cross_2", "2_cross_3", "2_cross_4"};
        VaultTemplate template = addAdjustedPiece(parent, pos, ModRand.choice(cross_types), rot);
        if(template.isCollidingExcParent(manager, parent, components) || template.getDistance() > SECOND_SIZE || getGroundFromAbove(world, pos.getX(), pos.getZ()) <= yAxel2Level) {
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
            return this.generateEndLevel(parent, pos, rot);
        }
        return true;
    }

    private boolean generateHallLevel(VaultTemplate parent, BlockPos pos, Rotation rot) {
        //Basic Hallway
        String[] hall_types = {"2_straight_1", "2_straight_2", "2_straight_3", "2_straight_4"};
        VaultTemplate template = addAdjustedPiece(parent, pos, ModRand.choice(hall_types), rot);

        if(template.isCollidingExcParent(manager, parent, components) || template.getDistance() > SECOND_SIZE || getGroundFromAbove(world, pos.getX(), pos.getZ()) <= yAxel2Level) {
            return false;
        }



        //Add current Hallway
        components.add(template);

        int r = world.rand.nextInt(3);
        boolean genSuccess;

        if(r == 0) {
            genSuccess = generateHallLevel(template, BlockPos.ORIGIN, rot);
        }
        else {
            //This is basically saying that if it hasn't generated a boss room, generate a cross
            if(!hasGeneratedBossRooms) {
                genSuccess = generateBossRoom(template, BlockPos.ORIGIN, rot);
            } else {
                genSuccess = generateCrossLevelAfterBoss(template, BlockPos.ORIGIN, rot);
            }

        }

        if(!genSuccess) {
            components.remove(template);
            return this.generateEndLevel(parent, pos, rot);
        }


        return true;
    }

    private boolean generateEndLevel(VaultTemplate parent, BlockPos pos, Rotation rot) {
        String[] end_types = {"end_1", "end_2", "end_3", "end_4", "end_5"};
        VaultTemplate template = addAdjustedPiece(parent, pos, ModRand.choice(end_types), rot);
        if(template.isCollidingExcParent(manager, parent, components) || template.getDistance() > SECOND_SIZE) {
            return false;
        }
        components.add(template);

        return true;
    }

    public static int getGroundFromAbove(World world, int x, int z)
    {
        int y = 255;
        boolean foundGround = false;
        while(!foundGround && y-- >= 31)
        {
            Block blockAt = world.getBlockState(new BlockPos(x,y,z)).getBlock();
            foundGround =  blockAt == Blocks.END_STONE;
        }

        return y;
    }

    /**
     * Adds a new piece, with the previous template a reference for position and
     * rotation
     */
    private VaultTemplate addAdjustedPiece(VaultTemplate parent, BlockPos pos, String type, Rotation rot) {
        VaultTemplate newTemplate = new VaultTemplate(manager, type, parent.getTemplatePosition(), rot, parent.getDistance() + 1, true);
        BlockPos blockpos = parent.getTemplate().calculateConnectedPos(parent.getPlacementSettings(), pos, newTemplate.getPlacementSettings(), BlockPos.ORIGIN);
        newTemplate.offset(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        adjustAndCenter(parent, newTemplate, rot);
        return newTemplate;
    }


    /**
     * Centers a template to line up on the x, and in the center with z
     */
    private void adjustAndCenter(VaultTemplate parent, VaultTemplate child, Rotation rot) {
        BlockPos adjustedPos = new BlockPos(parent.getTemplate().getSize().getX(), 0, (parent.getTemplate().getSize().getZ() - child.getTemplate().getSize().getZ()) / 2f)
                .rotate(rot);
        child.offset(adjustedPos.getX(), adjustedPos.getY(), adjustedPos.getZ());
    }

}
