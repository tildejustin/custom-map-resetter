package xyz.tildejustin.custommapresetter.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.tildejustin.custommapresetter.CustomMapResetter;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Inject(method = "reloadResources", at = @At(value = "HEAD"), cancellable = true)
    private void custommapresetter$stopResourceReload(CallbackInfo ci) {
        if (CustomMapResetter.running) {
            if (CustomMapResetter.loadedTextures) {
                ci.cancel();
            }
            CustomMapResetter.loadedTextures = true;
        }
    }
}
