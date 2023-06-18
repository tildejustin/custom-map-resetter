package xyz.tildejustin.custommapresetter;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LoadingScreenRenderer;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;


public class CustomMapResetter implements ClientModInitializer {
    private static final File attemptCountFile = FabricLoader.getInstance().getConfigDir().resolve("custom-map-attempts.txt").toFile();
    public static ResetTracker resetTracker;
    public static boolean running = false;
    public static boolean loading = false;
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
            Path finalNewSave = newSave.toPath();
            Files.walkFileTree(saveFile.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                        throws IOException {
                    Path targetdir = finalNewSave.resolve(saveFile.toPath().relativize(dir));
                    try {
                        Files.copy(dir, targetdir);
                    } catch (FileAlreadyExistsException e) {
                        if (!Files.isDirectory(targetdir))
                            throw e;
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                        throws IOException {
                    Files.copy(file, finalNewSave.resolve(saveFile.toPath().relativize(file)));
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        resetTracker.addWorld(newSave);
//		method_2935 = startIntegratedServer
        MinecraftClient.getInstance().method_2935(newSave.getName(), newSave.getName(), null);
    }

    @Override
    public void onInitializeClient() {
        CustomMapResetter.resetTracker = new ResetTracker(attemptCountFile);
    }
}
