/**********************************************************************************************************************
 * Copyright (c) 2020.                                                                                                *
 * This project was created by FranciscoSolis                                                                         *
 **********************************************************************************************************************/

package xyz.theprogramsrc.colegiointeractivojava.superjcore.logging;


import xyz.theprogramsrc.colegiointeractivojava.superjcore.JavaProject;
import xyz.theprogramsrc.colegiointeractivojava.superjcore.ProjectModule;
import xyz.theprogramsrc.colegiointeractivojava.superjcore.config.DataHandler;
import xyz.theprogramsrc.colegiointeractivojava.superjcore.utils.ConsoleColor;
import xyz.theprogramsrc.colegiointeractivojava.superjcore.utils.FileUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogHandler extends ProjectModule {

    private DataHandler dataHandler;

    @Override
    public String getName() {
        return "LogHandler";
    }

    public LogHandler(JavaProject core){
        super(core);
    }

    @Override
    public void onModuleEnable() {
        super.onModuleEnable();
        File logsFolder = new File(this.getDataFolder(), "logs");
        if (!logsFolder.exists()) logsFolder.mkdir();
        try {
            File file = new File(logsFolder.getAbsolutePath(), "latest.log");
            if (file.exists()) {
                String date = DateTimeFormatter.ofPattern("dd-MM-yyyy HH_mm_ss").format(LocalDateTime.now());
                File zipFile = new File(logsFolder.getAbsolutePath(), date + ".zip");
                if (!zipFile.exists()) zipFile.createNewFile();
                FileUtils.zip(zipFile, file);
                Files.delete(Paths.get(file.getAbsolutePath()));
            }
            file.createNewFile();
            this.dataHandler = new DataHandler(file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void log(String message){
        String prefix = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss ").format(LocalDateTime.now()) + this.getMainClass().getSimpleName();
        String out = "[" + prefix + "]: " + message + ConsoleColor.RESET;
        this.dataHandler.write(out);
        System.out.println(out);
    }

    public void err(String message){
        String prefix = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss ").format(LocalDateTime.now()) + this.getMainClass().getSimpleName();
        String out = "[" + prefix + " ERROR]: " + ConsoleColor.RED_BOLD + message + ConsoleColor.RESET;
        this.dataHandler.write(out);
        System.err.println(out);
    }

    public void warn(String message) {
        String prefix = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss ").format(LocalDateTime.now()) + this.getMainClass().getSimpleName();
        String out = "[" + prefix + " WARNING]: " + ConsoleColor.YELLOW_BOLD + message + ConsoleColor.RESET;
        this.dataHandler.write(out);
        System.out.println(out);
    }

    public void debug(String message) {
        String prefix = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss ").format(LocalDateTime.now()) + this.getMainClass().getSimpleName();
        String out = "[" + prefix + " DEBUG]: " + ConsoleColor.CYAN + message  + ConsoleColor.RESET;
        this.dataHandler.write(out);
        if(this.debug()) System.out.println(out);
    }

    public void debug(Exception ex){
        this.dataHandler.write(ex);
        if(this.debug()) ex.printStackTrace(System.out);
    }

}
