package g7.itsmap.sensor.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import g7.itsmap.sensor.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class InfoRecordFragment extends Fragment {


    public InfoRecordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info_record, container, false);
    }


}
