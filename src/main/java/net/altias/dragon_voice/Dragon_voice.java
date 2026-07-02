package net.altias.dragon_voice;

import by.dragonsurvivalteam.dragonsurvival.client.render.ClientDragonRenderer;
import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateHandler;
import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider;
import by.dragonsurvivalteam.dragonsurvival.common.entity.DragonEntity;
import by.dragonsurvivalteam.dragonsurvival.registry.dragon.body.emotes.DragonEmote;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.slf4j.Logger;
import java.util.Map;
import java.util.HashMap;
import net.altias.dragon_voice.Config;

import java.util.Iterator;
import java.util.UUID;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Dragon_voice.MODID)
public class Dragon_voice {
    public static final String MODID = "dragon_voice";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Dragon_voice(IEventBus modEventBus, ModContainer modContainer) {

        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }

    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        long now = System.currentTimeMillis();

        Iterator<UUID> iterator = VoiceState.SPEAKING.iterator();

        while (iterator.hasNext()) {
            UUID uuid = iterator.next();

            Long lastSpeak = VoiceState.LAST_SPEAK.get(uuid);
            if (lastSpeak == null || now - lastSpeak > VoiceState.TIMEOUT) {

                ServerPlayer player = event.getServer().getPlayerList().getPlayer(uuid);

                if (player != null && DragonStateProvider.isDragon(player)) {
                    DragonEntity dragon = ClientDragonRenderer.getDragon(player);

                    DragonEmote talk = VoiceState.getTalkEmote(player);

                    if (dragon.isPlayingEmote(talk)) {
                        int slot = getPlayingEmoteSlot(talk,dragon.getCurrentlyPlayingEmotes());
                        dragon.stopEmote(slot);
                    }
                }

                iterator.remove();
                VoiceState.LAST_SPEAK.remove(uuid);
            }
        }
    }

    public int getPlayingEmoteSlot(DragonEmote emote, DragonEmote[] currentlyPlaying) {
        for (int i = 0; i < currentlyPlaying.length; i++) {
            if (currentlyPlaying[i] == emote) {
                return i;
            }
        }
        return -1;
    }
}
