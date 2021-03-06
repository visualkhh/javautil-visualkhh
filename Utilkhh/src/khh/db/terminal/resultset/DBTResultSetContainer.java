package khh.db.terminal.resultset;

import java.util.ArrayList;

import khh.std.adapter.AdapterMapBase;

public class DBTResultSetContainer extends AdapterMapBase<Long, DBTResultRecord> {
    ArrayList<String> columnNames ;
    Long atRow =null;
    
    public DBTResultSetContainer() {
        columnNames = new ArrayList<String>();
    }
    
    public void addColumnName(String name){
        columnNames.add(name);
    }
    public void setColumnName(ArrayList<String> columnname){
        columnNames = columnname;
    }
    
    public String getColumnName(int index){
        return columnNames.get(index);
    }
    
    
    
    

}
