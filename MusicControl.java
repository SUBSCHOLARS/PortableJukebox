package com.example.examplemod.portablejukebox;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;

public class MusicControl {
    public static void stopBGM(){
        Minecraft minecraft=Minecraft.getInstance();
        minecraft.getSoundManager().stop(null, SoundSource.MUSIC);
    }
}
