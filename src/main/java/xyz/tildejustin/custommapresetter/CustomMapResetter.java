package xyz.tildejustin.custommapresetter;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LoadingScreenRenderer;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class CustomMapResetter implements ClientModInitializer {
    private static final File attemptCountFile = FabricLoader.getInstance().getConfigDir().resolve("custom-map-attempts.txt").toFile();
    public static ResetTracker resetTracker;
    public static boolean running = false;
    public static boolean loading = false;
    public static boolean loadedTextures = false;
    public static boolean autoreset = true;

    public static void tryLoadNewWorld() {
        CustomMapResetter.loading = true;
        if (CustomMapResetter.resetTracker.getCurrentWorld() == null) {
            MinecraftClient.getInstance().setScreen(new SetWorldScreen(MinecraftClient.getInstance().currentScreen));
            return;
        }
        File saveFile = MinecraftClient.getInstance().runDirectory.toPath().resolve("saves").resolve(CustomMapResetter.resetTracker.getCurrentWorld()).toFile();
        File newSave = saveFile.getParentFile().toPath().resolve(CustomMapResetter.resetTracker.getCurrentWorld() + " attempt " + CustomMapResetter.resetTracker.incrementAttemptCount(CustomMapResetter.resetTracker.getCurrentWorld())).toFile();
        if (!saveFile.exists()) {
            MinecraftClient.getInstance().setScreen(new SetWorldScreen(MinecraftClient.getInstance().currentScreen));
            return;
        }
        System.out.println("loading: " + saveFile);
        CustomMapResetter.running = true;
        LoadingScreenRenderer loadingScreen = new LoadingScreenRenderer(MinecraftClient.getInstance());
        loadingScreen.setTitle("Copying World");
        loadingScreen.setTask("");
        while (newSave.exists()) {
            newSave = new File(newSave + "-");
        }
        try {
            FileUtils.copyDirectory(saveFile, newSave);
        } catch (IOException e) {
            e.printStackTrace();
        }
        resetTracker.addWorld(newSave);
        MinecraftClient.getInstance().startIntegratedServer(newSave.getName(), newSave.getName(), null);
    }

    @Override
    public void onInitializeClient() {
        CustomMapResetter.resetTracker = new ResetTracker(attemptCountFile);
    }
}
