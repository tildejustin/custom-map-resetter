
package xyz.tildejustin.custommapresetter.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.tildejustin.custommapresetter.CustomMapResetter;

import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Inject(method = {"reloadResources(Z)Ljava/util/concurrent/CompletableFuture;", "reloadResourcesConcurrently"}, at = @At(value = "HEAD"), cancellable = true)
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