package com.example.b07_project.pages.customer.orders.PendingOrders;

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

public class PendingOrder_Single_Adapter extends RecyclerView.Adapter<PendingOrder_Single_Adapter.PendingOrderViewHolder>{
    private List<ProductOrderData> pendingOrderList;

    public PendingOrder_Single_Adapter(List<ProductOrderData> pendingOrderList) {
        this.pendingOrderList = pendingOrderList;
    }

    @NonNull
    @Override
    public PendingOrder_Single_Adapter.PendingOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_customer_pending_order_detail_layout, parent, false);
        return new PendingOrder_Single_Adapter.PendingOrderViewHolder(view);
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PendingOrder_Single_Adapter.PendingOrderViewHolder holder, int position) {

            ProductOrderData pendingOrderDetail = pendingOrderList.get(position);

            // Set the status and order total values with appropriate labels
            String item_name  = pendingOrderDetail.getProductName();
            String store_name = pendingOrderDetail.getStoreName();
            String price = pendingOrderDetail.getProductPriceString();
            String quantity  = String.valueOf(pendingOrderDetail.getQuantity());
            //Arrays.toString(statusValue)
            holder.itemName.setText(item_name);
            holder.itemStore.setText(store_name);
            holder.itemQuantity.setText(quantity);
            holder.itemPrice.setText(price);

            // Set the date ordered and number of suborders values with appropriate labels

        }



    @Override
    public int getItemCount() {
        return pendingOrderList.size();
    }


    public class PendingOrderViewHolder extends RecyclerView.ViewHolder {

        public TextView itemName, itemStore, itemPrice, itemQuantity;

        public PendingOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemStore = itemView.findViewById(R.id.itemStore);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemQuantity = itemView.findViewById(R.id.itemQuantity);
        }
    }
}


