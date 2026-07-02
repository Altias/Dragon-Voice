package net.altias.dragon_voice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // a list of strings that are treated as resource locations for items
    public static final ModConfigSpec.ConfigValue<List<? extends String>> SPECIES_ANIMATIONS =
            BUILDER.comment(
                    "Sets a specific animation key for a species to use. Format as species=animation. The key must be valid or the animation will not play."
                    )
                    .defineListAllowEmpty(
                    "speciesAnimations",
                    List.of(
                            "dragonsurvival:bee_queen=bee_blend_yes"
                    ),
                    o -> o instanceof String
            );

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static final Map<ResourceLocation, String> SPECIES_ANIMATION_MAP = new HashMap<>();

    public static void reload() {
        SPECIES_ANIMATION_MAP.clear();

        for (String entry : SPECIES_ANIMATIONS.get()) {
            String[] split = entry.split("=", 2);
            if (split.length != 2) continue;

            ResourceLocation species = ResourceLocation.parse(split[0].trim());
            String animation = split[1].trim();

            SPECIES_ANIMATION_MAP.put(species, animation);
        }
    }

}