package com.pekadev.audioplayer.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.pekadev.audioplayer.R


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun onBackgroundClick(view: View){
        var intent = Intent(this, AudioPageActivity::class.java)
        startActivity(intent)
    }


}
