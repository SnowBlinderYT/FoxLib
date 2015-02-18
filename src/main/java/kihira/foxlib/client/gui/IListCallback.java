/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Zoe Lee (Kihira)
 *
 * See LICENSE for full License
 */

package kihira.foxlib.client.gui;

import net.minecraft.client.gui.GuiListExtended;

public interface IListCallback<T extends GuiListExtended.IGuiListEntry> {

    public boolean onEntrySelected(GuiList guiList, int index, T entry);
}
