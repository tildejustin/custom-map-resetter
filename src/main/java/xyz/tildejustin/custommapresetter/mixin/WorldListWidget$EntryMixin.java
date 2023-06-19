package xyz.tildejustin.custommapresetter.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.world.level.storage.LevelSummary;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.tildejustin.custommapresetter.CustomMapResetter;
import xyz.tildejustin.custommapresetter.SetWorldScreen;

@Mixin(WorldListWidget.Entry.class)
public abstract class WorldListWidget$EntryMixin {
    @Shadow
    @Final
    public LevelSummary level;
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "play", at = @At(value = "HEAD"), cancellable = true)
    private void custommapresetter$selectworldonplay(CallbackInfo ci) {
        if (MinecraftClient.getInstance().currentScreen instanceof SetWorldScreen) {
            CustomMapResetter.resetTracker.setCurrentWorld(this.level.getName());
            CustomMapResetter.resetTracker.writeResetCountFile(CustomMapResetter.resetTracker.resetCount, CustomMapResetter.resetTracker.resetCountFile);
            this.client.setScreen(((SetWorldScreen) this.client.currentScreen).parent);
            ci.cancel();
        }
    }
}