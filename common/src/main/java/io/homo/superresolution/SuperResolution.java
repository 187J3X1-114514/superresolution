package io.homo.superresolution;

import com.mojang.blaze3d.systems.RenderSystem;
import io.homo.superresolution.config.Config;
import io.homo.superresolution.debug.imgui.ImguiMain;
import io.homo.superresolution.fsr2.FSR2;
import io.homo.superresolution.fsr2.nativelib.FSR2LibManager;
import io.homo.superresolution.impl.CanDestroy;
import io.homo.superresolution.impl.CanResize;
import io.homo.superresolution.resolutioncontrol.ResolutionControl;
import io.homo.superresolution.utils.FrameBuffer;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.minecraft.client.Minecraft.ON_OSX;

public final class SuperResolution implements CanResize, CanDestroy {
    public static final String MOD_ID = "super_resolution";
    public static ResolutionControl resolutioncontrol;
    private static SuperResolution instance;
    public static final Logger LOGGER = LoggerFactory.getLogger("SuperResolution");
    public static final Config config= new Config();
    public static FSR2 FSR;
    public static boolean isInit;
    private static final Minecraft minecraft = Minecraft.getInstance();
    public static boolean gameIsLoad = false;
    public static float frameTimeDelta = 16.6f;
    public static float frameTimeDelta_fsr = 16.6f;
    public static final FrameBuffer mainTarget = new FrameBuffer(true);
    public SuperResolution(){}
    public static void initFSR2Lib(){
        if (FSR2LibManager.exists(minecraft.gameDirectory.getAbsolutePath())){
            LOGGER.info("FSR2库存在无需提取");
        }else {
            LOGGER.info("FSR2库不存在，正在提取");
            FSR2LibManager.extract(minecraft.gameDirectory.getAbsolutePath());
        }
        FSR2LibManager.load();
    }
    public void init() {
        RenderSystem.assertOnRenderThread();
        instance = this;
        resolutioncontrol = new ResolutionControl(minecraft);
        resolutioncontrol.setClientFramebuffer(mainTarget);
        mainTarget.resize(SuperResolution.getMinecraftWidth(),SuperResolution.getMinecraftHeight(),ON_OSX);
        Minecraft.getInstance().getMainRenderTarget().setClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        Minecraft.getInstance().getMainRenderTarget().clear(ON_OSX);
        resolutioncontrol.init();
        FSR = new FSR2();
        LOGGER.info("成功加载FSR2库");
        new ImguiMain();
        LOGGER.info("imgui初始化完成");
        LOGGER.info("初始化完成");
        isInit = true;
        this.resize(SuperResolution.getMinecraftWidth(),SuperResolution.getMinecraftHeight());
    }
    public static SuperResolution getInstance() {
        return instance;
    }
    public void resize(int width,int height){
        RenderSystem.assertOnRenderThread();
        FSR.resize(width, height);
    }

    public static int getMinecraftWidth(){
        return minecraft.getWindow().getWidth();
    }
    public static int getMinecraftHeight(){
        return minecraft.getWindow().getHeight();
    }
    public void destroy(){
        RenderSystem.assertOnRenderThread();
        FSR.destroy();
    }
    public static void setFrameTimeDelta(float value){

        frameTimeDelta = value;
    }
    public static void setFrameTimeDeltaFSR(float value){
        frameTimeDelta_fsr = value;
    }
}

