package com.unessesaryguide.artisticexpression.loot;

import com.google.common.base.Suppliers;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unessesaryguide.artisticexpression.item.GeneralItems;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.function.Supplier;

public class SheepFleeceLootModifier extends LootModifier {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final Supplier<MapCodec<SheepFleeceLootModifier>> CODEC =
        Suppliers.memoize(() -> RecordCodecBuilder.mapCodec(inst ->
            LootModifier.codecStart(inst).apply(inst, SheepFleeceLootModifier::new)));

    public SheepFleeceLootModifier(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(
        ObjectArrayList<ItemStack> generatedLoot,
        LootContext context) {

        var entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (!(entity instanceof Sheep sheep)) return generatedLoot;

        DyeColor color = sheep.getColor();
        var fleeceItem = GeneralItems.SHAVED_FLEECE.get(color);
        if (fleeceItem == null) return generatedLoot;

        generatedLoot.removeIf(stack -> stack.is(ItemTags.WOOL));

        if (!sheep.isSheared()) {
            generatedLoot.add(new ItemStack(fleeceItem.get(), 4));
        }

        return generatedLoot;
    }

    // ADD THIS — was missing from your file
    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
