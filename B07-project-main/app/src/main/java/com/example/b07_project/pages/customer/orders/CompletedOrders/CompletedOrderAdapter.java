package com.example.b07_project.pages.customer.orders.CompletedOrders;

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
import com.example.b07_project.data_classes.completedOrder;
import com.example.b07_project.util.StoreHashTable;

import java.util.List;

public class CompletedOrderAdapter extends RecyclerView.Adapter<CompletedOrderAdapter.CompletedOrderViewHolder> {
    private List<completedOrder> completedOrderList;
    private Context context;

    public CompletedOrderAdapter(List<completedOrder> completedOrderList, Context context) {
        this.completedOrderList = completedOrderList;
        this.context= context;
    }

    @NonNull
    @Override
    public CompletedOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_customer_completed_order_item, parent, false);
        return new CompletedOrderViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CompletedOrderViewHolder holder, int position) {
        completedOrder completedOrder = completedOrderList.get(position);
        StoreHashTable order_details = completedOrder.getitems_ordered();


        holder.orderValueTextView.setText("Order Value: " + completedOrder.getOrderValue());
        holder.itemsOrderedTextView.setText("Items Ordered: " + order_details.toString());
        holder.dateOrderedTextView.setText("Date Ordered: " + completedOrder.getDateOrdered());
        holder.num_suborders.setText("Number of Suborders: "+ completedOrder.getNum_suborders());

        holder.detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("orderId", completedOrder.getOrderid());
                Navigation.findNavController(view).navigate(R.id.action_completedOrderActivity_to_completedOrdersSingleFragment, bundle);
            }
        });
    }


    @Override
    public int getItemCount() {
        return completedOrderList.size();
    }

    public static class CompletedOrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderValueTextView;
        TextView itemsOrderedTextView;
        TextView dateOrderedTextView;
        TextView num_suborders;
        Button detailButton;

        public CompletedOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderValueTextView = itemView.findViewById(R.id.orderValue);
            itemsOrderedTextView = itemView.findViewById(R.id.itemsOrdered);
            dateOrderedTextView = itemView.findViewById(R.id.dateordered);
            num_suborders = itemView.findViewById(R.id.num_suborders);
            detailButton = itemView.findViewById(R.id.detail_button_completed);
        }
    }

    private String joinItems(String[] items) {
        StringBuilder builder = new StringBuilder();
        for (String item : items) {
            builder.append(item).append(", ");
        }
        return builder.substring(0, builder.length() - 2); // Remove the trailing comma and space
    }
}
