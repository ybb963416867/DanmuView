package com.example.pc.danmuview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.pc.danmuview.DanMuView.DanMuAnimation.LiveModel;
import com.example.pc.danmuview.DanMuView.util.CustormAnim;
import com.example.pc.danmuview.DanMuView.util.GiftControl;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LinearLayout danmu;
    private GiftControl giftControl;
    private Button button;
    private List<LiveModel>  liveModels=new ArrayList<>();

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    LiveModel  gift= (LiveModel) msg.obj;

                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.but);
        danmu = (LinearLayout) findViewById(R.id.danmu);
        giftControl = new GiftControl(this);
        giftControl.setGiftLayout(false, danmu, 5).setCustormAnim(new CustormAnim());
        for (int i=0;i<50;i++){
            LiveModel  liveModel = new LiveModel("123"+i,"养你抵达养你抵"+i);
            liveModels.add(liveModel);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                for (LiveModel model : liveModels) {
////                    Message obtain = Message.obtain();
////                    obtain.obj=model;
////                    obtain.what=1;
////                    mHandler.sendMessageDelayed(obtain,2000);
//                    giftControl.loadGift(model);
//                }
                LiveModel  liveModel = new LiveModel("123","养你抵达养你抵");
                giftControl.loadGift(liveModel);
            }
        });
    }
}
