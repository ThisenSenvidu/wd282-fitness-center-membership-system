package com.fitness.plan;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import java.io.*;
import java.util.ArrayList;

@Component
public class PlanFileHandler {

    @Value("${app.plans.file-path}")
    private String filepath;

@PostConstruct                                                    // run app and create a file if they not in folder
    public void initFile() {
    try {
        File file = new File(filepath);
        File dir = file.getParentFile();
        if (dir !=null && !dir.exists()) dir.mkdirs();
        if (!file.exists())
            file.createNewFile();
    } catch (IOException e){
        System.out.println("Warning " + e.getMessage());
    }

}

}
