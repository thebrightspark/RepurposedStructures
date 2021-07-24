package com.telepathicgrunt.repurposedstructures.biomeinjection;

import com.telepathicgrunt.repurposedstructures.RepurposedStructures;
import com.telepathicgrunt.repurposedstructures.modinit.RSConfiguredStructures;
import com.telepathicgrunt.repurposedstructures.modinit.RSStructures;
import com.telepathicgrunt.repurposedstructures.utils.BiomeSelection;
import net.minecraft.world.biome.Biome.Category;
import net.minecraftforge.event.world.BiomeLoadingEvent;

public class Strongholds {

    public static void addStrongholds(BiomeLoadingEvent event) {

        if (RepurposedStructures.RSStrongholdsConfig.netherStrongholdMaxChunkDistance.get() != 1001 &&
            BiomeSelection.isBiomeAllowed(event, RSStructures.STRONGHOLD_NETHER.get(),
                    () -> BiomeSelection.haveCategories(event, Category.NETHER)))
        {
            event.getGeneration().getStructures().add(() -> RSConfiguredStructures.NETHER_STRONGHOLD);
        }
    }
}
