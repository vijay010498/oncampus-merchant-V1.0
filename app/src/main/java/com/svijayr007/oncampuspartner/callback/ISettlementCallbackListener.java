package com.svijayr007.oncampuspartner.callback;

import com.svijayr007.oncampuspartner.model.SettlementModel;

import java.util.List;

public interface ISettlementCallbackListener {
   void onLast5SettlementLoadSuccess(List<SettlementModel> settlementModelList);
   void onLast5SettlementLoadFailed(String message);
}
