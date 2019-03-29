package com.example.gaurav.ecommerce.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.gaurav.ecommerce.R;
import com.example.gaurav.ecommerce.interfac.ItemclickListner;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtProductName,txtProductPrice,txtProductQuantity;
    private ItemclickListner itemclickListner;


    public CartViewHolder(View itemView) {
        super(itemView);

        txtProductName=itemView.findViewById(R.id.cart_product_name);
        txtProductPrice=itemView.findViewById(R.id.cart_product_price);
        txtProductQuantity=itemView.findViewById(R.id.cart_product_quantity);
    }

    @Override
    public void onClick(View view) {

        itemclickListner.onClick(view,getAdapterPosition(),false);
    }

    public void setItemclickListner(ItemclickListner itemclickListner) {
        this.itemclickListner = itemclickListner;

    }
}
