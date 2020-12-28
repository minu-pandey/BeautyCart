package com.juhi.Util;

import com.juhi.model.Product;

public interface OnCartItemModified {
    void onItemRemoved(int adapterPosition);
    void onItemModified(int position, boolean isAddOrRemove, int count, Product product);
}
