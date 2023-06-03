package xyz.tildejustin.custommapresetter.mixin;

import net.minecraft.server.PlayerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.tildejustin.custommapresetter.CustomMapResetter;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {
    @Inject(method = "method_12827", at = @At("TAIL"))
    private void custommapresetter$preventLoop(CallbackInfo ci) {
        CustomMapResetter.loading = false;
    }
}
