package net.altias.dragon_voice;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.config.ModConfigEvent;

@EventBusSubscriber(modid = Dragon_voice.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ConfigEvents {

    @SubscribeEvent
    public static void onConfigLoad(ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == Config.SPEC) {
            Config.reload();
        }
    }

    @SubscribeEvent
    public static void onConfigReload(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == Config.SPEC) {
            Config.reload();
        }
    }
}
