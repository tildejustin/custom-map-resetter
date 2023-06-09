package xyz.tildejustin.custommapresetter.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.tildejustin.custommapresetter.CustomMapResetter;
import xyz.tildejustin.custommapresetter.SetWorldScreen;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
//    TODO: make it a wool block
    protected TitleScreenMixin(Text title) {
        super(title);
    }


    @Inject(method = "initWidgetsNormal", at = @At(value = "TAIL"))
    private void custommapresetter$addTitleScreenButton(CallbackInfo ci) {
        if (CustomMapResetter.running && !CustomMapResetter.loading) {
            CustomMapResetter.tryLoadNewWorld();
        }
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 124, this.height / 4 + 48, 20, 20, Text.of(""), (buttonWidget) -> {
            if (Screen.hasShiftDown()) {
                MinecraftClient.getInstance().setScreen(new SetWorldScreen(MinecraftClient.getInstance().currentScreen));
            } else {
                CustomMapResetter.tryLoadNewWorld();
            }
        }, ButtonWidget.DEFAULT_NARRATION_SUPPLIER));
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void custommapresetter$goldBootsOverlay(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
//        this.minecraft.getTextureManager().bindTexture(BUTTON_IMAGE);
//        (this.width / 2 - 124 + 2, this.height / 4 + 48 + 2, 0.0F, 0.0F, 16, 16, 16, 16);
        MinecraftClient.getInstance().getItemRenderer().renderGuiItemIcon(matrices, new ItemStack(Items.DIAMOND_BOOTS), this.width / 2 - 124 + 2, this.height / 4 + 48 + 2);
    }
}
