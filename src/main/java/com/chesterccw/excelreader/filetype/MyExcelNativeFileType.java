package com.chesterccw.excelreader.filetype;

import com.chesterccw.excelreader.ExcelReaderAction;
import com.chesterccw.excelreader.util.OpenFile;
import com.chesterccw.excelreader.util.ResolveData;
import com.intellij.ide.IdeBundle;
import com.intellij.openapi.fileTypes.INativeFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.IdeFrame;
import com.intellij.openapi.wm.impl.IdeFocusManagerImpl;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;

public final class MyExcelNativeFileType implements INativeFileType {

    public static final MyExcelNativeFileType INSTANCE = new MyExcelNativeFileType();

    private MyExcelNativeFileType() {

    }

    @Override
    public boolean openFileInAssociatedApplication(Project project, @NotNull VirtualFile virtualFile) {
        return openInExcelReader(virtualFile);
    }

    @Override
    public boolean useNativeIcon() {
        return false;
    }

    @NotNull
    @Override
    public String getName() {
        return "MyNative";
    }

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    @Override
    public String getDescription() {
        return IdeBundle.message("native.filetype.description");
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "xls;xlsx;csv";
    }

    @NotNull
    @Override
    public Icon getIcon() {
        return Objects.requireNonNull(IconLoader.findIcon("/images/excel.svg"));
    }

    @Override
    public boolean isBinary() {
        return true;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Nullable
    @Override
    public String getCharset(@NotNull VirtualFile virtualFile, byte @NotNull [] bytes) {
        return null;
    }

    public boolean openInExcelReader(@NotNull final VirtualFile file){
        ResolveData resolveData = ResolveData.getInstance();
        resolveData.setValue(file);
        ExcelReaderAction.Data data = resolveData.resolve(0);
        IdeFrame lastFocusedFrame = IdeFocusManagerImpl.getGlobalInstance().getLastFocusedFrame();
        if(lastFocusedFrame == null){
            return false;
        }
        OpenFile.getInstance().open(data,lastFocusedFrame.getProject());
        return true;
    }
}
