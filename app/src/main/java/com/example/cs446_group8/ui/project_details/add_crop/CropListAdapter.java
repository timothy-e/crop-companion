package com.example.cs446_group8.ui.project_details.add_crop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.cs446_group8.R;
import com.example.cs446_group8.data.Sow;
import com.example.cs446_group8.data.SowDao;

import java.util.ArrayList;

public class CropListAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<AddCropActivity.CropListItem> cropList = new ArrayList<AddCropActivity.CropListItem>();
    private Context context;
    private long projectId;
    private SowDao sowDao;
    public CropListAdapter(ArrayList<AddCropActivity.CropListItem> list, long projectId, SowDao sowDao, Context context) {
        this.cropList = list;
        this.context = context;
        this.sowDao = sowDao;
        this.projectId = projectId;

    }

    @Override
    public int getCount() {
        return cropList.size();
    }

    @Override
    public Object getItem(int pos) {
        return cropList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.crop_list_item, null);
        }

        TextView listItemText = (TextView) view.findViewById(R.id.list_item_string);
        listItemText.setText(cropList.get(position).cropName);

        // onClickListener
        ImageButton addBtn = (ImageButton) view.findViewById(R.id.add_btn);

        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                addBtn.setAlpha(.2f);
                addBtn.setClickable(false);
                // TODO: store crop as "added" to parent Project obj
                sowDao.insertAll(Sow.builder().projectId((int)projectId).cropId((int)cropList.get(position).cropId).build());

            }
        });

        return view;
    }
}
