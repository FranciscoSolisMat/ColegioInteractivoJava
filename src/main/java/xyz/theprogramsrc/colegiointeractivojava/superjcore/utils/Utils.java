/**********************************************************************************************************************
 * Copyright (c) 2020.                                                                                                *
 * This project was created by FranciscoSolis                                                                         *
 **********************************************************************************************************************/

package xyz.theprogramsrc.colegiointeractivojava.superjcore.utils;

import java.awt.*;
import java.net.URL;

public class Utils {

    public static boolean openWebsite(String url){
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if(desktop != null){
            if(desktop.isSupported(Desktop.Action.BROWSE)){
                try{
                    desktop.browse(new URL(url).toURI());
                    return true;
                }catch (Exception ex){
                    ex.printStackTrace();
                    return false;
                }
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    public static Color fromHex(String data){
        return new Color(Integer.valueOf(data.substring(1,3),16), Integer.valueOf(data.substring(3,5),16), Integer.valueOf(data.substring(5,7),16));
    }
}
