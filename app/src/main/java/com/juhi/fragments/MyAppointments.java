package com.juhi.fragments;

import android.app.MediaRouteButton;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.juhi.R;
import com.juhi.Util.AppointmentItemListener;
import com.juhi.Util.DateProvider;
import com.juhi.Util.OnAppointmentItemClickListener;
import com.juhi.Util.OnBeauticianActionChangeListner;
import com.juhi.adapter.AppointmentsListAdapter;
import com.juhi.model.Appointment;
import com.juhi.model.Product;
import com.juhi.ui.ChooseSlot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class MyAppointments extends Fragment implements OnBeauticianActionChangeListner, EventListener<QuerySnapshot>, AppointmentItemListener {
    private static final String TAG = "MyAppointments";
    List<Appointment> appointmentList = new ArrayList<>();
    RecyclerView recyclerView;
    AppointmentsListAdapter appointmentsListAdapter;
    FirebaseFirestore db;
    FirebaseAuth auth;
    CollectionReference collectionReference;
    LottieAnimationView lottieAnimationView;
    TextView emptyTextview;
    private boolean isBeautician;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        firestore.setFirestoreSettings(settings);
        appointmentsListAdapter = new AppointmentsListAdapter(getActivity(), appointmentList, this, this);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("beautycart", MODE_PRIVATE);
        isBeautician = sharedPreferences.getBoolean("usertype", false);
        Log.d(TAG, "onCreate: " + isBeautician);
        collectionReference = db.collection("ListOfAppointments");
        collectionReference.addSnapshotListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

//    private void getAppointments() {
//        progressBar.setVisibility(View.VISIBLE);
//        if (!appointmentList.isEmpty()) {
//            appointmentList.clear();
//        }
//
//        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                QuerySnapshot queryDocumentSnapshots = task.getResult();
//                ;
//                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
//                    String ref = snapshot.getId();
//                    Appointment appointment = snapshot.toObject(Appointment.class);
//                    if (!isBeautician) {
//                        if (ref.contains(auth.getUid())) {
//                            appointment.setBeautican(isBeautician);
//                            appointmentList.add(appointment);
//                        }
//                    } else {
//                        appointment.setBeautican(isBeautician);
//                        if (appointment.getStatus_code() != 100) {
//
//                            String dateAndMonth = DateProvider.getCurrentTimeStamp();
//                            Log.d(TAG, "onComplete: " + dateAndMonth);
//                            Log.d(TAG, "onComplete: " + appointment.getDateMonth());
//                            if (dateAndMonth.equals(appointment.getDateMonth())) {
//                                appointment.setDepartureDate(true);
//                            }
//
//                            appointmentList.add(appointment);
//                        }
//
//                    }
//                }
//                progressBar.setVisibility(View.GONE);
//                appointmentsListAdapter.notifyDataSetChanged();
//
//
//            }
//        });
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_appointments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.Appointments_r_view);
        progressBar = view.findViewById(R.id.Appointments_progress_bar);
        lottieAnimationView = view.findViewById(R.id.Appointments_empty_img);
        emptyTextview = view.findViewById(R.id.Appointments_empty_text);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(appointmentsListAdapter);
//        if (isBeautician)
//            getAppointments();
    }


    @Override
    public void onBeauticianAccept(boolean state, int adapterPosition) {
        int statusCode = 0;

        if (state) {
            statusCode = 101;
            collectionReference.document(appointmentList.get(adapterPosition).getPath()).update("status_code", statusCode);
            Appointment appointment = appointmentList.get(adapterPosition);
            appointment.setStatus_code(101);
            appointmentList.set(adapterPosition, appointment);

            appointmentsListAdapter.notifyItemChanged(adapterPosition);

        } else {
            statusCode = 100;
            collectionReference.document(appointmentList.get(adapterPosition).getPath()).update("status_code", statusCode);

            appointmentList.remove(adapterPosition);
            appointmentsListAdapter.notifyItemRemoved(adapterPosition);

        }


    }

    @Override
    public void onBeauticianUpdateStatus(final int statusCode, final int adapterPosition) {
        if (statusCode > 103) {
            return;
        }
        Log.d(TAG, "onBeauticianUpdateStatus: " + statusCode);
        collectionReference.document(appointmentList.get(adapterPosition).getPath()).update("status_code", statusCode).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Appointment appointment = appointmentList.get(adapterPosition);
                appointment.setStatus_code(statusCode);
                appointmentsListAdapter.notifyItemChanged(adapterPosition);
            }
        });
    }

    @Override
    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {

        progressBar.setVisibility(View.VISIBLE);
        if (e != null) {
            Log.w(TAG, "Listen failed.", e);
            return;
        }


        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
            if (!appointmentList.isEmpty()) {
                appointmentList.clear();
            }

            String ref = snapshot.getId();
            Appointment appointment = snapshot.toObject(Appointment.class);
            if (!isBeautician) {
                if (ref.contains(auth.getUid())) {
                    appointment.setBeautican(isBeautician);
                    appointmentList.add(appointment);
                }
            } else {
                appointment.setBeautican(isBeautician);
                if (appointment.getStatus_code() != 100) {

                    String dateAndMonth = DateProvider.getCurrentTimeStamp();
                    Log.d(TAG, "onComplete: " + dateAndMonth);
                    Log.d(TAG, "onComplete: " + appointment.getDateMonth());
                    if (dateAndMonth.equals(appointment.getDateMonth())) {
                        appointment.setDepartureDate(true);
                    }

                    appointmentList.add(appointment);
                }

            }
        }
        progressBar.setVisibility(View.GONE);
        appointmentsListAdapter.notifyDataSetChanged();

    }

    @Override
    public void onDatasetChanged(int size) {
        if (size == 0) {
            lottieAnimationView.setVisibility(View.VISIBLE);
            emptyTextview.setVisibility(View.VISIBLE);
        } else {
            lottieAnimationView.setVisibility(View.GONE);
            emptyTextview.setVisibility(View.GONE);
        }
    }
}
