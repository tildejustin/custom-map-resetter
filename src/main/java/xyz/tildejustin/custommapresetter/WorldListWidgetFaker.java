package xyz.tildejustin.custommapresetter;

import net.minecraft.client.render.Tessellator;

public interface WorldListWidgetFaker {
    abstract void renderEntry(int index, int x, int y, int rowHeight, int mouseX, int mouseY);

    abstract void method_1055(int index, int x, int y, int rowHeight, int mouseX, int mouseY, float f);
    abstract void method_1055(int i, int j, int k, int l, Tessellator t, int m, int n);
}
