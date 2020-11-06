package comp5216.sydney.edu.au.a5216login.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import comp5216.sydney.edu.au.a5216login.R;
import comp5216.sydney.edu.au.a5216login.entity.CoList;

public class CurrListAdapter extends ArrayAdapter<CoList> {
    public CurrListAdapter(@NonNull Context context, @NonNull List<CoList> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CoList list = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_curr_list,
                    parent, false);
        }
        // Lookup view for data population
        TextView tvItemName = (TextView) convertView.findViewById(R.id.currentItemText);
        TextView tvCreationName = (TextView) convertView.findViewById(R.id.creOrEditedTime);
        ImageView imageView = convertView.findViewById(R.id.ivIcon);
        // Populate the data into the template view using the data object
        tvItemName.setText(list.getListName());
        tvCreationName.setText(list.getTime());
        if (list.getType()==1){
            imageView.setImageResource(R.drawable.shopping);
        }
        if (list.getType()==2){
            imageView.setImageResource(R.drawable.wish);
        }

        // Return the completed view to render on screen
        return convertView;
    }



}
