package com.example.layanacomputindo.test

import android.Manifest
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.EasyPermissions

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

        var perms = Manifest.permission.ACCESS_FINE_LOCATION
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Have permissions, do the thing!
        } else {
            // Ask for permissions
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_location_contacts),
                    1, perms)
        }

        button.setOnClickListener(this)
        button2.setOnClickListener(this)
    }
}
