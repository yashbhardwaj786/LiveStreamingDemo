package com.arya.livestreaming;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

/**
 * Created by user on 22/08/17.
 */

public class ConnectionReceiver extends BroadcastReceiver {

    private ConnectionDetector cd;

    @Override
    public void onReceive(Context context, Intent intent) {

        cd = new ConnectionDetector(context.getApplicationContext());
        if( ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){

            if (cd.getNetworkInfo() != null)
            {
                if (cd.getNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI)
                {
                    Toast.makeText(context, "Connected To Wifi", Toast.LENGTH_SHORT).show();

//                    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//                    int numberOfLevels = 5;
//                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//                    int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
//                    System.out.println("hh yashal level is "+level);
//                    if (level == 1)
//                    {
//                        Toast.makeText(context, "Connected To Wifi and Strength is very poor", Toast.LENGTH_SHORT).show();
//                    }
//                    else if (level == 2)
//                    {
//                        Toast.makeText(context, "Connected To Wifi and Strength is bad ", Toast.LENGTH_SHORT).show();
//                    }
//                    else if (level == 3)
//                    {
//                        Toast.makeText(context, "Connected To Wifi and Strength is average", Toast.LENGTH_SHORT).show();
//                    }
//                    else if (level == 4)
//                    {
//                        Toast.makeText(context, "Connected To Wifi and Strength is good", Toast.LENGTH_SHORT).show();
//                    }
//                    else if (level == 5)
//                    {
//                        Toast.makeText(context, "Connected To Wifi and Strength is excellent", Toast.LENGTH_SHORT).show();
//                    }

                }
                else if(cd.getNetworkInfo().getType() == ConnectivityManager.TYPE_MOBILE)
                {

                    Toast.makeText(context, "Connected To Mobile Data", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(context, "No internet connection available", Toast.LENGTH_LONG).show();
            }
        }


    }

}
