package g7.itsmap.sensor.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import g7.itsmap.sensor.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BackButtonFragment extends Fragment {

    public interface BackButtonFragmentListener {

        void onClickBackButton();
    }

    private Button buttonBack;
    private BackButtonFragmentListener listener;

    public BackButtonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_back_button, container, false);
        this.buttonBack = (Button) v.findViewById(R.id.buttonBack);
        this.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.onClickBackButton();
            }
        });

        return v;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (BackButtonFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement GoButtonFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
