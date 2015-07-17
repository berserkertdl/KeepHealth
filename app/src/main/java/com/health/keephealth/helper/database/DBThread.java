package com.health.keephealth.helper.database;

import android.os.Handler;
import android.os.Message;

import com.health.keephealth.helper.vo.WeightEntity;

/**
 * Created by Administrator on 2015/7/13 0013.
 */
public  class  DBThread {

    public class WeightSaveOrUpdateThread extends Thread{

        private WeightEntity entity;
        private Handler handler;

        public WeightSaveOrUpdateThread(Handler handler, WeightEntity entity) {
            this.entity = entity;
            this.handler = handler;
        }

        @Override
        public void run() {
            if(entity.getId()>0){
                DBManager.updateWeightInfo(entity);
            }else{
                DBManager.insertWeightInfo(entity);
            }
            Message msg = new Message();
            msg.what = 0;
            handler.sendMessage(msg);
        }
    }

    public class WeightDeleteThread extends Thread{
        private int id;
        private Handler handler;

        public WeightDeleteThread(Handler handler,int id) {
            this.id = id;
            this.handler = handler;
        }

        @Override
        public void run() {
            DBManager.deleteWeightInfo(id);
            Message msg = new Message();
            msg.what = 0;
            handler.sendMessage(msg);
        }
    }

}
