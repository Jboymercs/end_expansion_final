package com.example.structure.advancements;

import com.example.structure.util.ModReference;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Method;

public class EEAdvancements {

    public static DefeatMobTrigger DEFEAT_LAMENTOR;

    @SuppressWarnings("unchecked")
    private static <T extends ICriterionTrigger<?>> T register(T criterion)
    {
        Method method = ReflectionHelper.findMethod(CriteriaTriggers.class, "register", "func_192118_a", ICriterionTrigger.class);
        method.setAccessible(true);

        try
        {
            criterion = (T) method.invoke(null, criterion);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return criterion;
    }

    public static void Initialization() {
        DEFEAT_LAMENTOR = register(new DefeatMobTrigger(new ResourceLocation(ModReference.MOD_ID, "defeat_lamentor")));
    }

}
