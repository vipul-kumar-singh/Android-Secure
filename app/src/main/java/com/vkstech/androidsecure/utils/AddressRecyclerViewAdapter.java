package com.vkstech.androidsecure.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vkstech.androidsecure.R;
import com.vkstech.androidsecure.dto.Address;

import java.util.List;

public class AddressRecyclerViewAdapter extends RecyclerView.Adapter<AddressRecyclerViewAdapter.ViewHolder> {

    private List<Address> addressList;
    private Context context;

    public AddressRecyclerViewAdapter(List<Address> addressList, Context context) {
        this.addressList = addressList;
        this.context = context;
    }

    public void updateDataSet(List<Address> addressList){
        this.addressList = addressList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.address_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Address address = addressList.get(position);

        holder.houseNumber.setText(address.getHouseNumber().toString());
        holder.area.setText(address.getArea());
        holder.city.setText(address.getCity());
        holder.state.setText(address.getState());
        holder.pinCode.setText(address.getPinCode().toString());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "You clicked " + address.getArea(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView houseNumber;
        public TextView area;
        public TextView city;
        public TextView state;
        public TextView pinCode;

        public LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            houseNumber = itemView.findViewById(R.id.houseNumber);
            area = itemView.findViewById(R.id.area);
            city = itemView.findViewById(R.id.city);
            state = itemView.findViewById(R.id.state);
            pinCode = itemView.findViewById(R.id.pinCode);

            linearLayout = itemView.findViewById(R.id.cardLinearLayout);

        }
    }

}
