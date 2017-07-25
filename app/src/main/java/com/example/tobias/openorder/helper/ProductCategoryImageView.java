package com.example.tobias.openorder.helper;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.example.tobias.openorder.domain.ProductCategory;

/**
 * Created by tobias on 19.07.17.
 */

public class ProductCategoryImageView extends android.support.v7.widget.AppCompatImageView {
    private ProductCategory productCategory = null;

    public ProductCategoryImageView(Context context) {
        super(context);
    }

    public ProductCategoryImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ProductCategoryImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }
}
