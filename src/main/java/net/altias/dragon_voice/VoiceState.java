package net.altias.dragon_voice;

import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateHandler;
import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider;
import by.dragonsurvivalteam.dragonsurvival.common.codecs.UnlockableBehavior;
import by.dragonsurvivalteam.dragonsurvival.registry.dragon.body.DragonBody;
import by.dragonsurvivalteam.dragonsurvival.registry.dragon.body.emotes.DragonEmote;
import by.dragonsurvivalteam.dragonsurvival.registry.dragon.body.emotes.DragonEmoteSet;
import by.dragonsurvivalteam.dragonsurvival.registry.dragon.body.emotes.DragonEmoteSets;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class VoiceState {
    public static final Map<UUID, Long> LAST_SPEAK = new ConcurrentHashMap<>();
    public static final Set<UUID> SPEAKING = ConcurrentHashMap.newKeySet();

    public static final long TIMEOUT = 250; // ms

    public static DragonEmote getTalkEmote(ServerPlayer player)
    {
        Registry<DragonEmoteSet> registry =
                player.registryAccess().registryOrThrow(DragonEmoteSet.REGISTRY);

        DragonStateHandler s = DragonStateProvider.getData(player);
        String emoteId = DragonEmoteSets.BLEND_TALK;

        ResourceLocation playerSpecies = s.speciesId();
        String animation = Config.SPECIES_ANIMATION_MAP.get(playerSpecies);
        System.out.println(playerSpecies);
        System.out.println("ANI: " + animation);
        if (animation != null)
        {
            emoteId = animation.toString();
        }

        DragonEmote talk = registry
                .getHolderOrThrow(s.body().value().emotes().getKey())
                .value()
                .getEmote(emoteId);
        System.out.println(s.body().value().emotes().getKey());
        System.out.println(talk);

        return talk;
    }
}
