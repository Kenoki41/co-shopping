package comp5216.sydney.edu.au.a5216login.shoppingRecords;


import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class Month extends ExpandableGroup<comp5216.sydney.edu.au.a5216login.shoppingRecords.Transaction> {

    private String amount;
    public Month(String title, String amount,List<Transaction> items) {
        super(title, items);
        this.amount = amount;
    }

    public String getAmount(){
        return amount;
    }
}
