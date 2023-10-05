package com.example.b07_project.pages.store.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07_project.R;
import com.example.b07_project.data_classes.OrderData;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<OrderData> orderList;
    private OnItemClickListener itemClickListener;
    private boolean showingCompletedOrders = false;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onMoveToCompletedClick(View view, int position);
    }

    public OrderAdapter(List<OrderData> orderList) {
        this.orderList = orderList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setShowCompletedOrders(boolean showCompleted) {
        showingCompletedOrders = showCompleted;
    }

    public boolean isShowingCompletedOrders() {
        return showingCompletedOrders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_order, parent, false);
        return new OrderViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderData order = orderList.get(position);
        if (order != null) {
            holder.textViewOrderDate.setText(order.getDate());
            holder.textViewCustomerName.setText("Order ID: " + order.getOrderId());
            if (showingCompletedOrders) {
                holder.buttonMoveToCompleted.setVisibility(View.GONE);
                holder.textViewStatus.setText("Completed");
            } else {
                holder.buttonMoveToCompleted.setVisibility(View.VISIBLE);
                holder.textViewStatus.setText("Pending");
            }
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewOrderDate;
        private TextView textViewCustomerName;
        private TextView textViewStatus;
        private Button buttonMoveToCompleted;

        public OrderViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            textViewOrderDate = itemView.findViewById(R.id.textViewOrderDate);
            textViewCustomerName = itemView.findViewById(R.id.textViewCustomerName);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            buttonMoveToCompleted = itemView.findViewById(R.id.buttonMoveToCompleted);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(v, position);
                        }
                    }
                }
            });

            buttonMoveToCompleted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onMoveToCompletedClick(v, position);
                        }
                    }
                }
            });
        }
    }
}
