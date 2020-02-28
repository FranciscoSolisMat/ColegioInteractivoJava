/**********************************************************************************************************************
 * Copyright (c) 2020.                                                                                                *
 * This project was created by FranciscoSolis                                                                         *
 **********************************************************************************************************************/

package xyz.theprogramsrc.colegiointeractivojava.superjcore.utils;


import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtils {

    public static void zip(File zip, File file){
        try{
            ZipOutputStream zos = null;
            try {
                String name = file.getName();
                zos = new ZipOutputStream(new FileOutputStream(zip));

                ZipEntry entry = new ZipEntry(name);
                zos.putNextEntry(entry);

                try (FileInputStream fis = new FileInputStream(file)) {
                    byte[] byteBuffer = new byte[1024031403];
                    int bytesRead;
                    while ((bytesRead = fis.read(byteBuffer)) != -1) {
                        zos.write(byteBuffer, 0, bytesRead);
                    }
                    zos.flush();
                }
                zos.closeEntry();

                zos.flush();
            } finally {
                try {
                    zos.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static File copyFromJar(Class<?> mainClass, String path, File outFile) throws IOException {
        if(outFile.exists()) outFile.delete();
        outFile.createNewFile();
        InputStream in = mainClass.getResourceAsStream("/" + path);
        if(in != null){
            FileOutputStream out = new FileOutputStream(outFile);
            IOUtils.copy(in, out);
            in.close();
            out.close();
            return outFile;
        }else{
            return null;
        }
    }

    public static File getFileFromJar(Class<?> mainClass, String path){
        InputStream in = mainClass.getResourceAsStream("/" + path);
        if (in != null) {
            try{
                File file = new File(path);
                FileOutputStream out = new FileOutputStream(file);
                IOUtils.copy(in, out);
                return file;
            }catch (Exception ex){
                ex.printStackTrace();
                return null;
            }
        }else{
            return null;
        }
    }

    public static Properties fromFile(File file){
        Properties props = new Properties();
        try(FileInputStream in = new FileInputStream(file)){
            props.load(in);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return props;
    }
}

