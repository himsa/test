package com.example.layanacomputindo.test

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.button -> {
                startActivity(Intent(this@MainActivity, CurrentLocationActivity::class.java))
            }
            R.id.button2 -> {
                startActivity(Intent(this@MainActivity, AddLocationActivity::class.java))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener(this)
        button2.setOnClickListener(this)
    }
}
