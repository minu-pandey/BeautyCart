package com.juhi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.juhi.R;
import com.juhi.Util.AppointmentItemListener;
import com.juhi.Util.DateProvider;
import com.juhi.Util.OnBeauticianActionChangeListner;
import com.juhi.model.Appointment;
import com.juhi.model.Product;

import java.util.Date;
import java.util.List;

public class AppointmentsListAdapter extends RecyclerView.Adapter<AppointmentsListAdapter.AppointmentBaseViewHolder> {
    public static final String items_101[] = {"Beautician has been departure", "Beautician has been arrived to your destination", "Beautician has finished his work"};
    public static final String items_102[] = {"Beautician has been arrived to your destination", "Beautician has finished his work"};
    public static final String items_103[] = {"Beautician has finished his work"};
    private static final int IS_USER = 101;
    private static final int IS_BEAUTICIAN = 103;
    private OnBeauticianActionChangeListner onBeauticianActionChangeListner;
    private AppointmentItemListener appointmentItemListener;
    private Context context;
    private List<Appointment> appointmentList;


    public AppointmentsListAdapter(Context context, List<Appointment> appointmentList, OnBeauticianActionChangeListner onBeauticianActionChangeListner, AppointmentItemListener appointmentItemListener) {
        this.context = context;
        this.appointmentList = appointmentList;
        this.onBeauticianActionChangeListner = onBeauticianActionChangeListner;
        this.appointmentItemListener = appointmentItemListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (appointmentList.get(position).isBeautican()) {
            return IS_BEAUTICIAN;
        } else {
            return IS_USER;
        }
    }

