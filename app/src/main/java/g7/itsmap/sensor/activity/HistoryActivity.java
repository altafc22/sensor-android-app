package g7.itsmap.sensor.activity;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Date;
import java.util.ArrayList;

import g7.itsmap.sensor.R;
import g7.itsmap.sensor.database.DatabaseRecordHelper;
import g7.itsmap.sensor.fragment.ListRecordsFragment;
import g7.itsmap.sensor.fragment.RecordDateFragment;
import g7.itsmap.sensor.fragment.RecordedValuesFragment;
import g7.itsmap.sensor.model.Record;

public class HistoryActivity extends AppCompatActivity implements RecordedValuesFragment.RecordedValuesFragmentListener, ListRecordsFragment.ListRecordsFragmentListener, RecordDateFragment.RecordDateFragmentListener {

    private enum ActivityMode {LIST, DETAIL}
    private enum OrientationMode {PORTRAIT, LANDSCAPE}

    private static final String LIST_FRAG = "list_fragment";
    private static final String RECORDED_VALUES_FRAG = "recorded_values_fragment";
    private static final String RECORD_DATE_FRAG = "record_date_fragment";

    private DatabaseRecordHelper database;
    private LinearLayout listContainer;
    private LinearLayout detailContainer;
    private ArrayList<Record> records;
    private int selectedRecord;
    private ActivityMode modeActivity;
    private OrientationMode modeOrientation;
    private RecordedValuesFragment recordedValuesFragment;
    private ListRecordsFragment listRecordsFragment;
    private RecordDateFragment recordDateFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(45, 43, 56)));

        this.database = new DatabaseRecordHelper(this);

        listContainer = (LinearLayout)findViewById(R.id.list_container);
        detailContainer = (LinearLayout)findViewById(R.id.detail_container);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            this.modeOrientation = OrientationMode.PORTRAIT;

        } else {

            this.modeOrientation = OrientationMode.LANDSCAPE;
        }

        this.loadRecords();

        if(savedInstanceState == null) {

            this.modeActivity = ActivityMode.LIST;
            this.selectedRecord = 0;

            this.listRecordsFragment = new ListRecordsFragment();
            this.recordedValuesFragment = new RecordedValuesFragment();
            this.recordDateFragment = new RecordDateFragment();

            if(this.records.size() > 0) {

                getFragmentManager().beginTransaction()
                        .add(R.id.list_container, this.listRecordsFragment, LIST_FRAG)
                        .add(R.id.record_container, this.recordedValuesFragment, RECORDED_VALUES_FRAG)
                        .add(R.id.record_date_container, this.recordDateFragment, RECORD_DATE_FRAG)
                        .commit();
            }

        } else {

            this.selectedRecord = savedInstanceState.getInt("record_position");
            this.modeActivity = (ActivityMode) savedInstanceState.getSerializable("activity_mode");

            if(this.modeActivity == null) {

                this.modeActivity = ActivityMode.LIST;
            }

            this.listRecordsFragment = (ListRecordsFragment)getFragmentManager().findFragmentByTag(LIST_FRAG);
            if(this.listRecordsFragment == null) {

                this.listRecordsFragment = new ListRecordsFragment();
            }

            this.recordedValuesFragment = (RecordedValuesFragment)getFragmentManager().findFragmentByTag(RECORDED_VALUES_FRAG);
            if(this.recordedValuesFragment == null) {

                this.recordedValuesFragment = new RecordedValuesFragment();
            }

            this.recordDateFragment = (RecordDateFragment)getFragmentManager().findFragmentByTag(RECORD_DATE_FRAG);
            if(this.recordDateFragment == null) {

                this.recordDateFragment = new RecordDateFragment();
            }
        }

        if(this.records.size() > 0) {

            this.updateFragmentState(this.modeActivity);
        }
    }

    private void loadRecords() {

        this.records = this.database.getAllRecords();
    }

    private void updateFragmentState(ActivityMode targetMode){

        if(targetMode == ActivityMode.DETAIL) {

            this.modeActivity = ActivityMode.DETAIL;

        } else {

            this.modeActivity = ActivityMode.LIST;
        }

        switchFragment(targetMode);
    }

    private void switchFragment(ActivityMode targetMode){

        if(this.modeOrientation == OrientationMode.PORTRAIT) {

            if (targetMode == ActivityMode.LIST) {

                listContainer.setVisibility(View.VISIBLE);
                detailContainer.setVisibility(View.GONE);

            } else {

                listContainer.setVisibility(View.GONE);
                detailContainer.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putInt("record_position", this.selectedRecord);
        outState.putSerializable("activity_mode", this.modeActivity);
    }

    @Override
    public void onBackPressed() {

        if(this.modeOrientation == OrientationMode.PORTRAIT) {

            if(this.modeActivity == ActivityMode.LIST) {

                finish();

            } else {

                this.updateFragmentState(ActivityMode.LIST);
            }

        } else {

            finish();
        }
    }

    @Override
    public Date needsDate() {

        Record record = this.records.get(this.selectedRecord);

        return record.getDate();
    }

    @Override
    public Record needRecord() {

        return this.records.get(this.selectedRecord);
    }

    @Override
    public ArrayList<Record> needRecordList() {

        return this.records;
    }

    @Override
    public void onSelectedRecord(int index) {

        this.selectedRecord = index;
        this.recordedValuesFragment.needsUpdate();
        this.recordDateFragment.needsUpdate();
        this.updateFragmentState(ActivityMode.DETAIL);
    }
}
