package xyz.tildejustin.custommapresetter.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.tildejustin.custommapresetter.CustomMapResetter;

@Mixin(OptionsScreen.class)
public abstract class SettingsScreenMixin extends Screen {
    protected SettingsScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    public void custommapresetter$addStopButton(CallbackInfo ci) {
        if (CustomMapResetter.running && CustomMapResetter.autoreset) {
            this.addButton(new ButtonWidget(5, this.height - 25, 100, 20, new LiteralText("Stop Resets & Quit"), button -> {
                CustomMapResetter.running = false;
                CustomMapResetter.loadedTextures = false;
                MinecraftClient.getInstance().world.disconnect();
                MinecraftClient.getInstance().disconnect();
                MinecraftClient.getInstance().openScreen(new TitleScreen());
            }));
        }
    }
}
