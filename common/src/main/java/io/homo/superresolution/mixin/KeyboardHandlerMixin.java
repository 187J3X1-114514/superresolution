package io.homo.superresolution.mixin;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import io.homo.superresolution.SuperResolution;
import io.homo.superresolution.fsr2.FSR2;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.function.Consumer;

import static org.apache.commons.io.FileUtils.getFile;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {

    @Inject(at= @At(value = "INVOKE", target ="Lnet/minecraft/client/Screenshot;grab(Ljava/io/File;Lcom/mojang/blaze3d/pipeline/RenderTarget;Ljava/util/function/Consumer;)V"),method = "keyPress")
    private void debugScreenshot(CallbackInfo ci){
        Screenshot.grab(Minecraft.getInstance().gameDirectory,"mv.png", SuperResolution.FSR.getHelper().getMotionVectorsBuffer(),(a)->{
            SuperResolution.LOGGER.info("mv buffer: {}",a.getString());
        });
        Screenshot.grab(Minecraft.getInstance().gameDirectory,"fsr2OutTexture.png", FSR2.fsr2OutTexture,(a)->{
            SuperResolution.LOGGER.info("fsr2OutTexture buffer: {}",a.getString());
        });
        Screenshot.grab(Minecraft.getInstance().gameDirectory,"world.png", FSR2.worldFramebuffer,(a)->{
            SuperResolution.LOGGER.info("world buffer: {}",a.getString());
        });
        grab(Minecraft.getInstance().gameDirectory,"world_d.png",(a)->{
            SuperResolution.LOGGER.info("world_d buffer: {}",a.getString());
        }, FSR2.worldFramebuffer.getDepthTextureId(), FSR2.worldFramebuffer.width, FSR2.worldFramebuffer.height);
    }

    private static NativeImage super_resolution$takeScreenshot(int id, int i, int j) {
        NativeImage nativeImage = new NativeImage(i, j, false);
        RenderSystem.bindTexture(id);
        nativeImage.downloadTexture(0, true);
        nativeImage.flipY();
        return nativeImage;
    }

    private static void grab(File gameDirectory, @Nullable String screenshotName, Consumer<Component> messageConsumer,int id,int i,int j) {
        NativeImage nativeImage = super_resolution$takeScreenshot(id,i,j);
        File file = new File(gameDirectory, "screenshots");
        file.mkdir();
        File file2;
        if (screenshotName == null) {
            file2 = getFile(file);
        } else {
            file2 = new File(file, screenshotName);
        }

        Util.ioPool().execute(() -> {
            try {
                nativeImage.writeToFile(file2);
                Component component = Component.literal(file2.getName()).withStyle(ChatFormatting.UNDERLINE).withStyle((style) -> {
                    return style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file2.getAbsolutePath()));
                });
                messageConsumer.accept(Component.translatable("screenshot.success", component));
            } catch (Exception var7) {
                messageConsumer.accept(Component.translatable("screenshot.failure", var7.getMessage()));
            } finally {
                nativeImage.close();
            }

        });
    }
}