    @NonNull
    @Override
    public AppointmentBaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == IS_USER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.r_container_appointment_is_users, parent, false);
            return new AppointmentUserViewHolder(view);

        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.r_container_appointment_is_beautician, parent, false);
            return new AppointmentBeauticianViewHolder(view, onBeauticianActionChangeListner, context);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentBaseViewHolder holder, int position) {
        Appointment appointment = appointmentList.get(position);
        if (getItemViewType(position) == IS_USER) {
            final AppointmentUserViewHolder appointmentUserViewHolder = (AppointmentUserViewHolder) holder;

            appointmentUserViewHolder.userName.setText(appointment.getUsername());
            appointmentUserViewHolder.userAddress.setText(appointment.getAddress());
            appointmentUserViewHolder.userPhoneNumber.setText(appointment.getNumber());
            appointmentUserViewHolder.userEmail.setText(appointment.getEmail());
            appointmentUserViewHolder.timeStamp.setText(appointment.getTime());
            appointmentUserViewHolder.orderId.setText(String.valueOf(appointment.getOrderId()));
            appointmentUserViewHolder.shippingDetails.setSelected(true);
appointmentUserViewHolder.userDetails.setVisibility(View.GONE);
            appointmentUserViewHolder.timeslot.setText(appointment.getTimeSlot());
            appointmentUserViewHolder.totalAmount.setText("₹ " + appointment.getTotal_amount());

            switch (appointment.getStatus_code()) {

                case 100:
                    appointmentUserViewHolder.appointmentinfo.setText("Appointment has been cancelled");

                    appointmentUserViewHolder.appointmentinfo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dot_red, 0, 0, 0);

                    break;
                case 101:
                    appointmentUserViewHolder.appointmentinfo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dot_green, 0, 0, 0);
                    appointmentUserViewHolder.appointmentinfo.setText("Appointment has been confirmed");
                    break;
                case 102:
                    appointmentUserViewHolder.appointmentinfo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dot_green, 0, 0, 0);
                    appointmentUserViewHolder.appointmentinfo.setText("Beautician has been departure");
                    break;
                case 103:
                    appointmentUserViewHolder.appointmentinfo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check, 0, 0, 0);


                    appointmentUserViewHolder.appointmentinfo.setText("Beautician has finished his work");
                    break;
            }

            String listOfProducts = "";
            for (Product s : appointment.getSelected_Products()) {
                listOfProducts += s.getmTitle() + " x " + s.getmCount() + ",";
            }
            listOfProducts = listOfProducts.substring(0, listOfProducts.length() - 1);
            appointmentUserViewHolder.listProducts.setText(listOfProducts);
            appointmentUserViewHolder.shippingDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (appointmentUserViewHolder.userDetails.getVisibility() == View.VISIBLE) {
                        appointmentUserViewHolder.shippingDetails.setSelected(true);
                        appointmentUserViewHolder.userDetails.setVisibility(View.GONE);
                        appointmentUserViewHolder.shippingDetails.setText("Show more details");
                    } else {
                        appointmentUserViewHolder.userDetails.setVisibility(View.VISIBLE);
                        appointmentUserViewHolder.shippingDetails.setSelected(false);
                        appointmentUserViewHolder.shippingDetails.setText("Show less details");
                    }
                }
            });

        } else {
            final AppointmentBeauticianViewHolder appointmentBeauticianViewHolder = (AppointmentBeauticianViewHolder) holder;

            appointmentBeauticianViewHolder.userName.setText(appointment.getUsername());
            appointmentBeauticianViewHolder.userAddress.setText(appointment.getAddress());
            appointmentBeauticianViewHolder.userPhoneNumber.setText(appointment.getNumber());
            appointmentBeauticianViewHolder.userEmail.setText(appointment.getEmail());
            appointmentBeauticianViewHolder.timeStamp.setText(appointment.getTime());
            appointmentBeauticianViewHolder.orderId.setText(String.valueOf(appointment.getOrderId()));
            appointmentBeauticianViewHolder.totalAmount.setText("₹ " + appointment.getTotal_amount());
            appointmentBeauticianViewHolder.timeslot.setText(appointment.getTimeSlot());

            appointmentBeauticianViewHolder.statusCode = appointment.getStatus_code();
            String listOfProducts = "";
            String date = appointment.getTime().substring(0, 6);

            switch (appointment.getStatus_code()) {
                case 101:
                    if (!appointment.isDepartureDate()) {
                        appointmentBeauticianViewHolder.updateInfo.setVisibility(View.VISIBLE);
                        appointmentBeauticianViewHolder.reject.setVisibility(View.GONE);
                        appointmentBeauticianViewHolder.accept.setVisibility(View.GONE);
                        appointmentBeauticianViewHolder.departureinfo.setVisibility(View.VISIBLE);
                        appointmentBeauticianViewHolder.updateInfo.setBackgroundColor(context.getResources().getColor(R.color.departure_waiting));
                        appointmentBeauticianViewHolder.updateInfo.setOnClickListener(null);
                        appointmentBeauticianViewHolder.updateInfo.setText("Departure on " + date);
                    } else {
                        appointmentBeauticianViewHolder.updateInfo.setVisibility(View.VISIBLE);
                        appointmentBeauticianViewHolder.reject.setVisibility(View.GONE);
                        appointmentBeauticianViewHolder.accept.setVisibility(View.GONE);
                        appointmentBeauticianViewHolder.departureinfo.setVisibility(View.GONE);
                        appointmentBeauticianViewHolder.updateInfo.setBackgroundColor(context.getResources().getColor(R.color.departure_ready));
                        appointmentBeauticianViewHolder.updateInfo.setText("Departure  now");
                    }

                    break;
                case 102:
                    appointmentBeauticianViewHolder.reject.setVisibility(View.GONE);
                    appointmentBeauticianViewHolder.accept.setVisibility(View.GONE);
                    appointmentBeauticianViewHolder.departureinfo.setVisibility(View.GONE);
                    appointmentBeauticianViewHolder.updateInfo.setVisibility(View.VISIBLE);
                    appointmentBeauticianViewHolder.updateInfo.setBackgroundColor(context.getResources().getColor(R.color.departure_ready));
                    appointmentBeauticianViewHolder.updateInfo.setText("Finished");
                    break;
                case 103:
                    appointmentBeauticianViewHolder.reject.setVisibility(View.GONE);
                    appointmentBeauticianViewHolder.accept.setVisibility(View.GONE);
                    appointmentBeauticianViewHolder.departureinfo.setVisibility(View.GONE);
                    appointmentBeauticianViewHolder.updateInfo.setVisibility(View.VISIBLE);
                    appointmentBeauticianViewHolder.updateInfo.setBackgroundColor(context.getResources().getColor(R.color.departure_ready));
                    appointmentBeauticianViewHolder.updateInfo.setText("Payment received");

                    break;
            }

            appointmentBeauticianViewHolder.shippingDetails.setSelected(true);
            for (Product s : appointment.getSelected_Products()) {
                listOfProducts += s.getmTitle() + " x " + s.getmCount() + ",";
            }
            listOfProducts=listOfProducts.substring(0, listOfProducts.length() - 1);
            appointmentBeauticianViewHolder.listProducts.setText(listOfProducts);

            appointmentBeauticianViewHolder.shippingDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (appointmentBeauticianViewHolder.userDetails.getVisibility() == View.VISIBLE) {
                        appointmentBeauticianViewHolder.shippingDetails.setSelected(true);
                        appointmentBeauticianViewHolder.userDetails.setVisibility(View.GONE);
                        appointmentBeauticianViewHolder.shippingDetails.setText("Show more details");
                    } else {
                        appointmentBeauticianViewHolder.userDetails.setVisibility(View.VISIBLE);
                        appointmentBeauticianViewHolder.shippingDetails.setSelected(false);
                        appointmentBeauticianViewHolder.shippingDetails.setText("Show less details");
                    }
                }
            });

        }
    }


    @Override
    public int getItemCount() {
        appointmentItemListener.onDatasetChanged(appointmentList.size());
        return appointmentList.size();
    }

    class AppointmentBaseViewHolder extends RecyclerView.ViewHolder {

        public AppointmentBaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


    class AppointmentUserViewHolder extends AppointmentBaseViewHolder {
        TextView packageName;
        TextView listProducts;
        TextView timeStamp;
        TextView totalAmount;
        TextView appointmentinfo;
        TextView userName;
        TextView userEmail;
        TextView userPhoneNumber;
        TextView userAddress;
        LinearLayout userDetails;
        TextView shippingDetails;
        TextView timeslot;
        TextView orderId;


        public AppointmentUserViewHolder(@NonNull View itemView) {
            super(itemView);
            packageName = itemView.findViewById(R.id.Appointments_r_package_name);
            listProducts = itemView.findViewById(R.id.Appointments_r_list_of_products);
            orderId = itemView.findViewById(R.id.Appointments_r_order_id);
            timeStamp = itemView.findViewById(R.id.Appointments_r_order_date_time);
            totalAmount = itemView.findViewById(R.id.Appointments_r_total_amount);
            appointmentinfo = itemView.findViewById(R.id.Appointments_r_appointment_current_info);
            userName = itemView.findViewById(R.id.Appointments_r_user_username);
            userEmail = itemView.findViewById(R.id.Appointments_r_user_email);
            userPhoneNumber = itemView.findViewById(R.id.Appointments_r_user_phone_number);
            userAddress = itemView.findViewById(R.id.Appointments_r_user_address);
            userDetails = itemView.findViewById(R.id.Appointments_r_user_details);
            timeslot = itemView.findViewById(R.id.Appointments_r_time_slot);

            shippingDetails = itemView.findViewById(R.id.Appointments_r_shippingdetails);
        }
    }

    class AppointmentBeauticianViewHolder extends AppointmentBaseViewHolder implements View.OnClickListener {
        OnBeauticianActionChangeListner onBeauticianActionChangeListner;
        TextView packageName;
        TextView listProducts;
        TextView timeStamp;
        TextView totalAmount;
        TextView appointmentStatusHolder;
        Button reject;
        Button accept;
        TextView userName;
        TextView userEmail;
        TextView userPhoneNumber;
        TextView userAddress;
        LinearLayout userDetails;
        TextView shippingDetails;
        TextView departureinfo;
        Button updateInfo;

        TextView timeslot;
        TextView orderId;
        int statusCode;
        Context context;


        public AppointmentBeauticianViewHolder(@NonNull View itemView, OnBeauticianActionChangeListner onBeauticianActionChangeListner, Context context) {
            super(itemView);
            this.onBeauticianActionChangeListner = onBeauticianActionChangeListner;
            packageName = itemView.findViewById(R.id.Appointments_r_package_name);
            listProducts = itemView.findViewById(R.id.Appointments_r_list_of_products);
            timeStamp = itemView.findViewById(R.id.Appointments_r_order_date_time);
            totalAmount = itemView.findViewById(R.id.Appointments_r_total_amount);
            reject = itemView.findViewById(R.id.Appointments_r_reject);
            departureinfo = itemView.findViewById(R.id.Appointments_r_departure_info);
            accept = itemView.findViewById(R.id.Appointments_r_accept);
            timeslot = itemView.findViewById(R.id.Appointments_r_time_slot);
            userName = itemView.findViewById(R.id.Appointments_r_user_username);
            userEmail = itemView.findViewById(R.id.Appointments_r_user_email);
            userPhoneNumber = itemView.findViewById(R.id.Appointments_r_user_phone_number);
            userAddress = itemView.findViewById(R.id.Appointments_r_user_address);
            orderId = itemView.findViewById(R.id.Appointments_r_order_id);
            userDetails = itemView.findViewById(R.id.Appointments_r_user_details);
            shippingDetails = itemView.findViewById(R.id.Appointments_r_shippingdetails);
            updateInfo = itemView.findViewById(R.id.Appointments_r_update);
            this.context = context;
            reject.setOnClickListener(this);
            accept.setOnClickListener(this);
            updateInfo.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int statusCode = appointmentList.get(getAdapterPosition()).getStatus_code();
            String dateAndMonth = DateProvider.getCurrentTimeStamp();
            Appointment appointment = appointmentList.get(getAdapterPosition());
            switch (view.getId()) {
                case R.id.Appointments_r_accept:
                    onBeauticianActionChangeListner.onBeauticianAccept(false, getAdapterPosition());
                    break;
                case R.id.Appointments_r_reject:


                    accept.setVisibility(View.GONE);
                    reject.setVisibility(View.GONE);
                    onBeauticianActionChangeListner.onBeauticianAccept(true, getAdapterPosition());


                    break;
                case R.id.Appointments_r_update:


                    if (dateAndMonth.equals(appointment.getDateMonth())) {
                        appointment.setDepartureDate(true);
                    }

                    appointmentList.set(getAdapterPosition(), appointment);

                    appointmentList.get(getAdapterPosition()).setStatus_code(statusCode++);
                    onBeauticianActionChangeListner.onBeauticianUpdateStatus(statusCode++, getAdapterPosition());
                    switch (statusCode) {
                        case 101:
                            if (!appointmentList.get(getAdapterPosition()).isDepartureDate()) {
                                updateInfo.setVisibility(View.VISIBLE);
                                reject.setVisibility(View.GONE);
                                accept.setVisibility(View.GONE);
                                departureinfo.setVisibility(View.VISIBLE);
                                updateInfo.setBackgroundColor(context.getResources().getColor(R.color.departure_waiting));
                                updateInfo.setOnClickListener(null);
                                String date = appointmentList.get(getAdapterPosition()).getTime().substring(0, 6);
                                updateInfo.setText("Departure on " + date);
                            } else {
                                updateInfo.setVisibility(View.VISIBLE);
                                reject.setVisibility(View.GONE);
                                accept.setVisibility(View.GONE);
                                departureinfo.setVisibility(View.GONE);
                                updateInfo.setBackgroundColor(context.getResources().getColor(R.color.departure_ready));
                                updateInfo.setText("Departure  now");
                            }

                            break;
                        case 102:
                            reject.setVisibility(View.GONE);
                            accept.setVisibility(View.GONE);
                            departureinfo.setVisibility(View.GONE);
                            updateInfo.setBackgroundColor(context.getResources().getColor(R.color.departure_ready));
                            updateInfo.setText("Finished");
                            break;
                        case 103:
                            reject.setVisibility(View.GONE);
                            accept.setVisibility(View.GONE);
                            departureinfo.setVisibility(View.GONE);
                            updateInfo.setBackgroundColor(context.getResources().getColor(R.color.departure_ready));
                            updateInfo.setText("Payment received");

                            break;
                    }

            }


        }


    }

}
