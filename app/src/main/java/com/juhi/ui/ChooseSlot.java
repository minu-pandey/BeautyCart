package com.juhi.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.juhi.R;
import com.juhi.Util.DateProvider;
import com.juhi.model.Appointment;
import com.juhi.model.Cart;
import com.juhi.model.Product;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.ThreadLocalRandom;

public class ChooseSlot extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ChooseSlot";

    final Calendar myCalendar = Calendar.getInstance();
    TextInputEditText email;
    TextInputEditText number;
    TextInputEditText address;
    TextInputEditText username;
    FirebaseFirestore db;
    Button confirmBooking;
    TextView dateView;
    Spinner time;
    FirebaseAuth auth;
    private TextInputEditText[] fields;
    private List<Product> productList = new ArrayList<>();
    private int totalPrice;
    private String timestamp;
    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            int monthtemo = monthOfYear + 1;
            if (monthtemo < 10) {
                String date = String.valueOf(dayOfMonth);

                String month = "0" + String.valueOf(monthtemo);

                month = DateProvider.getMonthFromNumber(month);
                DateFormat df = new SimpleDateFormat("h:mm a");
                String time = df.format(Calendar.getInstance().getTime());

                timestamp = date + " " + month + " " + year;
                Log.d(TAG, "onDateSet: " + timestamp + " " + year + " at " + time);
                dateView.setText(timestamp);
            } else {
                String month = String.valueOf(monthtemo);
                month = DateProvider.getMonthFromNumber(month);
                String date = String.valueOf(dayOfMonth);
                timestamp = date + " " + month + " " + year;


                Log.d(TAG, "onDateSet: " + timestamp);
                dateView.setText(timestamp);
            }
        }
    };

    public static String generateOtp(int size) {

        StringBuilder generatedToken = new StringBuilder();
        try {
            SecureRandom number = SecureRandom.getInstance("SHA1PRNG");
            for (int i = 0; i < size; i++) {
                generatedToken.append(number.nextInt(9));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return generatedToken.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_slot);
        email = findViewById(R.id.Choose_slot_input_email);
        number = findViewById(R.id.Choose_slot_input_number);
        address = findViewById(R.id.Choose_slot_input_Addrss);
        username = findViewById(R.id.Choose_slot_input_username);
        confirmBooking = findViewById(R.id.Choose_slot_confirm_booking);
        dateView = findViewById(R.id.Choose_slot_date);
        time = findViewById(R.id.Choose_slot_time);
        dateView.setOnClickListener(this);
        confirmBooking.setOnClickListener(this);
        Gson gson = new Gson();
        Cart cart = gson.fromJson(getIntent().getStringExtra("selecteditem"), Cart.class);
        totalPrice = getIntent().getIntExtra("totalprice", -1);
        productList.addAll(cart.getSelectedProducts());
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    private void setAppointment() {
        Random rnd = new Random();
        int orderId = 100000 + rnd.nextInt(900000);

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm a");
        String dateAndMonth = DateProvider.getCurrentTimeStamp();
        date.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        DateFormat df = new SimpleDateFormat("h:mm a");
        String Currenttime = df.format(Calendar.getInstance().getTime());
        timestamp = timestamp+" at " + Currenttime;
        String localTime = date.format(currentLocalTime);
        String path = auth.getUid() + localTime;
        Appointment appointment = new Appointment();
        appointment.setAddress(address.getText().toString());
        appointment.setDate(new Date());
        appointment.setUsername(username.getText().toString());
        appointment.setEmail(email.getText().toString());
        appointment.setDateMonth(dateAndMonth);
        appointment.setOrderId(orderId);
        appointment.setPath(path);

        appointment.setNumber(number.getText().toString());
        appointment.setStatus_code(0);
        appointment.setTimeSlot(time.getSelectedItem().toString());
        appointment.setTime(timestamp);
        appointment.setTotal_amount(String.valueOf(totalPrice));
        appointment.setSelected_Products(productList);
        CollectionReference collectionReference = db.collection("ListOfAppointments");
        collectionReference.document(path).set(appointment).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(ChooseSlot.this, "Appointment has been scheduled", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ChooseSlot.this, UserMainActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChooseSlot.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Choose_slot_date:
                DatePickerDialog datePickerDialog=new DatePickerDialog(ChooseSlot.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                datePickerDialog.show();
                break;

            case R.id.Choose_slot_confirm_booking:
                fields = new TextInputEditText[]{email, address, number, username};
                boolean isAllFieldsOk = verifyDetails(fields);
                if (isAllFieldsOk && timestamp == null) {
                    Toast.makeText(this, "Check your fields", Toast.LENGTH_SHORT).show();
                }
                setAppointment();
                break;
        }

    }

    private boolean verifyDetails(TextInputEditText[] fields) {
        for (TextInputEditText textInputEditText : fields) {
            if (textInputEditText.getText().toString().isEmpty())
                return true;
        }
        return false;
    }
}
