package com.svijayr007.oncampuspartner.callback;

import com.svijayr007.oncampuspartner.model.CampusModel;

import java.util.List;

public interface ICampusCallbackListener {
    void onCampusLoadSuccess(List<CampusModel> campusModelList );
    void onCampusLoadFailed(String message);
}
