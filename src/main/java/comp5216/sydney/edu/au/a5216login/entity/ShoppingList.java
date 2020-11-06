package comp5216.sydney.edu.au.a5216login.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

@Entity(tableName = "shopping_demo")
public class ShoppingList implements Comparable <ShoppingList>{
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "toDoItemID")
    private int toDoItemID;

    @ColumnInfo(name = "toDoItemName")
    private String toDoItemName;

    @ColumnInfo(name = "dateTime")
    private String dateTime;
    private static DateFormat date_format = DateFormat.getDateTimeInstance();

    @Ignore
    public ShoppingList(String toDoItemName){
        this.toDoItemName = toDoItemName;
        this.dateTime = date_format.format(new Date());
    }

    public ShoppingList(String toDoItemName, String dateTime) {
        this.toDoItemName = toDoItemName;
        this.dateTime = dateTime;

    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getToDoItemID() {
        return toDoItemID;
    }

    public void setToDoItemID(int toDoItemID) {
        this.toDoItemID = toDoItemID;
    }

    public String getToDoItemName() {
        return toDoItemName;
    }

    public void setToDoItemName(String toDoItemName) {
        this.toDoItemName = toDoItemName;
        this.dateTime = date_format.format(new Date());
    }

    @Override
    public int compareTo(ShoppingList shoppingList) {
        try {
            Date d1 = date_format.parse(this.dateTime);
            Date d2 = date_format.parse(shoppingList.dateTime);
            return -1 * (d1.compareTo(d2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
