package com.juhi.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.juhi.R;
import com.juhi.Util.OnCartItemModified;
import com.juhi.adapter.CartListAdapter;
import com.juhi.model.Cart;
import com.juhi.model.Product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PreviewCartActivity extends AppCompatActivity implements OnCartItemModified, View.OnClickListener {
    RecyclerView recyclerView;
    CartListAdapter cartListAdapter;
    List<Product> productList = new ArrayList<>();
    TextView totalPriceView;
    Button placeOrder;
    int totalPrice;
    private List<Product> selectedItemTemp=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_cart);
        totalPriceView = findViewById(R.id.Cart_total_amount);
        placeOrder=findViewById(R.id.Cart_place_order);
        Gson gson = new Gson();
        Cart cart = gson.fromJson(getIntent().getStringExtra("selecteditem"), Cart.class);
        productList.addAll(cart.getSelectedProducts());
        selectedItemTemp.addAll(productList);
        calculateTotalPrice();
        recyclerView = findViewById(R.id.Cart_Preview_r_View);
        cartListAdapter = new CartListAdapter(productList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(cartListAdapter);
        placeOrder.setOnClickListener(this);
    }

    private void calculateTotalPrice() {
        for (Product product:productList)
        {
            totalPrice+=Integer.valueOf(product.getmActualPrice())*Integer.valueOf(product.getmCount());
        }
        totalPriceView.setText("₹ " + totalPrice);
    }


    @Override
    public void onItemRemoved(int adapterPosition) {
        productList.remove(adapterPosition);
        selectedItemTemp.remove(adapterPosition);
        cartListAdapter.notifyItemRemoved(adapterPosition);
        if (productList.isEmpty()) {
            findViewById(R.id.Cart_bottom_layout).setVisibility(View.GONE);
            findViewById(R.id.Cart_Preview_empty_img).setVisibility(View.VISIBLE);
            findViewById(R.id.Cart_Preview_empty_text).setVisibility(View.VISIBLE);
        }
        calculateTempTotalPrice();

    }

    @Override
    public void onItemModified(int position, boolean isAddOrRemove, int count, Product product) {
        if (isAddOrRemove) {
            if (!selectedItemTemp.contains(product)) {
                selectedItemTemp.add(product);
            }
            else
            {
                int index=selectedItemTemp.indexOf(product);
                selectedItemTemp.set(index,product);
            }
        } else {
            if (count == 0) {
                selectedItemTemp.remove(product);
            }
            else
                if (selectedItemTemp.contains(product)) {
                    int index=selectedItemTemp.indexOf(product);
                    int price=Integer.valueOf(product.getmActualPrice())*count-Integer.valueOf(product.getmActualPrice());
                    selectedItemTemp.set(index,product);
                }
        }
        calculateTempTotalPrice();
    }
    private void calculateTempTotalPrice() {
        totalPrice=0;
        for (Product product:selectedItemTemp)
        {
            totalPrice+=Integer.valueOf(product.getmActualPrice())*Integer.valueOf(product.getmCount());
        }
        totalPriceView.setText("₹ " + totalPrice);
    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent(this,ChooseSlot.class);
        Cart cart = new Cart();
        cart.setCurrentDate(new Date());
        cart.setSelectedProducts(selectedItemTemp);
        Gson gson = new Gson();
        String myJson = gson.toJson(cart);
        intent.putExtra("selecteditem", myJson);
        intent.putExtra("totalprice",totalPrice);
        startActivity(intent);
    }
}
