package io.homo.superresolution.forge;

import io.homo.superresolution.SuperResolution;
import dev.architectury.platform.forge.EventBuses;
import io.homo.superresolution.resolutioncontrol.mixin.MinecraftAccessor;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SuperResolution.MOD_ID)
public final class SuperResolutionForge {
    private SuperResolution common;
    public SuperResolutionForge() {
        EventBuses.registerModEventBus(SuperResolution.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        this.common = new SuperResolution();
        this.common.init();
    }
}
