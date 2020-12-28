package com.juhi.Util;

import com.juhi.model.Product;

public interface ProductListItemSelectListener {
    void onProductEditSelected(int selected_position, int count, Product product,boolean addOrRemove);
}
