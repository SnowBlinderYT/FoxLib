/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014
 *
 * See LICENSE for full License
 */

package kihira.foxlib.client.toast;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.profiler.Profiler;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ToastManager {

    public static final ToastManager INSTANCE = new ToastManager();

    private final ArrayList<Toast> toasts = new ArrayList<Toast>();

    private ToastManager() {
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void createToast(int x, int y, String text) {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        int stringWidth = fontRenderer.getStringWidth(text);
        toasts.add(new Toast(x, y, stringWidth + 10,  stringWidth * 3, text));
    }

    @SuppressWarnings("unchecked")
    public void createCenteredToast(int x, int y, int maxWidth, String text) {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        int stringWidth = fontRenderer.getStringWidth(text);
        if (stringWidth > maxWidth) {
            List<String> strings = fontRenderer.listFormattedStringToWidth(text, maxWidth);
            toasts.add(new Toast(x - (maxWidth / 2) - 5, y, maxWidth + 10, maxWidth * 2, strings.toArray(new String[strings.size()])));
        }
        else {
            toasts.add(new Toast(x - (stringWidth / 2) - 5, y, stringWidth + 10, stringWidth * 4, text));
        }
    }

    @SubscribeEvent
    public void onClientTickPost(TickEvent.ClientTickEvent event) {
        Iterator<Toast> toasts = this.toasts.iterator();
        while (toasts.hasNext()) {
            Toast toast = toasts.next();
            toast.time--;
            if (toast.time <= 0) toasts.remove();
        }
    }

    @SubscribeEvent
    public void onDrawScreenPost(GuiScreenEvent.DrawScreenEvent.Post event) {
        Profiler profiler = Minecraft.getMinecraft().mcProfiler;
        profiler.startSection("toastNotification");
        for (Toast toast : toasts) {
            toast.drawToast(event.mouseX, event.mouseY);
        }
        profiler.endSection();
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type == RenderGameOverlayEvent.ElementType.HOTBAR) {
            Profiler profiler = Minecraft.getMinecraft().mcProfiler;
            profiler.startSection("toastNotification");
            for (Toast toast : toasts) {
                toast.drawToast(event.mouseX, event.mouseY);
            }
            profiler.endSection();
        }
    }
}
