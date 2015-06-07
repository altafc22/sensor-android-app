package g7.itsmap.sensor.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import g7.itsmap.sensor.R;
import g7.itsmap.sensor.adapter.ListRecordsAdapter;
import g7.itsmap.sensor.model.Record;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListRecordsFragment extends Fragment {

    public interface ListRecordsFragmentListener {

        ArrayList<Record> needRecordList();
        void onSelectedRecord(int index);
    }

    private ListView listView;
    private ListRecordsFragmentListener listener;

    public ListRecordsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_record, container, false);
        this.listView = (ListView) v.findViewById(R.id.listViewRecords);

        this.displayList();

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (ListRecordsFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ListRecordsFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private void displayList() {

        ArrayList<Record> records = this.listener.needRecordList();

        ListRecordsAdapter adapter = new ListRecordsAdapter(records, getActivity());
        this.listView.setAdapter(adapter);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                view.setSelected(true);
                listener.onSelectedRecord(position);
            }
        });
    }

}
