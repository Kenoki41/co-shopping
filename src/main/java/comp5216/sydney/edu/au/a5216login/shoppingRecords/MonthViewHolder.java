package comp5216.sydney.edu.au.a5216login.shoppingRecords;


import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import comp5216.sydney.edu.au.a5216login.R;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class MonthViewHolder extends GroupViewHolder {
    private TextView mTextView;
    private ImageView arrow;
    private TextView categoryTotoalAmount;
    public MonthViewHolder(View itemView) {
        super(itemView);
        mTextView = itemView.findViewById(R.id.month);
        arrow = itemView.findViewById(R.id.arrow);
        categoryTotoalAmount = itemView.findViewById(R.id.cartegoryTotalamount);
    }

    public void bind(Month month){
        mTextView.setText(month.getTitle());
        categoryTotoalAmount.setText(month.getAmount());

    }









    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
    }

    private void animateExpand() {
        RotateAnimation rotate =
                new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }

    private void animateCollapse() {
        RotateAnimation rotate =
                new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }
}
