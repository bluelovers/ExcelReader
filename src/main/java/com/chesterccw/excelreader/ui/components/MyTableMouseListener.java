package com.chesterccw.excelreader.ui.components;

import com.chesterccw.excelreader.util.TableTool;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyTableMouseListener extends MouseAdapter {

    @Override
    public void mouseClicked(MouseEvent e) {
        JTable table = (JTable) e.getSource();
        if (table == null){
            return;
        }
        if (e.getClickCount() == 2) {
            int col = table.columnAtPoint(e.getPoint());
            TableTool.unfoldCol(table,col);
        }
        if (e.getClickCount() == 3) {
            TableTool.unfoldCol(table,-1);
        }
    }
}
