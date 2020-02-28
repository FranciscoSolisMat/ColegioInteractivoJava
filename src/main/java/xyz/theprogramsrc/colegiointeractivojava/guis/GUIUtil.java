/**********************************************************************************************************************
 * Copyright (c) 2020.                                                                                                *
 * This project was created by FranciscoSolis                                                                         *
 **********************************************************************************************************************/

package xyz.theprogramsrc.colegiointeractivojava.guis;

import java.awt.*;
import java.awt.event.ActionListener;

public class GUIUtil {

    public static MenuBar getMenuBar(Menu... menus){
        MenuBar bar = new MenuBar();
        for (Menu m : menus) {
            bar.add(m);
        }

        return bar;
    }

    public static Menu createMenu(String name, ActionListener listener, MenuItem... items){
        Menu menu = new Menu(name);
        for (MenuItem item : items) {
            menu.add(item);
        }

        menu.addActionListener(listener);;
        return menu;
    }

    public static MenuItem createItem(String display, String cmd, int key, boolean shift){
        MenuItem item = new MenuItem();
        item.setLabel(display);
        item.setShortcut(new MenuShortcut(key, shift));
        item.setActionCommand(cmd);
        return item;
    }

    public static MenuItem createItem(String display, String cmd){
        MenuItem item = new MenuItem();
        item.setLabel(display);
        item.setActionCommand(cmd);
        return item;
    }
}
