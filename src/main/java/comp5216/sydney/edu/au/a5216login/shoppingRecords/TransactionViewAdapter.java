package comp5216.sydney.edu.au.a5216login.shoppingRecords;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

import comp5216.sydney.edu.au.a5216login.R;

public class TransactionViewAdapter extends ExpandableRecyclerViewAdapter<MonthViewHolder, TransactionViewHolder> {


    public TransactionViewAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public MonthViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_recyclerview_month,null, false);
        return new MonthViewHolder(v);
    }



    @Override
    public TransactionViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_recyclerview_transaction,null, false);
        return new TransactionViewHolder(v);
    }

    @Override
    public void onBindChildViewHolder(TransactionViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        Transaction transaction = (Transaction)group.getItems().get(childIndex);
        holder.bind(transaction);
    }

    @Override
    public void onBindGroupViewHolder(MonthViewHolder holder, int flatPosition, ExpandableGroup group) {
        Month month = (Month)group;
        holder.bind(month);
    }
}
