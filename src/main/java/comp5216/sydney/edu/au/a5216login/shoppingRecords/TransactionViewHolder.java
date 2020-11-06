package comp5216.sydney.edu.au.a5216login.shoppingRecords;

import android.view.View;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import comp5216.sydney.edu.au.a5216login.R;

public class TransactionViewHolder extends ChildViewHolder {
    TextView tv_description;
    TextView tv_amount;
    TextView  tv_datime;
    public TransactionViewHolder(View itemView) {
        super(itemView);
        tv_description = (TextView)itemView.findViewById(R.id.description);
        tv_amount = (TextView)itemView.findViewById(R.id.amount);
        tv_datime = (TextView)itemView.findViewById(R.id.datime);

    }

    public void bind(Transaction transaction){
        tv_amount.setText(""+ transaction.getAmount());
        tv_description.setText( transaction.getDescription());
        tv_datime.setText( transaction.getDatetime());
    }
}
