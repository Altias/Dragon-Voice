package net.altias.dragon_voice;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class VoiceState {
    public static final Map<UUID, Long> LAST_SPEAK = new ConcurrentHashMap<>();
    public static final Set<UUID> SPEAKING = ConcurrentHashMap.newKeySet();

    public static final long TIMEOUT = 250; // ms
}
