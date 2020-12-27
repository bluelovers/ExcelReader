package com.chesterccw.excelreader;

import com.chesterccw.excelreader.util.AnActionEventUtil;
import com.chesterccw.excelreader.util.OpenFile;
import com.chesterccw.excelreader.util.ResolveData;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.Vector;

public class ExcelReaderAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Data data = new ResolveData(e.getData(CommonDataKeys.VIRTUAL_FILE)).resolve();
        Project project = e.getData(PlatformDataKeys.PROJECT);
        if(project == null){
            return;
        }
        OpenFile.getInstance().open(data,project);
    }

    public static class Data {
        public String title;
        public Vector<String> header = new Vector<>();
        public Vector<Vector<Object>> rows = new Vector<>();
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(AnActionEventUtil.available(e));
    }
}
