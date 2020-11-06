package comp5216.sydney.edu.au.a5216login.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import comp5216.sydney.edu.au.a5216login.entity.Item;
import comp5216.sydney.edu.au.a5216login.R;

public class CurrItemAdapter extends BaseAdapter {


    private List<Item> mData;
    private LayoutInflater mInflater;

    public CurrItemAdapter(List<Item> mData, LayoutInflater mInflater) {
        this.mData = mData;
        this.mInflater = mInflater;
    }

    public List<Item> getmData() {
        return mData;
    }

    public LayoutInflater getmInflater() {
        return mInflater;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        //获得ListView中的view
        View viewListItem = mInflater.inflate(R.layout.view_curr_item,null);
        //获得ListItem对象
        Item item = mData.get(position);
        //获得自定义布局中每一个控件的对象。
        TextView name = (TextView) viewListItem.findViewById(R.id.textView_name);
        TextView price = (TextView) viewListItem.findViewById(R.id.textView_price);
        TextView quantity = (TextView) viewListItem.findViewById(R.id.textView_Qty);
        //将数据一一添加到自定义的布局中。
        name.setText(item.getName());
        price.setText(item.getPrice().toString());
        quantity.setText(item.getQuantity()+"");
        if (item.getComplete()==1){
            viewListItem.setBackgroundResource(R.color.textColorChecked);
        }
        return viewListItem ;
    }
}
