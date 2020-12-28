package com.juhi.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.juhi.R;
import com.juhi.Util.ProductListItemEditistener;
import com.juhi.Util.ProductListItemSelectListener;
import com.juhi.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductBaseViewHolder> {
    private List<Product> packageItemList;
    private List<Product> packageItemListTemp=new ArrayList<>();
    private ProductListItemSelectListener productListItemSelectListener;
    private ProductListItemEditistener productListItemEditistener;
    private Context context;
    private static final int IS_USER = 101;
    private static final int IS_BEAUTICIAN = 103;

    public ProductListAdapter(Context mContext, List<Product> packageItemList, ProductListItemSelectListener productListItemSelectListener, ProductListItemEditistener productListItemEditistener) {
        this.packageItemList = packageItemList;
        this.context = mContext;
        this.productListItemEditistener = productListItemEditistener;
        this.productListItemSelectListener = productListItemSelectListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (packageItemList.get(position).isBeautican())
            return IS_BEAUTICIAN;
        else
            return IS_USER;

    }

    public List<Product> getPackageItemListTemp() {
        return packageItemListTemp;
    }

    @NonNull
    @Override
    public ProductBaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == IS_BEAUTICIAN) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.r_container_product_notuser, parent, false);
            return new ProductBeauticainViewHolder(view, productListItemEditistener);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.r_container_product_isuser, parent, false);
            return new ProductViewHolder(view, productListItemSelectListener);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ProductBaseViewHolder holder, int position) {
        Product product = packageItemList.get(position);
        if (getItemViewType(position) == IS_USER) {
            ProductViewHolder productViewHolder = (ProductViewHolder) holder;
            productViewHolder.actualPrice.setText("₹ " + product.getmActualPrice());
            productViewHolder.discountPercentage.setText("OFF " + product.getmOfferPercentage() + " %");
            productViewHolder.originalPrice.setText("₹ " + product.getmOriginalPrice());
            productViewHolder.productTitle.setText(product.getmTitle());
            productViewHolder.countView.setText("0");
            productViewHolder.onBind(position);


        } else {

            ProductBeauticainViewHolder productBeauticainViewHolder = (ProductBeauticainViewHolder) holder;
            productBeauticainViewHolder.actualPrice.setText("₹ " + product.getmActualPrice());
            productBeauticainViewHolder.discountPercentage.setText("OFF " + product.getmOfferPercentage() + " %");
            productBeauticainViewHolder.originalPrice.setText("₹ " + product.getmOriginalPrice());
            productBeauticainViewHolder.productTitle.setText(product.getmTitle());


        }

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

    class ProductViewHolder extends ProductBaseViewHolder implements View.OnClickListener {
        TextView actualPrice;
        TextView originalPrice;
        TextView discountPercentage;
        TextView productTitle;
        TextView countView;
        ImageView add;
        ImageView remove;
        ProductListItemSelectListener productListItemSelectListener;

        public ProductViewHolder(@NonNull View itemView, ProductListItemSelectListener productListItemSelectListener) {
            super(itemView);
            actualPrice = itemView.findViewById(R.id.Product_actual_price);
            originalPrice = itemView.findViewById(R.id.Product_original_price);
            discountPercentage = itemView.findViewById(R.id.Product_offer_percent);
            productTitle = itemView.findViewById(R.id.Product_title);
            countView = itemView.findViewById(R.id.Product_count);
            add = itemView.findViewById(R.id.Product_add);
            remove = itemView.findViewById(R.id.Product_remove);
            add.setOnClickListener(this);
            remove.setOnClickListener(this);
            this.productListItemSelectListener = productListItemSelectListener;
            originalPrice.setPaintFlags(originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            countView.setTag(1);

        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            Product product = packageItemList.get(position);
            int count = product.getmCount();
            boolean addOrRemove = false;
            switch (view.getId()) {
                case R.id.Product_add:

                    countView.setTag(count++);
                    product.setmCount(count);
                    packageItemList.set(position, product);
                    packageItemListTemp.add(product);
                    countView.setText(String.valueOf(product.getmCount()));
                    addOrRemove=true;
                    break;
                case R.id.Product_remove:

                    if (count > 0) {
                        countView.setTag(count--);
                        product.setmCount(count);
                        packageItemList.set(position, product);
                        packageItemListTemp.set(packageItemListTemp.indexOf(product), product);
                        countView.setText(String.valueOf(product.getmCount()));
                        addOrRemove=false;
                    }


                    break;


            }
            productListItemSelectListener.onProductEditSelected(getAdapterPosition(), count,product,addOrRemove);


        }

        public void onBind(int position) {
            Product product = packageItemList.get(position);

            countView.setText(String.valueOf(product.getmCount()));
        }
    }

    class ProductBeauticainViewHolder extends ProductBaseViewHolder implements View.OnClickListener {
        TextView actualPrice;
        TextView originalPrice;
        TextView discountPercentage;
        TextView productTitle;
        ProductListItemEditistener productListItemEditistener;

        TextView edit;


        public ProductBeauticainViewHolder(@NonNull View itemView, ProductListItemEditistener productListItemEditistener) {
            super(itemView);
            actualPrice = itemView.findViewById(R.id.Product_is_beauticain_actual_price);
            originalPrice = itemView.findViewById(R.id.Product_is_beauticain_original_price);
            discountPercentage = itemView.findViewById(R.id.Product_is_beauticain_offer_percent);
            productTitle = itemView.findViewById(R.id.Product_is_beauticain_title);
            edit = itemView.findViewById(R.id.Product_is_beauticain_edit);
            originalPrice.setPaintFlags(originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            edit.setOnClickListener(this);
            this.productListItemEditistener = productListItemEditistener;

        }

        @Override
        public void onClick(View view) {
            productListItemEditistener.onProductSelected(getAdapterPosition());
        }
    }

}
