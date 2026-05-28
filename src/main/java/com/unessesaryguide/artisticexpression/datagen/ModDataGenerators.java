package com.unessesaryguide.artisticexpression.datagen;

import com.unessesaryguide.artisticexpression.ArtisticExpression;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = ArtisticExpression.MODID)
public class ModDataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(event.includeClient(),
            new ModLanguageProvider(output));

        generator.addProvider(event.includeClient(),
            new ModItemModelProvider(output, existingFileHelper));

        generator.addProvider(event.includeServer(),
            new ModRecipeProvider(output, event.getLookupProvider()));

        generator.addProvider(event.includeServer(),
            new ModItemTagProvider(output, event.getLookupProvider(),
                CompletableFuture.completedFuture(null)));

        generator.addProvider(event.includeClient(),
            new ModBlockStateProvider(output, existingFileHelper));
    }
}
