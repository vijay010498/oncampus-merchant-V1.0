package com.svijayr007.oncampuspartner.ui.signup;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.svijayr007.oncampuspartner.callback.ICampusCallbackListener;
import com.svijayr007.oncampuspartner.common.Common;
import com.svijayr007.oncampuspartner.model.CampusModel;

import java.util.ArrayList;
import java.util.List;

public class CampusViewModel extends ViewModel implements ICampusCallbackListener {
    private MutableLiveData<List<CampusModel>> campusListMutable;
    private MutableLiveData<String> messageError = new MutableLiveData<>();

    private ICampusCallbackListener listener;

    public CampusViewModel() {
        listener = this;
    }

    public MutableLiveData<List<CampusModel>> getCampusListMutable() {
        if(campusListMutable == null){
            campusListMutable = new MutableLiveData<>();
            loadAllCampusFromFirebase();
        }
        return campusListMutable;
    }

    private void loadAllCampusFromFirebase() {
        List<CampusModel> campusModels = new ArrayList<>();
        DatabaseReference campusRef = FirebaseDatabase.getInstance(Common.serverValues)
                .getReference(Common.CAMPUS_REF);
        campusRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot campusSnap : dataSnapshot.getChildren()){
                        CampusModel campusModel = campusSnap.getValue(CampusModel.class);
                        Log.i("CAMPUS CHECK",campusModel.getName());
                        campusModels.add(campusModel);
                    }
                    if(campusModels.size() > 0)
                        listener.onCampusLoadSuccess(campusModels);
                    else
                        listener.onCampusLoadFailed("Campus List Empty");

                }else {
                    listener.onCampusLoadFailed("No Campus Found on Server!");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public MutableLiveData<String> getMessageError() {
        return messageError;
    }

    @Override
    public void onCampusLoadSuccess(List<CampusModel> campusModelList) {
        campusListMutable.setValue(campusModelList);

    }

    @Override
    public void onCampusLoadFailed(String message) {
        messageError.setValue(message);

    }
}
