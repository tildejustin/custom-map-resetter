package xyz.tildejustin.custommapresetter.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.tildejustin.custommapresetter.CustomMapResetter;

import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Inject(method = {"reloadResources(Z)Ljava/util/concurrent/CompletableFuture;", "reloadResourcesConcurrently"}, at = @At(value = "HEAD"), cancellable = true)
    private void custommapresetter$stopResourceReload(CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        if (CustomMapResetter.running) {
            if (CustomMapResetter.loadedTextures) {
                cir.setReturnValue(new CompletableFuture<>());
            }
            CustomMapResetter.loadedTextures = true;
        }
    }
} 