package main.java.scripter.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by holly on 02/10/2015.
 */
public class ScriptFileWriter
{
    public ScriptFileWriter()
    {

    }

    public boolean createScript(String text, String path, String fileName)
    {
        boolean wasSuccessful = false;
        createDirIfNotExists(path);
        String absolutePath = path + System.getProperty("file.separator") + fileName;
        File file = new File(path);
        file.mkdirs();
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(absolutePath)))
        {
            writer.write(text);
            wasSuccessful = true;
        }
        catch (IOException io)
        {
            io.printStackTrace();
        }
        return wasSuccessful;
    }

    private void createDirIfNotExists(String path)
    {

    }
}
