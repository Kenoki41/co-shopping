package comp5216.sydney.edu.au.a5216login.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import comp5216.sydney.edu.au.a5216login.entity.ShoppingList;

@Dao
public interface ToDoItemDao {
    @Query("SELECT * FROM shopping_demo")
    List<ShoppingList> listAll();

    @Insert
    void insert(ShoppingList shoppingList);

    @Insert
    void insertAll(ShoppingList... shoppingLists);

    @Query("DELETE FROM shopping_demo")
    void deleteAll();
}
