package xyz.tildejustin.custommapresetter.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SettingsScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.tildejustin.custommapresetter.CustomMapResetter;

@Mixin(SettingsScreen.class)
public abstract class SettingsScreenMixin extends Screen {
    @Inject(method = "init", at = @At("TAIL"))
    public void custommapresetter$addStopButton(CallbackInfo ci) {
        if (CustomMapResetter.running) {
            this.buttons.add(new ButtonWidget(1238, 5, this.height - 25, 100, 20, "Stop Resets & Quit") {
                @Override
                public void method_18374(double d, double e) {
                    CustomMapResetter.running = false;
                    MinecraftClient.getInstance().world.disconnect();
                    MinecraftClient.getInstance().connect(null);
                    MinecraftClient.getInstance().setScreen(new TitleScreen());
                }
            });
        }
    }
}
