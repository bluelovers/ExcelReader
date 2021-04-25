package com.chesterccw.excelreader.ui;

import com.chesterccw.excelreader.ExcelReaderAction;
import com.chesterccw.excelreader.ui.components.MyTableModel;
import com.chesterccw.excelreader.util.Constant;
import com.chesterccw.excelreader.util.ResolveData;
import com.chesterccw.excelreader.util.TableTool;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.SearchTextField;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.JBUI;
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
import java.util.Map;
import java.util.Vector;

public class MyToolWindow implements DocumentListener {

    public JPanel mainPanel,filterPanel;
    JBTabbedPane sheetPane;
    SearchTextField filter;
    ComboBox<String> comboBox;

    Vector<String> header;
    Vector<Vector<Object>> rows;
    Map<Integer,String> sheetMap = null;
    JBScrollPane scrollPane;

    public MyToolWindow(ExcelReaderAction.Data data){
        setValue(data);
    }

    public void setValue(ExcelReaderAction.Data data) {
        this.header = data.header;
        this.rows = data.rows;
        if (!data.sheetMap.isEmpty()) {
            this.sheetMap = data.sheetMap;
        }
    }
    public void init(){
        initToolBar();
        JBScrollPane scrollPane = initTablePanel();
        initSheetPane();
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(filterPanel,BorderLayout.NORTH);
        if (sheetMap == null) {
            mainPanel.add(scrollPane,BorderLayout.CENTER);
        } else {
            mainPanel.add(sheetPane,BorderLayout.CENTER);
        }
    }

    private JBScrollPane initTablePanel() {
        JBTable table;
        MyTableModel tableModel;
        tableModel = new MyTableModel(rows,header);
        table = new JBTable(tableModel);
        // table.setCellSelectionEnabled(true);
        TableTool.setTableStyle(table);
        scrollPane = new JBScrollPane(table);
        TableTool.setJspStyle(scrollPane);
        return scrollPane;
    }

    private void initToolBar(){
        filterPanel = new JPanel(new BorderLayout());
        comboBox = new ComboBox<>(getComboBoxSelection());
        filter = new SearchTextField();
        filter.getTextEditor().getDocument().addDocumentListener(this);
        filterPanel.add(comboBox,BorderLayout.WEST);
        filterPanel.add(filter,BorderLayout.CENTER);
    }

    private void initSheetPane() {
        if (sheetMap == null){
            return;
        }
        sheetPane = new JBTabbedPane();
        sheetPane.setTabPlacement(JBTabbedPane.BOTTOM);
        sheetPane.setTabComponentInsets(JBUI.emptyInsets());
        for (Map.Entry<Integer,String> entry : sheetMap.entrySet()) {
            JBScrollPane scrollPane = initTablePanel();
            scrollPane.setBorder(null);
            sheetPane.addTab(entry.getValue(),scrollPane);
        }
        sheetPane.addChangeListener(e -> {
            int selectedIndex = sheetPane.getSelectedIndex();
            JBScrollPane scrollPane =  (JBScrollPane)sheetPane.getSelectedComponent();
            JViewport viewport = scrollPane.getViewport();
            JBTable table = (JBTable)viewport.getView();
            if (selectedIndex < 0) {
                return;
            }
            loadSheetPaneAt(selectedIndex,table);
        });
    }

    public void loadSheetPaneAt(int index,JBTable table){
        ResolveData resolveData = ResolveData.getInstance();
        ExcelReaderAction.Data data = resolveData.resolve(index);
        setValue(data);
        MyTableModel model = (MyTableModel) table.getModel();
        model.setDataVector(rows,header);
        refreshComboBox();
        searchInTable(filter.getText());
        TableTool.fitTableColumns(table);
    }

    public void refreshComboBox(){
        comboBox.removeAllItems();
        String[] comboBoxSelection = getComboBoxSelection();
        for (String string : comboBoxSelection) {
            comboBox.addItem(string);
        }
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

    public void searchInTable(String insertValue){
        if (insertValue == null) {
            return;
        }
        JBScrollPane scrollPane = (JBScrollPane) sheetPane.getSelectedComponent();
        JBTable table = (JBTable) scrollPane.getViewport().getComponent(0);
        MyTableModel tableModel = (MyTableModel) table.getModel();
        TableRowSorter<MyTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        if ("".equals(insertValue)) {
            sorter.setRowFilter(null);
            return;
        }

        RowFilter<MyTableModel, Integer> searchFilter =
                new RowFilter<MyTableModel, Integer>() {
                    @Override
                    public boolean include(Entry<? extends MyTableModel,
                            ? extends Integer> entry) {
                        MyTableModel model = entry.getModel();
                        int index = entry.getIdentifier().intValue();
                        Object selectedItem = comboBox.getSelectedItem();
                        return model.contains(index,filter.getText(),selectedItem ==
                                null ? Constant.SELECT_ALL : selectedItem.toString());
                    }
                };
        sorter.setRowFilter(searchFilter);
    }

    private void searchInTable(Document document){
        String insertValue = "";
        try {
            insertValue = document.getText(0,document.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        searchInTable(insertValue);
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
