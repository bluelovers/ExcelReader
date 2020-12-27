package com.chesterccw.excelreader.ui;

import com.chesterccw.excelreader.ui.components.MyTableModel;
import com.chesterccw.excelreader.util.Constant;
import com.chesterccw.excelreader.util.MyFont;
import com.chesterccw.excelreader.util.TableTool;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.table.JBTable;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MyToolWindow implements DocumentListener {

    public JPanel mainPanel,filterPane;
    JBTable table;
    JBScrollPane scrollPane;
    JBTextField filter;
    ComboBox<String> comboBox;

    Vector<String> header;
    Vector<Vector<Object>> rows;

    TableRowSorter<MyTableModel> sorter;
    RowFilter<MyTableModel, Integer> searchFilter;
    MyTableModel tableModel;

    MyToolWindow(){

    }

    public MyToolWindow(Vector<String> header, Vector<Vector<Object>> rows){
        this.header = header;
        this.rows = rows;
    }

    public void init(){
        TableTool tool = new TableTool();
        tableModel = new MyTableModel(rows,header);
        table = new JBTable(tableModel);
        tool.setTableStyle(table);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        searchFilter = new RowFilter<MyTableModel, Integer>() {
            @Override
            public boolean include(Entry<? extends MyTableModel, ? extends Integer> entry) {
                MyTableModel model = entry.getModel();
                int index = entry.getIdentifier().intValue();
                Object selectedItem = comboBox.getSelectedItem();
                return model.contains(index,filter.getText(),selectedItem == null ? Constant.SELECT_ALL : selectedItem.toString());
            }
        };

        scrollPane = new JBScrollPane(table);
        tool.setJspStyle(scrollPane);
        filterPane = new JPanel(new BorderLayout());
        comboBox = new ComboBox<>(getComboBoxSelection());
        comboBox.setFont(MyFont.Common);

        filter = new JBTextField();
        filter.setFont(MyFont.Common);
        filter.getDocument().addDocumentListener(this);
        filterPane.add(comboBox,BorderLayout.WEST);
        filterPane.add(filter,BorderLayout.CENTER);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(filterPane,BorderLayout.NORTH);
        mainPanel.add(scrollPane,BorderLayout.CENTER);
    }

    private String[] getComboBoxSelection(){
        List<String> list = new ArrayList<>();
        list.add(Constant.SELECT_ALL);
        for(String s : header){
            if(StringUtils.isNotEmpty(s)){
                list.add(s);
            }
        }
        return list.toArray(new String[]{});
    }

    private void searchInTable(Document document){
        String insertValue = "";
        try {
            insertValue = document.getText(0,document.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        if(StringUtils.isNotEmpty(insertValue)){
            sorter.setRowFilter(searchFilter);
        } else {
            sorter.setRowFilter(null);
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        searchInTable(e.getDocument());
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        searchInTable(e.getDocument());
    }

    @Override
    public void changedUpdate(DocumentEvent e) {

    }
}
