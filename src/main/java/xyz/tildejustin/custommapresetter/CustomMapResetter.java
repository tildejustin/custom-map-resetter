package xyz.tildejustin.custommapresetter;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ProgressScreen;
import net.minecraft.text.LiteralText;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class CustomMapResetter implements ModInitializer {
	public static ResetTracker resetTracker;
	private static final File attemptCountFile = FabricLoader.getInstance().getConfigDir().resolve("custom-map-attempts.txt").toFile();
	public static boolean running = false;
	public static boolean loading = false;

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
		ProgressScreen loadingScreen = new ProgressScreen();
		loadingScreen.method_21526(new LiteralText("Copying World"));
		MinecraftClient.getInstance().setScreen(loadingScreen);
		while (newSave.exists()) {
			newSave = new File(newSave.toString() + "-");
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
	public void onInitialize() {
		CustomMapResetter.resetTracker = new ResetTracker(attemptCountFile);
	}
}
