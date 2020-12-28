package com.juhi.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.juhi.R;
import com.juhi.model.Cart;
import com.juhi.model.Product;

import java.util.HashMap;
import java.util.Map;

public class ModifyProduct extends AppCompatActivity implements View.OnClickListener {
    TextInputEditText productName;
    TextInputEditText productActualPrice;
    TextInputEditText productoriginalPrice;
    TextInputEditText productDiscountPercentage;
    TextView title;
    Button saveChanges;
    FirebaseFirestore db;
    Product product;
    String collectionName;
    private TextInputEditText[] fields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_product);
        productName = findViewById(R.id.ModifyProduct_product_name);
        productActualPrice = findViewById(R.id.ModifyProduct_product_actual_price);
        productoriginalPrice = findViewById(R.id.ModifyProduct_original_price);
        productDiscountPercentage = findViewById(R.id.ModifyProduct_discount_percentage);
        title = findViewById(R.id.ModifyProduct_title);
        saveChanges = findViewById(R.id.ModifyProduct_save);
        saveChanges.setOnClickListener(this);
        db = FirebaseFirestore.getInstance();
        collectionName = getIntent().getStringExtra("collectionname");

    }

    @Override
    protected void onStart() {
        super.onStart();
        setProductDetails();
    }

    private void setProductDetails() {
        Gson gson = new Gson();
        product = gson.fromJson(getIntent().getStringExtra("product"), Product.class);
        if (product == null) {
            title.setText("add new Product");
            return;
        }
        productName.setText(product.getmTitle());
        productDiscountPercentage.setText(product.getmOfferPercentage() + " %");
        productActualPrice.setText(product.getmActualPrice());
        productoriginalPrice.setText(product.getmOriginalPrice());
    }

    @Override
    public void onClick(View view) {
        fields = new TextInputEditText[]{productName, productActualPrice, productoriginalPrice, productDiscountPercentage};
        boolean isAllFieldsOk = verifyDetails(fields);
        if (isAllFieldsOk) {
            Toast.makeText(this, "Check your fields", Toast.LENGTH_SHORT).show();
        }
        saveChangesToCloud();
    }

    private void saveChangesToCloud() {
        Map<String, Object> productMap = new HashMap<>();
        productMap.put("service_item", productName.getText().toString());
        productMap.put("final_price",Long.valueOf( productActualPrice.getText().toString()));
        productMap.put("original_price", Long.valueOf(productoriginalPrice.getText().toString()));
        productMap.put("discounted_percentage", productDiscountPercentage.getText().toString());

        db.collection(collectionName).document(productName.getText().toString()).set(productMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ModifyProduct.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean verifyDetails(TextInputEditText[] fields) {
        for (TextInputEditText textInputEditText : fields) {
            if (textInputEditText.getText().toString().isEmpty())
                return true;
        }
        return false;
    }
}
