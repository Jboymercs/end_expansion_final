package com.example.structure.command;

import com.example.structure.config.WorldConfig;
import com.example.structure.util.misc.EELogger;
import com.google.common.base.Functions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.BiomeDictionary;

import javax.annotation.Nullable;
import java.util.*;

public class CommandLocateEE implements ICommand {

    private final List<String> aliases;
    public CommandLocateEE() {
        aliases = new ArrayList<>();
        aliases.add("locateEE");
    }

    @Override
    public String getName() {
        return "locateEE";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "locateEE <mod location>";
    }

    @Override
    public List<String> getAliases() {
        return this.aliases;
    }


    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 1) {
            throw new WrongUsageException("locateEE <mod location>");
        } else {
            String s = args[0];
            if(s.equals("LamentedIslands")) {
                BlockPos blockpos = findNearestPosLamentedIslands(sender);

                if (blockpos != null && WorldConfig.lamented_islands_enabled) {
                    sender.sendMessage(new TextComponentTranslation("commands.locate.success", new Object[]{s, blockpos.getX(), blockpos.getZ()}));
                } else {
                    throw new CommandException("commands.locate.failure", s);
                }
            }
        }
    }


    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return sender.canUseCommand(this.getRequiredPermissionLevel(), this.getName());
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, "LamentedIslands") : Collections.emptyList();
    }

    public static BlockPos findNearestPosLamentedIslands(ICommandSender sender) {
        BlockPos resultpos = null;
        BlockPos pos = sender.getPosition();
        World world = sender.getEntityWorld();
        Chunk chunk = world.getChunk(pos);
        //probably laggy as hell but hey it works
        if(Math.abs(chunk.x) > 35 && Math.abs(chunk.z) > 35 && world.provider.getDimension() == 1) {
            for (int i = -WorldConfig.lamented_islands_search_distance; i < WorldConfig.lamented_islands_search_distance + 1; i++) {
                for (int j = -WorldConfig.lamented_islands_search_distance; j < WorldConfig.lamented_islands_search_distance + 1; j++) {
                    boolean c = IsLamentedIslandsAtPos(world, chunk.x + i, chunk.z + j);
                    if (c) {
                        resultpos = new BlockPos((chunk.x + i) << 4, 100, (chunk.z + j) << 4);
                        break;
                    }
                }
            }
        }
        return resultpos;
    }

    protected static boolean IsLamentedIslandsAtPos(World world, int chunkX, int chunkZ) {
        int spacing = WorldConfig.structureFrequency;
        int separation = 16;
        int i = chunkX;
        int j = chunkZ;

        if (chunkX < 0) {
            chunkX -= spacing - 1;
        }

        if (chunkZ < 0) {
            chunkZ -= spacing - 1;
        }

        int k = chunkX / spacing;
        int l = chunkZ / spacing;
        Random random = world.setRandomSeed(k, l, 10388824);
        k = k * spacing;
        l = l * spacing;
        k = k + (random.nextInt(spacing - separation) + random.nextInt(spacing - separation)) / 2;
        l = l + (random.nextInt(spacing - separation) + random.nextInt(spacing - separation)) / 2;

        if (i == k && j == l) {
            BlockPos pos = new BlockPos((i << 4), 0, (j << 4));
            return isAbleToSpawnHere(pos, world);
        } else {

            return false;
        }
    }

    public static boolean isAbleToSpawnHere(BlockPos pos, World world) {
        for(BiomeDictionary.Type types : getSpawnBiomeTypes()) {
            Biome biomeCurrently = world.provider.getBiomeForCoords(pos);
            if(BiomeDictionary.hasType(biomeCurrently, types)) {
                return false;
            }
        }
        return true;
    }

    private static List<BiomeDictionary.Type> lamentedIslandsSpawnBiomes;

    public static List<BiomeDictionary.Type> getSpawnBiomeTypes() {
        if(lamentedIslandsSpawnBiomes == null) {
            lamentedIslandsSpawnBiomes = Lists.newArrayList();

            for(String str : WorldConfig.biome_types_lamented_islands) {
                try {
                    BiomeDictionary.Type type = BiomeDictionary.Type.getType(str);

                    if (type != null) lamentedIslandsSpawnBiomes.add(type);
                    else EELogger.logError("Biome Type" + str + " is not correct", new NullPointerException());
                } catch (Exception e) {
                    EELogger.logError(str + " is not a valid type name", e);
                }
            }
        }

        return lamentedIslandsSpawnBiomes;
    }

    public static List<String> getListOfStringsMatchingLastWord(String[] args, String... possibilities) {
        return getListOfStringsMatchingLastWord(args, Arrays.asList(possibilities));
    }

    public static List<String> getListOfStringsMatchingLastWord(String[] inputArgs, Collection<?> possibleCompletions) {
        String s = inputArgs[inputArgs.length - 1];
        List<String> list = Lists.newArrayList();

        if (!possibleCompletions.isEmpty()) {
            for (String s1 : Iterables.transform(possibleCompletions, Functions.toStringFunction())) {
                if (doesStringStartWith(s, s1)) {
                    list.add(s1);
                }
            }

            if (list.isEmpty()) {
                for (Object object : possibleCompletions) {
                    if (object instanceof ResourceLocation && doesStringStartWith(s, ((ResourceLocation) object).getPath())) {
                        list.add(String.valueOf(object));
                    }
                }
            }
        }

        return list;
    }

    public static boolean doesStringStartWith(String original, String region)
    {
        return region.regionMatches(true, 0, original, 0, original.length());
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
