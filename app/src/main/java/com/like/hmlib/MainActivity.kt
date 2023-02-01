package com.like.hmlib

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import com.like.kotlinkit.udp.UdpDiscovery

/**
 *  描述：
 * @author  like
 * Created on 2023-02-01 11:51
 */
class MainActivity : Activity() {
    lateinit var mUpnpManager: UdpDiscovery
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchBt = findViewById<Button>(R.id.search_udp)
        val closeBt = findViewById<Button>(R.id.close_udp)
        mUpnpManager = UdpDiscovery.Builder(this)
            .setMarkPort(6001)
            .setFindListener {

            }
            .setMarkHead("HMAUDIO")
            .build()
        searchBt.setOnClickListener {
            if (!mUpnpManager.mIsSearch)
                mUpnpManager.search()
        }
        closeBt.setOnClickListener {
            if (mUpnpManager.mIsSearch)
                mUpnpManager.stopSearch()
        }
    }

}