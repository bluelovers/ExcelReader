package com.chesterccw.excelreader.util;

import com.chesterccw.excelreader.ExcelReaderAction;
import com.chesterccw.excelreader.ui.MyToolWindow;
import com.chesterccw.excelreader.ui.MyToolWindowFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.RegisterToolWindowTask;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.util.function.Supplier;

/**
 * @author obiscr
 */
public class OpenFile {

    private static final OpenFile INSTANCE = new OpenFile();

    private OpenFile(){

    }

    public static OpenFile getInstance() {
        return INSTANCE;
    }

    public void open(ExcelReaderAction.Data data, Project project){
        MyToolWindow toolWindow = new MyToolWindow(data);
        toolWindow.init();
        MyToolWindowFactory toolWindowFactory = new MyToolWindowFactory(toolWindow);
        JPanel panel = toolWindow.mainPanel;
        ToolWindowManager twm = ToolWindowManager.getInstance(project);
        ToolWindow excelReader = twm.getToolWindow("ExcelReader");
        if(excelReader != null){
            excelReader.remove();
        }
        Supplier<String> supplier = () -> "ExcelReader";
        Icon icon = UIUtil.isUnderDarcula() ?
                IconLoader.findIcon("/images/excel_toolwindow_dark.svg") :
                IconLoader.findIcon("/images/excel_toolwindow.svg");
        RegisterToolWindowTask task = new RegisterToolWindowTask(
                "ExcelReader", ToolWindowAnchor.BOTTOM,
                panel,false,true,true,
                true,toolWindowFactory, icon,supplier
        );
        ToolWindow cipherToolWindow = twm.registerToolWindow(task);
        cipherToolWindow.setTitle(data.title);
        cipherToolWindow.show(() -> {

        });
    }

}
