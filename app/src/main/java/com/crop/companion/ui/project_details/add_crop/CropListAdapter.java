package com.crop.companion.ui.project_details.add_crop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.crop.companion.R;
import com.crop.companion.data.Sow;
import com.crop.companion.data.SowDao;

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
                sowDao.insertAll(Sow.builder().projectId(projectId).cropId(cropList.get(position).cropId).build());
                Toast.makeText(context, cropList.get(position).cropName + " added to the project! ", Toast.LENGTH_SHORT).show();
                cropList.remove(position);
                notifyDataSetChanged();
            }
        });

        return view;
    }
}
