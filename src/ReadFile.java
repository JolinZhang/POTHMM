

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by Jonelezhang on 3/28/17.
 */
public class ReadFile {


    public String[] readFiles(String path){
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> fileName = new ArrayList<>();
        StringBuffer stringBuffer = new StringBuffer();
        int file_count = 0;
        //read and write all sentences into stringBuffer
        for (int i = 0; i < listOfFiles.length; i++) {
            //500 files
            File file = listOfFiles[i];
            if (file.isFile()&& file.getName().matches("[a-zA-Z ]*\\d+.*")) {
                fileName.add(file.getName());
//                System.out.println(file.getName());
                try {
                    FileReader fileReader = new FileReader(file);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    file_count++;


                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuffer.append(line);
                        stringBuffer.append(" ");
                    }
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
//        System.out.println(stringBuffer);
        System.out.println("file count: "+ file_count);
        //get the array of all sentences
        String[] sentences = new String(stringBuffer).split("  ");


        return sentences;
    }


}
