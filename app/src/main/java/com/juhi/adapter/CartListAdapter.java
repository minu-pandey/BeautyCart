package com.juhi.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.juhi.R;
import com.juhi.Util.OnCartItemModified;
import com.juhi.model.Product;

import java.util.ArrayList;
import java.util.List;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.CartViewHolder> {
    private List<Product> packageItemList;
    private List<Product> packageItemListTemp = new ArrayList<>();
    private OnCartItemModified onCartItemRemoved;
    public CartListAdapter(List<Product> packageItemList, OnCartItemModified onCartItemRemoved) {
        this.packageItemList = packageItemList;
        packageItemListTemp.addAll(packageItemList);

        this.onCartItemRemoved=onCartItemRemoved;

    }
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.r_container_cart, parent, false);
        return new CartViewHolder(view, onCartItemRemoved);


    }
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = packageItemList.get(position);
        CartViewHolder cartViewHolder = (CartViewHolder) holder;
        cartViewHolder.originalPrice.setText("₹ " + product.getmActualPrice());
        cartViewHolder.productTitle.setText(product.getmTitle());
        cartViewHolder.countView.setText("0");
        cartViewHolder.onBind(position);
    }
    @Override
    public int getItemCount() {
        return packageItemList.size();
    }
    class ProductBaseViewHolder extends RecyclerView.ViewHolder {

        public ProductBaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    class CartViewHolder extends ProductBaseViewHolder implements View.OnClickListener {

        TextView originalPrice;

        TextView productTitle;

        TextView countView;

        ImageView add;

        ImageView remove;

        Button delete;

        OnCartItemModified onCartItemRemoved;


        public CartViewHolder(@NonNull View itemView, OnCartItemModified onCartItemRemoved) {
            super(itemView);

            originalPrice = itemView.findViewById(R.id.Cart_price);
            productTitle = itemView.findViewById(R.id.Cart_title);
            delete=itemView.findViewById(R.id.Cart_delete);
            countView = itemView.findViewById(R.id.Cart_count);
            add = itemView.findViewById(R.id.Cart_add);
            remove = itemView.findViewById(R.id.Cart_remove);
            add.setOnClickListener(this);
            remove.setOnClickListener(this);
            delete.setOnClickListener(this);
            this.onCartItemRemoved=onCartItemRemoved;

            countView.setTag(0);
        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            Product product = packageItemList.get(position);
            int count = product.getmCount();
            boolean addOrRemove = false;
            switch (view.getId()) {
                case R.id.Cart_add:
                    countView.setTag(count++);
                    product.setmCount(count);
                    packageItemList.set(position, product);
                    packageItemListTemp.add(product);
                    countView.setText(String.valueOf(product.getmCount()));
                    onCartItemRemoved.onItemModified(getAdapterPosition(),true,product.getmCount(),product);
                    addOrRemove = true;
                    break;
                case R.id.Cart_remove:
                    if (count > 1) {
                        countView.setTag(count--);
                        product.setmCount(count);
                        packageItemList.set(position, product);
                        packageItemListTemp.set(position, product);
                        countView.setText(String.valueOf(product.getmCount()));
                        addOrRemove = false;
                        onCartItemRemoved.onItemModified(getAdapterPosition(),false,product.getmCount(),product);
                    }
                    else
                    {
                        onCartItemRemoved.onItemRemoved(getAdapterPosition());
                    }
                    break;
                case R.id.Cart_delete:
                    onCartItemRemoved.onItemRemoved(getAdapterPosition());
                    break;


            }



        }

        public void onBind(int position) {
            Product product = packageItemList.get(position);

            countView.setText(String.valueOf(product.getmCount()));
        }
    }
}
