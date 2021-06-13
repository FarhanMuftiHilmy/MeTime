package com.rechit.metime.view.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rechit.metime.R;
import com.rechit.metime.model.Time;
import com.rechit.metime.vievmodel.TimeViewModel;
import com.rechit.metime.view.adapter.TimeAdapter;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import timerx.Stopwatch;
import timerx.StopwatchBuilder;

public class TimeFragment extends Fragment implements TimeAdapter.TimeAdapterCallback {

    private EditText edtTitleTime;
    private TextView timeProgres, activityProgres;
    private Button btnStart;
    private TimeViewModel tvm;

    public TimeFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        btnStart = view.findViewById(R.id.btn_start);
        edtTitleTime = view.findViewById(R.id.edt_time_activity);
        timeProgres= view.findViewById(R.id.time_in_progres);
        activityProgres = view.findViewById(R.id.activity_in_progress);

        RecyclerView rvListTime = (RecyclerView) view.findViewById(R.id.rv_time_activity);
        rvListTime.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        TimeAdapter adapter = new TimeAdapter(this);
        rvListTime.setAdapter(adapter);

        tvm = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(TimeViewModel.class);
        tvm.getTimeListLiveData().observe(getViewLifecycleOwner(), adapter::setData);

        if (firebaseUser !=null){
            tvm.query();
            tvm.addSnapshotListener();
        }

        Stopwatch stopwatch = new StopwatchBuilder()
                .startFormat("HH:MM:SS")
                .onTick(time -> timeProgres.setText(time))
                .build();

        btnStart.setOnClickListener(v -> {
            String title = edtTitleTime.getText().toString();

            if (btnStart.getText().toString().equals("Start")){
                if (title.isEmpty()){
                    Toast.makeText(getActivity(), "pastikan title terisi", Toast.LENGTH_SHORT).show();
                    return;
                }

                activityProgres.setText(title);
                stopwatch.reset();
                stopwatch.start();
                btnStart.setText("Stop");

            }else{
                btnStart.setText("Start");
                stopwatch.stop();
                activityProgres.setText("");
                edtTitleTime.setText("");

                Time time = new Time();
                time.setId(UUID.randomUUID().toString());
                time.setTitleTime(title);
                time.setTrackingTime(timeProgres.getText().toString());
                time.setTimeStamps(Timestamp.now());

                tvm.insert(time);
                timeProgres.setText("00:00:00");
            }
        });
    }

    @Override
    public void onTimeDeleted(Time time) {
        tvm.delete(time);
    }
}