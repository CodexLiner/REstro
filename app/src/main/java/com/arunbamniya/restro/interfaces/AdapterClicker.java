package com.arunbamniya.restro.interfaces;

import com.arunbamniya.restro.network.ItemResponse;

public interface AdapterClicker {
    void onItemClicked(ItemResponse item, int position);

    void onCartChanged(ItemResponse item, int position);

    void onCategoryChanged(String category , int position);

    void upDateCartValue(boolean b);



}
