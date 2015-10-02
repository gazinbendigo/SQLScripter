package main.java.scripter.template;

import java.util.List;

/**
 * Created by adm9360_a on 1/10/2015.
 */
public class BackoutServiceCatalogtemplate
{
    private List<String> serviceNumbers;


    public BackoutServiceCatalogtemplate(List<String> serviceNumbers)
    {
        this.serviceNumbers = serviceNumbers;
    }

    public String createScript()
    {
        StringBuilder data = new StringBuilder();
        StringBuilder serviceNos = new StringBuilder();
        for (int i = 0; i < serviceNumbers.size(); i++) {
            if((i + 1) == serviceNumbers.size()) {
                serviceNos.append(serviceNumbers.get(i));
            }
            else
            {
                serviceNos.append((serviceNumbers.get(i) + ","));
            }
        }
        if(serviceNumbers.size() > 0)
        {
            data.append("BEGIN TRANSACTION;\n\n");
            data.append("IF EXISTS (\n\tSELECT 1\n\tFROM dbo.REQUEST_SERVICE_LOG\n\t");
            data.append("WHERE REQUEST_SERVICE_ID IN (SELECT REQUEST_SERVICE_ID FROM dbo.REQUEST_SERVICE WHERE SERVICE_ID in (" +  serviceNos.toString() + ")))\n");
            data.append("BEGIN\n\tRAISERROR('Deleting Record in dbo.REQUEST_SERVICE_LOG', 10, 1) WITH NOWAIT;\n\n\t");
            data.append("DELETE FROM dbo.REQUEST_SERVICE_LOG\n\tWHERE REQUEST_SERVICE_ID IN (SELECT REQUEST_SERVICE_ID FROM dbo.REQUEST_SERVICE WHERE SERVICE_ID in (" +  serviceNos.toString() + "))\n\n");
            data.append("IF @@ERROR <> 0 GOTO PROCERROR;\nEND\nELSE\nBEGIN\n\tRAISERROR('Record does not exist in dbo.REQUEST_SERVICE_LOG', 10, 1) WITH NOWAIT;\n");
            data.append("END;\n\nIF EXISTS (\n\tSELECT 1\n\tFROM dbo.REQUEST_SERVICE\n\tWHERE SERVICE_ID in (" +  serviceNos.toString() + "))\nBEGIN\n\t");
            data.append("RAISERROR('Deleting Record in dbo.REQUEST_SERVICE', 10, 1) WITH NOWAIT;\n\n\tDELETE FROM dbo.REQUEST_SERVICE\n\t\t");
            data.append("WHERE SERVICE_ID in (" +  serviceNos.toString() + ");\n\n\tIF @@ERROR <> 0 GOTO PROCERROR;\nEND\nELSE\nBEGIN\n\t");
            data.append("RAISERROR('Record does not exist in dbo.REQUEST_SERVICE', 10, 1) WITH NOWAIT;\nEND;\nIF EXISTS (\n\t");
            data.append("SELECT 1\n\tFROM dbo.SERVICE_CATALOG\n\tWHERE SERVICE_ID in (" +  serviceNos.toString() + "))\n\tBEGIN\n\t");
            data.append("RAISERROR('Deleting Record in dbo.SERVICE_CATALOG', 10, 1) WITH NOWAIT;\n\n\t");
            data.append("DELETE FROM dbo.SERVICE_CATALOG\n\t\tWHERE SERVICE_ID in (" +  serviceNos.toString() + ");\n\n\tIF @@ERROR <> 0 GOTO PROCERROR;\n");
            data.append("END\nELSE\nBEGIN\n\tRAISERROR('Record does not exist in dbo.SERVICE_CATALOG', 10, 1) WITH NOWAIT;\nEND;\n\n");
            data.append("COMMIT TRANSACTION;\n\nPROCEND:\n\tRETURN;\n\nPROCERROR:\n\tROLLBACK TRANSACTION;\n\n\tGOTO PROCEND;\n\n");
        }
        return data.toString();
    }

}

