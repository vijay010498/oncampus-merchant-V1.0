package com.svijayr007.oncampuspartner.ui.deliveryAgents;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.svijayr007.oncampuspartner.callback.IDeliveryAgentCallbackListener;
import com.svijayr007.oncampuspartner.common.Common;
import com.svijayr007.oncampuspartner.model.DeliveryAgentModel;

import java.util.ArrayList;
import java.util.List;

public class DeliveryAgentViewModel extends ViewModel implements IDeliveryAgentCallbackListener {
    private MutableLiveData<List<DeliveryAgentModel>> deliveryAgentMutableLiveData;
    private MutableLiveData<String> messageError = new MutableLiveData<>();

    private IDeliveryAgentCallbackListener listener;

    public DeliveryAgentViewModel() {
        listener = this;
    }

    public MutableLiveData<String> getMessageError() {
        return messageError;
    }

    public MutableLiveData<List<DeliveryAgentModel>> getDeliveryAgentMutableLiveData() {
        if(deliveryAgentMutableLiveData == null){
            deliveryAgentMutableLiveData = new MutableLiveData<>();
            loadDeliveryAgentFromServer();
        }
        return deliveryAgentMutableLiveData;
    }

    private void loadDeliveryAgentFromServer() {
        List<DeliveryAgentModel> deliveryAgentModels = new ArrayList<>();
        FirebaseDatabase.getInstance(Common.deliveryAgentsDB)
                .getReference(Common.DELIVERY_AGENT_REF)
                .orderByChild("partner")
                .equalTo(Common.currentPartnerUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for(DataSnapshot deliveryAgentSnapshot : snapshot.getChildren()){
                                DeliveryAgentModel deliveryAgentModel = deliveryAgentSnapshot.getValue(DeliveryAgentModel.class);
                                deliveryAgentModel.setKey(deliveryAgentSnapshot.getKey());
                                deliveryAgentModels.add(deliveryAgentModel);
                            }
                            if(deliveryAgentModels.size() > 0)
                                listener.onDeliveryAgentLoadSuccess(deliveryAgentModels);
                            else
                                listener.onDeliveryAgentLoadFailed("Delivery Agent List Empty");
                        }
                        else{
                            listener.onDeliveryAgentLoadFailed("No Delivery Agent Found");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void onDeliveryAgentLoadSuccess(List<DeliveryAgentModel> deliveryAgentModelList) {
        deliveryAgentMutableLiveData.setValue(deliveryAgentModelList);

    }

    @Override
    public void onDeliveryAgentLoadFailed(String message) {
        messageError.setValue(message);

    }
}