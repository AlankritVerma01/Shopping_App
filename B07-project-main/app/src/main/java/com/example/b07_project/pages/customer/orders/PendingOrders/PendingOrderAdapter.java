package com.example.b07_project.pages.customer.orders.PendingOrders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07_project.R;
import com.example.b07_project.data_classes.PendingOrder;
import com.example.b07_project.util.StoreHashTable;

import java.util.List;

public class PendingOrderAdapter extends RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder> {

    private Context context;
    private List<PendingOrder> pendingOrderList;

    public PendingOrderAdapter(List<PendingOrder> pendingOrderList, Context context) {
        this.pendingOrderList = pendingOrderList;
        this.context= context;
    }

    @NonNull
    @Override
    public PendingOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_customer_pending_order_item, parent, false);
        return new PendingOrderViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PendingOrderViewHolder holder, int position) {
        PendingOrder pendingOrder = pendingOrderList.get(position);

        // Set the status and order total values with appropriate labels
        StoreHashTable statusValue = pendingOrder.getpending();
        String orderTotalValue = pendingOrder.getOrderValue();
        //Arrays.toString(statusValue)
        holder.textViewOrderNumber.setText("Pending Items : " + statusValue.toString() );
        holder.textViewOrderValue.setText("Order Total: " + orderTotalValue);

        // Set the date ordered and number of suborders values with appropriate labels
        String dateOrderedValue = pendingOrder.getDateOrdered();
        String numSubordersValue = pendingOrder.getNumItems();
        holder.textViewDateOrdered.setText("Date Ordered: " + dateOrderedValue);
        holder.textViewNumSuborders.setText("Number of Suborders: " + numSubordersValue);
        holder.detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("orderId", pendingOrder.getOrderid());
                // Call the function to navigate to the second nested RecyclerVie
                        Navigation.findNavController(v).navigate(R.id.action_pendingOrderAllFragment_to_pendingOrderSingleFragment,bundle);
                    }
                });

            }

    @Override
    public int getItemCount() {
        return pendingOrderList.size();
    }

    public static class PendingOrderViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewOrderNumber, textViewNumSuborders, textViewOrderValue, textViewDateOrdered;
        public Button detailButton;


        public PendingOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewOrderNumber = itemView.findViewById(R.id.textViewpending);
            textViewNumSuborders = itemView.findViewById(R.id.textViewNumSuborders);
            textViewOrderValue = itemView.findViewById(R.id.textViewOrderValue);
            textViewDateOrdered = itemView.findViewById(R.id.textViewDateOrdered);
             detailButton = itemView.findViewById(R.id.detail_button);
        }
    }

}
