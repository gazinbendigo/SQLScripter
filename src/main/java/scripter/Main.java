package main.java.scripter;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import main.java.scripter.io.ScriptFileWriter;
import main.java.scripter.template.BackoutServiceCatalogtemplate;
import main.java.scripter.template.InsertServiceCatalogTemplate;
import main.java.scripter.template.RunDbScriptTemplate;
import main.java.scripter.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application
{
    private boolean createRunDbScript = true;
    private boolean createServiceCatalogScript = true;
    private boolean createBackoutScript = true;
    private boolean isServiceNumnbersNotEmpty;
    private boolean isServiceNamesNotEmpty;
    private CheckBox createRunDbScriptCkbx;
    private CheckBox createServiceCatalogCkbx;
    private CheckBox backoutServiceCatalogCkbx;
    private Label serviceEntryInstructionLbl;
    private Label serviceIdLbl;
    private TextField serviceIdsTxFld;
    private Label serviceNamesLbl;
    private TextField serviceNamesTxFld;
    private Button createBttn;
    private ScriptFileWriter scriptWriter;
    private String dataDirectory;


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("SqlScripter.fxml"));
        primaryStage.setTitle("SQL Scripter");
        Label createOptionsText = new Label("I want to create:");
        createRunDbScriptCkbx = new CheckBox("A RunDbScript");
        createRunDbScriptCkbx.setSelected(true);
        createServiceCatalogCkbx = new CheckBox("Service Catalog insert script.");
        createServiceCatalogCkbx.setSelected(true);
        backoutServiceCatalogCkbx = new CheckBox("Backout Service Catalog script.");
        backoutServiceCatalogCkbx.setSelected(true);
        serviceEntryInstructionLbl = new Label("Enter comma separated Service numbers & Names.");
        serviceIdLbl = new Label("Enter Service numbers: ");
        serviceIdsTxFld = new TextField();
        serviceNamesLbl = new Label("Enter Service Names: ");
        serviceNamesTxFld = new TextField();
        createBttn = new Button("Create");
        createBttn.setDisable(true);
        scriptWriter = new ScriptFileWriter();
        dataDirectory = "C:\\Scripts\\Data";

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        grid.add(createOptionsText, 0, 0);
        grid.add(createRunDbScriptCkbx, 0, 1);
        grid.add(createServiceCatalogCkbx, 0, 2);
        grid.add(backoutServiceCatalogCkbx, 0, 3, 2, 1);
        grid.add(serviceEntryInstructionLbl, 0, 4, 2, 1);
        grid.add(serviceIdLbl, 0, 5);
        grid.add(serviceIdsTxFld, 1, 5);
        grid.add(serviceNamesLbl, 0, 6);
        grid.add(serviceNamesTxFld, 1, 6);
        grid.add(createBttn, 1, 7, 2, 1);

        createRunDbScriptCkbx.setOnAction(e -> handleRunDbScriptAction(e));
        createServiceCatalogCkbx.setOnAction(e -> handleCreateServiceCatalogAction(e));
        backoutServiceCatalogCkbx.setOnAction(e -> handleCreateBackoutScriptAction(e));
        createBttn.setOnAction(e -> createScriptsAction(e));

        serviceIdsTxFld.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(StringUtils.isValidString(newValue))
                {
                    isServiceNumnbersNotEmpty = true;
                    if(isServiceNumnbersNotEmpty && isServiceNamesNotEmpty)
                    {
                        createBttn.setDisable(false);
                    }
                }
                else
                {
                    isServiceNumnbersNotEmpty = false;
                    createBttn.setDisable(true);
                }
            }
        });

        serviceNamesTxFld.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(StringUtils.isValidString(newValue))
                {
                    isServiceNamesNotEmpty = true;
                    if(isServiceNumnbersNotEmpty && isServiceNamesNotEmpty)
                    {
                        createBttn.setDisable(false);
                    }
                }
                else
                {
                    isServiceNamesNotEmpty = false;
                    createBttn.setDisable(true);
                }
            }
        });

        Scene scene = new Scene(grid, 400, 275);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private void handleCreateBackoutScriptAction(ActionEvent event)
    {
        CheckBox checkBox = (CheckBox) event.getSource();
        setCreateBackoutScript(checkBox.isSelected());
    }

    private void handleRunDbScriptAction(ActionEvent event)
    {
        CheckBox checkBox = (CheckBox) event.getSource();
        setCreateRunDbScript(checkBox.isSelected());
    }

    private void handleCreateServiceCatalogAction(ActionEvent event)
    {
        CheckBox checkBox = (CheckBox) event.getSource();
        setCreateServiceCatalogScript(checkBox.isSelected());
    }

    private void setCreateBackoutScript(boolean value)
    {
        createBackoutScript = value;
    }

    private boolean getCreateBackoutScript()
    {
        return createBackoutScript;
    }

    private void setCreateRunDbScript(boolean value)
    {
        createRunDbScript = value;
    }

    private boolean getCreateRunDbScript()
    {
        return createRunDbScript;
    }

    public void setCreateServiceCatalogScript(boolean value)
    {
        createServiceCatalogScript = value;
    }

    private boolean getCreateServiceCatalogScript()
    {
        return createServiceCatalogScript;
    }

    private void createScriptsAction(ActionEvent event)
    {
        if(getCreateRunDbScript())
        {
            createRunDbScript();
        }
        if(getCreateServiceCatalogScript())
        {
            createServiceCatalogScript();
        }
        if(getCreateBackoutScript())
        {
            createBackoutServiceCatalogScript();
        }
        System.out.println("Scripts Completed.");
    }

    private void createRunDbScript()
    {
        RunDbScriptTemplate runDbTemplate = new RunDbScriptTemplate(getCreateBackoutScript());
        scriptWriter.createScript(runDbTemplate.createSCript(), "C:\\Scripts", "RunDbScript.bat");
    }

    private void createServiceCatalogScript()
    {
        List<String> serviceNumbers = extractCommaSeparatedInputIntoList(serviceIdsTxFld.getText());
        List<String> serviceNames = extractCommaSeparatedInputIntoList(serviceNamesTxFld.getText());
        InsertServiceCatalogTemplate template = new InsertServiceCatalogTemplate(serviceNumbers, serviceNames);
        scriptWriter.createScript(template.createSCript(), dataDirectory, "InsertServiceCatalog.sql");
    }

    private void createBackoutServiceCatalogScript()
    {
        List<String> serviceNumbers = extractCommaSeparatedInputIntoList(serviceIdsTxFld.getText());
        BackoutServiceCatalogtemplate template = new BackoutServiceCatalogtemplate(serviceNumbers);
        scriptWriter.createScript(template.createScript(), dataDirectory, "BackoutServiceCatalog.sql");
    }

    private List<String> extractCommaSeparatedInputIntoList(String input)
    {
        List<String> list = new ArrayList<String>();
        String data[] = input.split(",");
        for (int i = 0; i < data.length; i++)
        {
            list.add(StringUtils.removeLeadingWhiteSpace(data[i]));
        }
        return list;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
