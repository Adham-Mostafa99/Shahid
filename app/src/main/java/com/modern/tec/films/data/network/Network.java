package com.modern.tec.films.data.network;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


public class Network {

    MutableLiveData<Boolean> isNetworkAvailable;

    @SuppressLint("CheckResult")
    public Network(Application application) {
        isNetworkAvailable = new MutableLiveData<>();

        application.registerReceiver(mConnReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


    }


    private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
            boolean isFailover = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);

            NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            NetworkInfo otherNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);

            isNetworkAvailable.setValue(currentNetworkInfo.isConnected());
        }
    };

    public LiveData<Boolean> getIsNetworkAvailable() {
        return isNetworkAvailable;
    }
}
