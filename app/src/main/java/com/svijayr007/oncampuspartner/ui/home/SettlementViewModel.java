package com.svijayr007.oncampuspartner.ui.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.svijayr007.oncampuspartner.callback.ISettlementCallbackListener;
import com.svijayr007.oncampuspartner.common.Common;
import com.svijayr007.oncampuspartner.model.SettlementModel;

import java.util.ArrayList;
import java.util.List;

public class SettlementViewModel extends ViewModel implements ISettlementCallbackListener {
    private MutableLiveData<List<SettlementModel>> settlementMutableLiveData;
    private MutableLiveData<String> messageError;

    private ISettlementCallbackListener listener;

    public SettlementViewModel() {
        settlementMutableLiveData = new MutableLiveData<>();
        messageError = new MutableLiveData<>();
        listener = this;
    }

    public MutableLiveData<String> getMessageError() {
        return messageError;
    }

    public MutableLiveData<List<SettlementModel>> getSettlementMutableLiveData() {
        loadLast5Settlements();
        return settlementMutableLiveData;
    }

    private void loadLast5Settlements() {
        List<SettlementModel> tempList = new ArrayList<>();
        Query settlementRef = FirebaseDatabase.getInstance(Common.settlementDb)
                                .getReference(Common.SETTLEMENTS_REF)
                                .child(Common.currentPartnerUser.getRestaurant());
        settlementRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot settlementSnap : snapshot.getChildren()){
                        SettlementModel settlementModel =  settlementSnap.getValue(SettlementModel.class);
                        settlementModel.setKey(settlementSnap.getKey());
                        settlementModel.setSettlementId(settlementSnap.getKey());
                        tempList.add(settlementModel);
                    }

                    listener.onLast5SettlementLoadSuccess(tempList);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onLast5SettlementLoadFailed(error.getMessage());
            }
        });
    }

    @Override
    public void onLast5SettlementLoadSuccess(List<SettlementModel> settlementModelList) {
        settlementMutableLiveData.setValue(settlementModelList);

    }

    @Override
    public void onLast5SettlementLoadFailed(String message) {
        messageError.setValue(message);
    }
}
