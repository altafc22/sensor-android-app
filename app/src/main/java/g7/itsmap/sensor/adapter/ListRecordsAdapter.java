package g7.itsmap.sensor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import g7.itsmap.sensor.R;
import g7.itsmap.sensor.model.Record;

/**
 * Created by benjaminclanet on 21/04/2015.
 */
public class ListRecordsAdapter extends BaseAdapter {

    ArrayList<Record> recordItems;
    Context context;

    public ListRecordsAdapter(ArrayList<Record> recordItems, Context context) {

        this.recordItems = recordItems;
        this.context = context;
    }

    @Override
    public int getCount() {

        return this.recordItems.size();
    }

    @Override
    public Object getItem(int i) {

        return (Record) this.recordItems.get(i);
    }

    @Override
    public long getItemId(int i) {

        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null) {

            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.record_list_item, null);
        }

        Record r = this.recordItems.get(i);
        TextView textDate = (TextView) view.findViewById(R.id.textRecordDate);

        String formattedString = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(r.getDate());
        textDate.setText(formattedString);

        return view;
    }
}
