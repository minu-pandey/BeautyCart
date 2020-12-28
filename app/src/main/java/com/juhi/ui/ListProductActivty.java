package com.juhi.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.juhi.R;
import com.juhi.Util.ProductListItemEditistener;
import com.juhi.Util.ProductListItemSelectListener;
import com.juhi.adapter.ProductListAdapter;
import com.juhi.model.Cart;
import com.juhi.model.Product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ListProductActivty extends AppCompatActivity implements ProductListItemEditistener, ProductListItemSelectListener, View.OnClickListener {
    private static final String TAG = "ListProductActivty";
    RecyclerView productListView;
    ProductListAdapter productListAdapter;
    FirebaseFirestore db;
    String collectionName;
    List<Product> productList;
    List<Product> selectedItem = new ArrayList<>();
    Button next;


    ProgressBar progressBar;
    boolean isBeautician;
    private TextView countView;
    private int mNotifCount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product_activty);
        productList = new ArrayList<>();
        productListView = findViewById(R.id.List_product_r_view);
        progressBar = findViewById(R.id.List_product_progess);
        next = findViewById(R.id.List_product_next);
        productListAdapter = new ProductListAdapter(this, productList, this, this);
        productListView.setLayoutManager(new LinearLayoutManager(this));
        productListView.setItemAnimator(new DefaultItemAnimator());
        productListView.setAdapter(productListAdapter);
        db = FirebaseFirestore.getInstance();
        collectionName = getIntent().getStringExtra("collectionname");

        SharedPreferences sharedPreferences = getSharedPreferences("beautycart", MODE_PRIVATE);
        isBeautician = sharedPreferences.getBoolean("usertype", false);
        next.setOnClickListener(this);
        if (isBeautician) {
            next.setText("Add new Product");
        } else {
            next.setText("Add to cart");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getProductsList();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!isBeautician)
        {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.cart, menu);
            MenuItem item = menu.findItem(R.id.Menu_cart);
            MenuItemCompat.setActionView(item, R.layout.feed_update_count);
            View cart = menu.findItem(R.id.Menu_cart).getActionView();
            countView = cart.findViewById(R.id.notif_count);
            countView.setText(String.valueOf(mNotifCount));
            return true;

        }
        else
        {
            return false;
        }


    }

    private void setNotifCount(int count) {
        mNotifCount = selectedItem.size();


        invalidateOptionsMenu();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void getProductsList() {
        progressBar.setVisibility(View.VISIBLE);
        CollectionReference collectionReference = db.collection(collectionName);
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                QuerySnapshot queryDocumentSnapshots = task.getResult();
                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {

                    Map<String, Object> map = snapshot.getData();
                    Product product = new Product();
                    product.setBeautican(isBeautician);
                    long actualprice = (long) map.get("final_price");
                    long originalPrice = (long) map.get("original_price");
                    product.setmActualPrice(String.valueOf(actualprice));
                    product.setmCount(0);
                    product.setmOfferPercentage((String) map.get("discounted_percentage"));
                    product.setmTitle((String) map.get("service_item"));
                    product.setmOriginalPrice(String.valueOf(originalPrice));
                    productList.add(product);


                }
                progressBar.setVisibility(View.GONE);
                productListAdapter.notifyDataSetChanged();


            }
        });
    }


    @Override
    public void onProductSelected(int selected_position) {
        Log.d(TAG, "onProductSelected: ");
        Intent modifyProduct = new Intent(this, ModifyProduct.class);
        Product product=productList.get(selected_position);
        Gson gson = new Gson();
        String myJson = gson.toJson(product);
        modifyProduct.putExtra("collectionname",collectionName);
        modifyProduct.putExtra("product", myJson);
        startActivity(modifyProduct);


    }

    @Override
    public void onProductEditSelected(int selected_position, int count, Product product, boolean addOrRemove) {
        if (addOrRemove) {
            if (!selectedItem.contains(product)) {

                selectedItem.add(product);

            }
            else
            {
                int index=selectedItem.indexOf(product);

                selectedItem.set(index,product);

            }

        } else {
            if (count == 0) {
                selectedItem.remove(product);

            }
            else
            {

                if (selectedItem.contains(product)) {
                int index=selectedItem.indexOf(product);
                int price=Integer.valueOf(product.getmActualPrice())*count-Integer.valueOf(product.getmActualPrice());
                selectedItem.set(index,product);

            }


            }

        }
        setNotifCount(count);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.List_product_next:
                if (isBeautician) {
                    Intent modifyProduct = new Intent(this, ModifyProduct.class);
                    modifyProduct.putExtra("collectionname",collectionName);
                    startActivity(modifyProduct);

                } else {
                    if(selectedItem.isEmpty())
                    {
                        Toast.makeText(this, "Please select your product", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent cartPreview = new Intent(this, PreviewCartActivity.class);
                    Cart cart = new Cart();
                    cart.setCurrentDate(new Date());
                    cart.setSelectedProducts(selectedItem);
                    Gson gson = new Gson();
                    String myJson = gson.toJson(cart);
                    cartPreview.putExtra("selecteditem", myJson);
                    startActivity(cartPreview);
                }
                break;
        }
    }
}
