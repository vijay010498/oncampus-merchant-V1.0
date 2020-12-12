package com.svijayr007.oncampuspartner.services;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.svijayr007.oncampuspartner.common.Common;

import java.util.Random;

public class MyFCMServices extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        if(remoteMessage.getNotification() != null){
            Common.showNotification(this,new Random().nextInt(),remoteMessage.getNotification().getTitle()
                    ,remoteMessage.getNotification().getBody(),null);
        }

       /* Map<String, String> dataRecv = remoteMessage.getData();
        if(dataRecv != null)
        {
            if(dataRecv.get(Common.NOTI_TITLE).equals("New Order"))
            {
                //Here we need to call Main activity to assign values to Common.currentPartner
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(Common.IS_OPEN_ACTIVITY_NEW_ORDER,true);// use extra to detect is app open from notification
                Common.showNotification(this, new Random().nextInt(),
                        dataRecv.get(Common.NOTI_TITLE),
                        dataRecv.get(Common.NOTI_CONTENT),
                        intent);
            }
            else{
                Common.showNotification(this, new Random().nextInt(),
                        dataRecv.get(Common.NOTI_TITLE),
                        dataRecv.get(Common.NOTI_CONTENT),
                        null);
            }

        }*/
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        if(Common.currentPartnerUser != null)
            if(Common.currentPartnerUser.getUid() != null)
                Common.updateToken(this,token);
    }
}
