package com.chesterccw.excelreader.ui.components;

import com.chesterccw.excelreader.util.Constant;
import org.apache.commons.lang3.StringUtils;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @author chesterccw
 * @date 2020/7/17
 */
public class MyTableModel extends DefaultTableModel {

    Vector<Vector<Object>> rows;
    Vector<String> header;
    public MyTableModel(Vector<Vector<Object>> rows, Vector<String> header){
        this.rows = rows;
        this.header = header;
        setDataVector(rows,header);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {

    }

    public boolean contains(int row, String keyWord, String selectedItem){
        if(Constant.SELECT_ALL.equals(selectedItem)){
            List<String> rowData = getRowData(row);
            for(String s : rowData){
                if(StringUtils.isEmpty(s)){
                    continue;
                }
                if(s.contains(keyWord)){
                    return true;
                }
            }
        } else {
            int column = getColumnIndex(selectedItem);
            if(column < 0) {
                return false;
            }
            if(row - 1 > getRowCount()){
                return false;
            }
            Object valueAt = getValueAt(row, column);
            return valueAt != null && valueAt.toString().contains(keyWord);
        }
        return false;
    }

    public int getColumnIndex(String selectedItem){
        int size = getColumnCount();
        for(int i = 0 ; i < size ; i++){
            String columnName = getColumnName(i);
            if(selectedItem.equals(columnName)){
                return i;
            }
        }
        return -1;
    }

    public List<String> getRowData(int row){
        List<String> list = new ArrayList<>();
        int size = getColumnCount();
        for(int i = 0 ; i < size ; i++){
            Object valueAt = getValueAt(row,i);
            if(valueAt == null) {
                continue;
            }
            list.add(valueAt.toString());
        }
        return list;
    }


}
