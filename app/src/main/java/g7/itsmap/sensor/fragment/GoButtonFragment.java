package g7.itsmap.sensor.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import g7.itsmap.sensor.R;

public class GoButtonFragment extends Fragment {

    public interface GoButtonFragmentListener {

        void onClickRecordButton(GoButtonMode mode);
    }

    public enum GoButtonMode {WAITING, RECORDING}
    private GoButtonFragmentListener listener;
    private Button goButton;
    private GoButtonMode mode;

    public GoButtonFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_go_button, container, false);
        this.goButton = (Button) v.findViewById(R.id.goButton);
        this.mode = GoButtonMode.WAITING;
        this.goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changeMode();
                updateButton();
                listener.onClickRecordButton(mode);
            }
        });

        this.updateButton();

        return v;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (GoButtonFragmentListener) activity;
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

    public void reset() {

        this.mode = GoButtonMode.WAITING;
        this.updateButton();
    }

    private void updateButton() {

        if(this.mode == GoButtonMode.WAITING) {

            this.goButton.setText(R.string.button_go_waiting);
            this.goButton.setBackground(getResources().getDrawable(R.drawable.circle_button_green));

        } else {

            this.goButton.setText(R.string.button_go_recording);
            this.goButton.setBackground(getResources().getDrawable(R.drawable.circle_button_red));
        }
    }

    private void changeMode() {

        if (this.mode == GoButtonMode.WAITING) {

            this.mode = GoButtonMode.RECORDING;

        } else {

            this.mode = GoButtonMode.WAITING;
        }
    }
}
