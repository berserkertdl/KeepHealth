package com.health.keephealth.helper.database;

import android.os.Handler;
import android.os.Message;

import com.health.keephealth.helper.vo.WeightEntity;

/**
 * Created by Administrator on 2015/7/13 0013.
 */
public  class  DBThread {

    public class WeightInsertThread extends Thread{

        private WeightEntity entity;
        private Handler handler;

        public WeightInsertThread(Handler handler,WeightEntity entity) {
            this.entity = entity;
            this.handler = handler;
        }

        @Override
        public void run() {
            DBManager.insertWeightInfo(entity);
            Message msg = new Message();
            msg.what = 0;
            handler.sendMessage(msg);
        }
    }

}
