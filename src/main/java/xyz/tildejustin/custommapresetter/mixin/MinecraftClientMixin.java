package xyz.tildejustin.custommapresetter.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.BackupPromptScreen;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.world.EditWorldScreen;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.level.storage.LevelStorage;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.tildejustin.custommapresetter.CustomMapResetter;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow public abstract void openScreen(@Nullable Screen screen);

    @Shadow @Final private static Logger LOGGER;

    @Shadow @Final private LevelStorage levelStorage;

    @Inject(method = "reloadResources", at = @At(value = "HEAD"), cancellable = true)
    private void custommapresetter$stopResourceReload(CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        if (CustomMapResetter.running) {
            if (CustomMapResetter.loadedTextures) {
                cir.setReturnValue(null);
            }
            CustomMapResetter.loadedTextures = true;
        }
    }

    @ModifyArg(method = "startIntegratedServer(Ljava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;startIntegratedServer(Ljava/lang/String;Lnet/minecraft/util/registry/DynamicRegistryManager$Impl;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function4;ZLnet/minecraft/client/MinecraftClient$WorldLoadAction;)V"), index = 5)
    private MinecraftClient.WorldLoadAction custommapresetter$iknowwhatimdoing(MinecraftClient.WorldLoadAction worldLoadAction) {
        return MinecraftClient.WorldLoadAction.NONE;
    }
}