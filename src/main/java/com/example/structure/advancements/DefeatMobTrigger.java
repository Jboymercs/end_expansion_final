package com.example.structure.advancements;

import java.util.List;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import com.google.common.collect.Lists;

import java.util.Map;
import java.util.Set;

public class DefeatMobTrigger implements ICriterionTrigger<DefeatMobTrigger.Instance> {
    /**
     * Defeat way of unlocking entries within the guide Book
     */

    private final Map<PlayerAdvancements, DefeatMobTrigger.Listeners> listeners = Maps.<PlayerAdvancements, Listeners>newHashMap();
    private final ResourceLocation id;

    public DefeatMobTrigger(ResourceLocation id) {
        this.id = id;
    }
    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public void addListener(PlayerAdvancements playerAdvancementsIn, Listener<DefeatMobTrigger.Instance> listener) {
        DefeatMobTrigger.Listeners killedtrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (killedtrigger$listeners == null)
        {
            killedtrigger$listeners = new DefeatMobTrigger.Listeners(playerAdvancementsIn);
            this.listeners.put(playerAdvancementsIn, killedtrigger$listeners);
        }

        killedtrigger$listeners.add(listener);
    }

    @Override
    public void removeListener(PlayerAdvancements playerAdvancementsIn, Listener<DefeatMobTrigger.Instance> listener) {
        DefeatMobTrigger.Listeners killedtrigger$listeners = this.listeners.get(playerAdvancementsIn);

        if (killedtrigger$listeners != null)
        {
            killedtrigger$listeners.remove(listener);

            if (killedtrigger$listeners.isEmpty())
            {
                this.listeners.remove(playerAdvancementsIn);
            }
        }
    }

    @Override
    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn) {
        this.listeners.remove(playerAdvancementsIn);
    }

    @Override
    public DefeatMobTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
        return null;
    }

    public void trigger(EntityPlayerMP player)
    {
        DefeatMobTrigger.Listeners killedtrigger$listeners = this.listeners.get(player.getAdvancements());

        if (killedtrigger$listeners != null)
        {
            killedtrigger$listeners.trigger(player);
        }
    }

    public static class Instance extends AbstractCriterionInstance {

        public Instance(ResourceLocation criterionIn) {
            super(criterionIn);
        }

        public boolean test(EntityPlayerMP player) {
            return true;
        }
    }

    static class Listeners {
        private final PlayerAdvancements playerAdvancements;
        private final Set<Listener<Instance>> listeners = Sets.<ICriterionTrigger.Listener<DefeatMobTrigger.Instance>>newHashSet();
        public Listeners(PlayerAdvancements playerAdvancementsIn)
        {
            this.playerAdvancements = playerAdvancementsIn;
        }

        public boolean isEmpty()
        {
            return this.listeners.isEmpty();
        }

        public void add(ICriterionTrigger.Listener<DefeatMobTrigger.Instance> listener)
        {
            this.listeners.add(listener);
        }

        public void remove(ICriterionTrigger.Listener<DefeatMobTrigger.Instance> listener)
        {
            this.listeners.remove(listener);
        }

        public void trigger(EntityPlayerMP player)
        {
            List<ICriterionTrigger.Listener<DefeatMobTrigger.Instance>> list = null;

            for (ICriterionTrigger.Listener<DefeatMobTrigger.Instance> listener : this.listeners)
            {
                if (((DefeatMobTrigger.Instance)listener.getCriterionInstance()).test(player))
                {
                    if (list == null)
                    {
                        list = Lists.<ICriterionTrigger.Listener<DefeatMobTrigger.Instance>>newArrayList();
                    }

                    list.add(listener);
                }
            }

            if (list != null)
            {
                for (ICriterionTrigger.Listener<DefeatMobTrigger.Instance> listener1 : list)
                {
                    listener1.grantCriterion(this.playerAdvancements);
                }
            }
        }

    }
}
