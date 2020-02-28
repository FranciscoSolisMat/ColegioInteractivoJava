/**********************************************************************************************************************
 * Copyright (c) 2020.                                                                                                *
 * This project was created by FranciscoSolis                                                                         *
 **********************************************************************************************************************/

package xyz.theprogramsrc.colegiointeractivojava.superjcore.utils;


import java.util.Base64;

public class StringUtils {

    public static String encode(String data){
        Base64.Encoder encoder = Base64.getEncoder();
        return new String(encoder.encode(encoder.encode(encoder.encode(data.getBytes()))));
    }

    public static String decode(String data){
        Base64.Decoder decoder = Base64.getDecoder();
        return new String(decoder.decode(decoder.decode(decoder.decode(data.getBytes()))));
    }
}
