/**********************************************************************************************************************
 * Copyright (c) 2020.                                                                                                *
 * This project was created by FranciscoSolis                                                                         *
 **********************************************************************************************************************/

package xyz.theprogramsrc.colegiointeractivojava.superjcore.config;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class DataHandler {
    private File file;

    public DataHandler(String folder, String name){
        try{
            if(folder != null){
                File folderFile = new File(folder);
                if(!folderFile.exists()) folderFile.mkdir();
                File file = new File(folderFile.getAbsolutePath(), name);
                if(!file.exists()) file.createNewFile();
                this.file = file;
            }else{
                File file = new File(name);
                if(!file.exists()) file.createNewFile();
                this.file = file;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public DataHandler(File file){
        try{
            if(!file.exists()) file.createNewFile();
            this.file = file;
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public File getFile() {
        return file;
    }

    public void write(String text){
        try{
            List<String> lines = this.read();
            lines.add(text);
            FileUtils.writeLines(this.file, lines);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void write(Exception ex){
        try{
            FileWriter fileWriter = new FileWriter(this.getFile(), true);
            PrintWriter writer = new PrintWriter(fileWriter);
            ex.printStackTrace(writer);
            writer.close();
            fileWriter.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<String> read(){
        try{
            Path path = Paths.get(this.file.getAbsolutePath());
            return Files.readAllLines(path);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public String read(String match){
        return this.read().stream().filter(s->s.contains(match)).findFirst().orElse(null);
    }

    public String read(int index){
        return this.read().get(index);
    }
}
