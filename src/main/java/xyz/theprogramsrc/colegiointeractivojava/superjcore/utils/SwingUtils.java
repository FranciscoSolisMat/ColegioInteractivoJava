/**********************************************************************************************************************
 * Copyright (c) 2020.                                                                                                *
 * This project was created by FranciscoSolis                                                                         *
 **********************************************************************************************************************/

package xyz.theprogramsrc.colegiointeractivojava.superjcore.utils;

import javax.swing.*;
import java.awt.*;

public class SwingUtils {

    public static void buttonToImage(JButton button, Image image){
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        if(button.getIcon() != null) button.setIcon(null);
        button.setIcon(new ImageIcon(image));
    }
}
