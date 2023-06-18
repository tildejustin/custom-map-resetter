package xyz.tildejustin.custommapresetter.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.tildejustin.custommapresetter.CustomMapResetter;
import xyz.tildejustin.custommapresetter.SetWorldScreen;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
//    TODO: make it a wool block

    @Inject(method = "init", at = @At(value = "HEAD"))
    private void custommapresetter$loadnext(CallbackInfo ci) {
        if (!CustomMapResetter.autoreset) {
            CustomMapResetter.running = false;
        }
        if (CustomMapResetter.running && !CustomMapResetter.loading) {
            CustomMapResetter.tryLoadNewWorld();
        }
    }

    @Inject(method = "init", at = @At(value = "TAIL"))
    private void custommapresetter$addTitleScreenButton(CallbackInfo ci) {
        this.buttons.add(new ButtonWidget(13, this.width / 2 - 124, this.height / 4 + 48, 20, 20, ""));
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void custommapresetter$goldBootsOverlay(int mouseX, int mouseY, float delta, CallbackInfo ci) {
        // wow.
        if (!FabricLoader.getInstance().getModContainer("minecraft").get().getMetadata().getVersion().getFriendlyString().contains("1.5")) {
            ((TextureManagerMixin) Minecraft.getMinecraft().textureManager).callBindTexture(((TextureManagerMixin) Minecraft.getMinecraft().textureManager).callGetTextureFromPath("/gui/items.png"));
            drawTexture(this.width / 2 - 124 + 2, this.height / 4 + 48 + 2, 3 * 16, 3 * 16, 16, 16);
        }
    }

    @Inject(method = "buttonClicked", at = @At("HEAD"), cancellable = true)
    public void buttonClicked(ButtonWidget button, CallbackInfo ci) {
        if (button.id == 13) {
            if (Screen.hasShiftDown()) {
                this.field_1229.openScreen(new SetWorldScreen(this.field_1229.currentScreen));
            } else {
                CustomMapResetter.tryLoadNewWorld();
            }
            ci.cancel();
        }

    }
}
