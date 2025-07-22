package xyz.tildejustin.custommapresetter.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.tildejustin.custommapresetter.CustomMapResetter;

import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Dynamic
    @Inject(method = {"reloadResources(Z)Ljava/util/concurrent/CompletableFuture;", "method_36561(ZLnet/minecraft/class_310$class_8764;)Ljava/util/concurrent/CompletableFuture;"}, at = @At(value = "HEAD"), cancellable = true)
    private void custommapresetter$stopResourceReload(CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        if (CustomMapResetter.running) {
            if (CustomMapResetter.loadedTextures) {
                cir.setReturnValue(CompletableFuture.completedFuture(null));
                return;
            }
            CustomMapResetter.loadedTextures = true;
        }
    }
} 