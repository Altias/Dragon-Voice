package net.altias.dragon_voice.plugin;

import by.dragonsurvivalteam.dragonsurvival.DragonSurvival;
import by.dragonsurvivalteam.dragonsurvival.client.render.ClientDragonRenderer;
import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateHandler;
import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider;
import by.dragonsurvivalteam.dragonsurvival.common.entity.DragonEntity;
import by.dragonsurvivalteam.dragonsurvival.registry.dragon.body.emotes.DragonEmote;
import by.dragonsurvivalteam.dragonsurvival.registry.dragon.body.emotes.DragonEmoteSet;
import by.dragonsurvivalteam.dragonsurvival.registry.dragon.body.emotes.DragonEmoteSets;
import by.dragonsurvivalteam.dragonsurvival.util.DragonAnimations;
import de.maxhenkel.voicechat.api.ForgeVoicechatPlugin;
import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatConnection;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import net.altias.dragon_voice.VoiceState;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

@ForgeVoicechatPlugin
public class DragonVoicePlugin implements VoicechatPlugin {
    @Override
    public String getPluginId() {
        return "dragon_voice";
    }

    @Override
    public void initialize(VoicechatApi api) {

    }

    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(MicrophonePacketEvent.class, this::onMicPacket);
    }

    private void onMicPacket(MicrophonePacketEvent event)
    {
        VoicechatConnection senderConnection = event.getSenderConnection();
        if (senderConnection == null) {
            return;
        }

        if (event.getPacket().getOpusEncodedData().length <= 0) {
            // Don't trigger any events when stopping to talk
            return;
        }

        if (senderConnection.isInGroup()) {
            return;
        }

        if (!(senderConnection.getPlayer().getPlayer() instanceof ServerPlayer player))
        {
            return;
        }

        DragonStateHandler handler = DragonStateProvider.getData(player);

        if (!handler.isDragon())
        {
            return;
        }

        VoiceState.LAST_SPEAK.put(player.getUUID(), System.currentTimeMillis());

        if (!VoiceState.SPEAKING.add(player.getUUID())) {
            return;
        }

        DragonEntity dragon = ClientDragonRenderer.getDragon(player);
        Registry<DragonEmoteSet> registry = player.registryAccess().registryOrThrow(DragonEmoteSet.REGISTRY);


        Holder.Reference<DragonEmoteSet> holder =
                registry.getHolderOrThrow(DragonEmoteSets.DEFAULT_EMOTES);
        DragonEmoteSet set = holder.value();
        DragonEmote talk = set.getEmote(DragonEmoteSets.BLEND_TALK);

        if (!dragon.isPlayingEmote(talk)) {
            dragon.beginPlayingEmote(talk);
        }

    }
}
