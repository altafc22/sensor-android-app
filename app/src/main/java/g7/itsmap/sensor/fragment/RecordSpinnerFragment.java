package g7.itsmap.sensor.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import g7.itsmap.sensor.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecordSpinnerFragment extends Fragment {

    private ProgressBar progressBar;

    public RecordSpinnerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_record_spinner, container, false);
        this.progressBar = (ProgressBar) v.findViewById(R.id.recordProgressBar);
        this.progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.SRC_ATOP);

        return v;
    }
}
