package com.example.examplemod.portablejukebox;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import static com.example.examplemod.ExampleMod.MODID;

public class PortableJukeBox extends Item{
    public static final SoundEvent[] SOUND_ARRAY_FOR_PORTABLE_JUKEBOX={
            new SoundEvent(new ResourceLocation(MODID,"ocean")),
            new SoundEvent(new ResourceLocation(MODID,"lost")),
            new SoundEvent(new ResourceLocation(MODID,"sea")),
            new SoundEvent(new ResourceLocation(MODID,"victory"))};
    private int currentSoundIndex=0;
    private SoundInstance currentSound;
    public PortableJukeBox(){
        super((new Item.Properties().tab(CreativeModeTab.TAB_MISC)).stacksTo(1));
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand){
        BlockPos pos=player.blockPosition();
        ItemStack stack=player.getItemInHand(hand);

        if(!player.level.isClientSide){
            if(player.getItemBySlot(EquipmentSlot.HEAD).isEmpty()){
                player.setItemSlot(EquipmentSlot.HEAD,stack.copy());
                stack.shrink(1);
                level.playSound(null,pos, SoundEvents.ARMOR_EQUIP_ELYTRA, SoundSource.PLAYERS,1.0f,1.0f);
                return super.use(level,player,hand);
            }
        }
        return InteractionResultHolder.fail(stack);
    }
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean isSelected){
        if(entity instanceof Player player){
            if(player.getItemBySlot(EquipmentSlot.HEAD)==stack&&!player.level.isClientSide){
                if(currentSound==null) {
                    MusicControl.stopBGM();
                    playSound(SOUND_ARRAY_FOR_PORTABLE_JUKEBOX[currentSoundIndex]);
                }
            }
            else if(player.getItemBySlot(EquipmentSlot.HEAD)==stack){
                level.addParticle(ParticleTypes.NOTE,
                        player.blockPosition().getX()+Math.random(),
                        player.blockPosition().getY()+Math.random()+1.5D,
                        player.blockPosition().getZ()+Math.random(),
                        2*Math.random(),
                        2*Math.random(),
                        2*Math.random());
            }
            else if(player.getItemBySlot(EquipmentSlot.HEAD)!=stack){
                stopMusic();
            }
        }
    }
    public void nextTrack(){
        if(currentSoundIndex>Integer.MAX_VALUE){
            currentSoundIndex=0;
        }
        stopMusic();
        currentSoundIndex=(currentSoundIndex+1)%SOUND_ARRAY_FOR_PORTABLE_JUKEBOX.length;
        playSound(SOUND_ARRAY_FOR_PORTABLE_JUKEBOX[currentSoundIndex]);
    }
    public void previousTrack(){
        stopMusic();
        currentSoundIndex=(currentSoundIndex-1+SOUND_ARRAY_FOR_PORTABLE_JUKEBOX.length)%SOUND_ARRAY_FOR_PORTABLE_JUKEBOX.length;
        playSound(SOUND_ARRAY_FOR_PORTABLE_JUKEBOX[currentSoundIndex]);
    }
    @Override
    public EquipmentSlot getEquipmentSlot(ItemStack stack){
        return EquipmentSlot.HEAD;
    }
    private void playSound(SoundEvent soundEvent){
        Minecraft minecraft=Minecraft.getInstance();
        currentSound=SimpleSoundInstance.forMusic(soundEvent);
        minecraft.getSoundManager().play(currentSound);
    }
    private void stopMusic(){
        Minecraft minecraft=Minecraft.getInstance();
        minecraft.getSoundManager().stop(currentSound);
        currentSound=null;
    }
}
