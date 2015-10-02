package main.java.scripter.template;

import main.java.scripter.util.StringUtils;

/**
 * Created by holly on 02/10/2015.
 */
public class RunDbScriptTemplate
{
    private StringBuffer fileData;
    private boolean includeBackoutInstruction;

    public RunDbScriptTemplate(boolean includeBackout)
    {
        fileData = new StringBuffer();
        includeBackoutInstruction = includeBackout;
    }

    public String createSCript()
    {
        fileData.append("@ECHO OFF\n\nECHO.\nECHO **************************************************************************\n");
        fileData.append("ECHO **  Batch File:  RunDBScript.bat\nECHO **  Purpose:\nECHO **\nECHO **  Parameters:\n");
        fileData.append("ECHO **\t\t\tServer\nECHO **\t\t\tDatabase Name\nECHO **\nECHO **  Author:\nECHO **  Date: " + StringUtils.getTodaysDate() + "\n");
        fileData.append("ECHO **  Build:\nECHO **************************************************************************\n");
        fileData.append("ECHO.\n\nif \"%1\" == \"\" goto ServerError\nif \"%2\" == \"\" goto DBError\n");
        if(includeBackoutInstruction)
        {
            appendBackoutParameter();
        }
        fileData.append("\nSET LOG=osql_%2.log\n\nDel %LOG%\n\nECHO DATE: %DATE% @ %TIME%\nECHO DATE: %DATE% @ %TIME% > %LOG%\n\n");
        fileData.append("ECHO.\nECHO ***********************        Data Updates       ************************\n");
        fileData.append("ECHO ***********************        Data Updates       ************************ >> %LOG%\n\n");
        fileData.append("ECHO Executing InsertServiceCatalog.sql\nECHO Executing InsertServiceCatalog.sql >> %LOG%\n");
        fileData.append("osql -E -S %1 -d %2 -n -i \"Data\\InsertServiceCatalog.sql\" >> %LOG%\ngoto end\n\n");
        if(includeBackoutInstruction)
        {
            appendBackoutInstruction();
        }
        fileData.append(":ServerError\nECHO Error! Server Parameter Not Provided!\ngoto help\n\n:DBError\nECHO Error! Database Parameter Not Provided!\n");
        fileData.append("goto help\n\n:help\nECHO.\nECHO \"Usage: RunDBScript <server name> <database>\"\nECHO.\n\n:end\n");
        fileData.append("ECHO.\nECHO **************************  Process completed!  **************************\n");
        fileData.append("ECHO **************************  Process completed!  ************************** >> %LOG%\n\n");
        return fileData.toString();
    }

    private void appendBackoutParameter()
    {
        fileData.append("if \"%3\" == \"backout\" goto backout\n");
    }

    private void appendBackoutInstruction()
    {
        fileData.append(":backout\nECHO.\nECHO Backing out\nECHO Backing out >> %LOG%\nosql -E -S %1 -d %2 -n -i \"Data\\BackoutServiceCatalog.sql\" >> %LOG%\n");
        fileData.append("goto end\n\n");
    }

}
