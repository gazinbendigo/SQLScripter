package main.java.scripter.template;

import java.util.List;

/**
 * Created by holly on 30/09/2015.
 */
public class InsertServiceCatalogTemplate
{
    private StringBuffer data;
    private List<String> serviceNumbers;
    private List<String> serviceNames;

    public InsertServiceCatalogTemplate(List<String> serviceNumbers, List<String> serviceNames)
    {
        data = new StringBuffer();
        this.serviceNames = serviceNames;
        this.serviceNumbers = serviceNumbers;
    }

    public String createSCript()
    {
        if(serviceNames.size() == serviceNames.size())
        {
            data.append("BEGIN TRANSACTION;\n\n");
            for(int i = 0; i < serviceNumbers.size(); i++)
            {
                data.append("IF EXISTS (\n\tSELECT 1\n\tFROM dbo.SERVICE_CATALOG\n\tWHERE SERVICE_ID = " + serviceNumbers.get(i) + ")\n");
                data.append("BEGIN\n\tRAISERROR('Updating Record in dbo.SERVICE_CATALOG', 10, 1) WITH NOWAIT;\n\n");
                data.append("\tUPDATE dbo.SERVICE_CATALOG\n\tSET SERVICE_NME = '" + serviceNames.get(i) + "',\n\t\t");
                data.append("SERVICE_LOG_IND = 1\n\tWHERE SERVICE_ID = " + serviceNumbers.get(i) + ";\n\n\tIF @@ERROR <> 0 GOTO PROCERROR;\nEND\nELSE\n");
                data.append("BEGIN\n\tRAISERROR('Inserting Record into dbo.SERVICE_CATALOG', 10, 1) WITH NOWAIT;\n\n\t");
                data.append("INSERT INTO dbo.SERVICE_CATALOG (\n\t\tSERVICE_ID,\n\t\tSERVICE_NME,\n\t\tSERVICE_LOG_IND)\n\t");
                data.append("VALUES (\n\t\t" + serviceNumbers.get(i) + ",\n\t\t'"+ serviceNames.get(i) + "',\n\t\t1);\n\n\tIF @@ERROR <> 0 GOTO PROCERROR;\nEND;\n\n");
            }
            data.append("COMMIT TRANSACTION;\n\nPROCEND:\n\tRETURN;\n\n");
            data.append("PROCERROR:\n\tROLLBACK TRANSACTION;\n\n\tGOTO PROCEND;\n\n");
        }
        return data.toString();
    }
}

