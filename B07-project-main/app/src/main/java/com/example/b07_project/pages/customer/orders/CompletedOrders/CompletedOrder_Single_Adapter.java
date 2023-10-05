package com.example.b07_project.pages.customer.orders.CompletedOrders;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07_project.R;
import com.example.b07_project.data_classes.ProductOrderData;

import java.util.List;

public class CompletedOrder_Single_Adapter extends RecyclerView.Adapter<CompletedOrder_Single_Adapter.CompletedOrderViewHolder>{
    private List<ProductOrderData> completedOrderList;

    public CompletedOrder_Single_Adapter(List<ProductOrderData> completedOrderList) {
        this.completedOrderList = completedOrderList;
    }

    @NonNull
    @Override
    public CompletedOrder_Single_Adapter.CompletedOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_customer_completed_order_detail_layout, parent, false);
        return new CompletedOrder_Single_Adapter.CompletedOrderViewHolder(view);
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CompletedOrder_Single_Adapter.CompletedOrderViewHolder holder, int position) {

        ProductOrderData completedOrderDetail = completedOrderList.get(position);

        // Set the status and order total values with appropriate labels
        String item_name  = completedOrderDetail.getProductName();
        String store_name = completedOrderDetail.getStoreName();
        String price = completedOrderDetail.getProductPriceString();
        String quantity  = String.valueOf(completedOrderDetail.getQuantity());
        //Arrays.toString(statusValue)
        holder.itemName.setText(item_name);
        holder.itemStore.setText(store_name);
        holder.itemQuantity.setText(quantity);
        holder.itemPrice.setText(price);

        // Set the date ordered and number of suborders values with appropriate labels

    }



    @Override
    public int getItemCount() {
        return completedOrderList.size();
    }


    public class CompletedOrderViewHolder extends RecyclerView.ViewHolder {

        public TextView itemName, itemStore, itemPrice, itemQuantity;

        public CompletedOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemStore = itemView.findViewById(R.id.itemStore);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemQuantity = itemView.findViewById(R.id.itemQuantity);
        }
    }
}
