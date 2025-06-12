package com.example.examplemod.portablejukebox;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber
public class PortableJukeBoxKeyHandler {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event){
        Minecraft minecraft=Minecraft.getInstance();
        Player player= minecraft.player;
        if(player!=null&&player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof PortableJukeBox portableJukeBox){
            if(event.getKey()== GLFW.GLFW_KEY_N&&event.getAction()==GLFW.GLFW_PRESS){
                portableJukeBox.nextTrack();
            }else if(event.getKey()==GLFW.GLFW_KEY_B&&event.getAction()==GLFW.GLFW_PRESS){
                portableJukeBox.previousTrack();
            }
        }
    }
}
