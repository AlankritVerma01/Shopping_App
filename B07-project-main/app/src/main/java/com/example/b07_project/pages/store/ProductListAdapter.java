package com.example.b07_project.pages.store;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07_project.ImageRepository;
import com.example.b07_project.R;
import com.example.b07_project.mvp_interface.ProductListPresenter;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ProductListPresenter presenter;
        ImageView imageView;
        TextView nameView;
        TextView priceView;

        public ViewHolder(@NonNull View itemView, ProductListPresenter presenter) {
            super(itemView);
            itemView.setOnClickListener(this);

            imageView = itemView.findViewById(R.id.product_preview);
            nameView = itemView.findViewById(R.id.product_name);
            priceView = itemView.findViewById(R.id.product_price);
            this.presenter = presenter;
        }

        @Override
        public void onClick(View view) { presenter.onProductClicked(getAdapterPosition()); }
    }

    private final ProductListPresenter presenter;

    public ProductListAdapter(ProductListPresenter presenter) {
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public ProductListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                                            .inflate(R.layout.view_product_list_element, parent, false),
                              presenter);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Set text inside list elements
        holder.nameView.setText(presenter.getProductName(position));
        holder.priceView.setText(presenter.getProductDisplayedPrice(position));

        presenter.getProductPreview(position,
                bitmap -> {
                    if (bitmap != null) {
                        holder.imageView.setImageBitmap(bitmap);
                    } else {
                        holder.imageView.setImageResource(ImageRepository.getDefaultImageResource());
                    }
                },
                exception -> Log.e("ProductListAdapter", "Exception: ", exception));
    }

    @Override
    public int getItemCount() {
        return presenter.getNumProducts();
    }
}
