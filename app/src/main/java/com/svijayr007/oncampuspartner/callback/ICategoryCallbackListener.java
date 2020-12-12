package com.svijayr007.oncampuspartner.callback;

import com.svijayr007.oncampuspartner.model.CategoryModel;

import java.util.List;

public interface ICategoryCallbackListener {
    void onCategoryLoadSuccess(List<CategoryModel> categoryModelList );
    void onCategoryLoadFailed(String message);

}
