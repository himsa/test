package com.example.layanacomputindo.test

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.layanacomputindo.test.model.ResponseLocation
import com.example.layanacomputindo.test.rest.RestClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.ArrayAdapter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_add_location.*
import android.widget.AdapterView
import com.google.android.gms.maps.model.Marker






class AddLocationActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {
    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.mark_me -> {
                with(map) {
                    for (i in 0 until latlngs.size){
                        addMarker(MarkerOptions().position(latlngs[i]))
                    }
                }
            }
            R.id.add_location -> {

            }
        }
    }

    private var pDialog: ProgressDialog? = null
    private val latlngs = ArrayList<LatLng>()
    private lateinit var map: GoogleMap

    val mLOCATION = LatLng(-7.792662, 110.366073)
    val ZOOM_LEVEL = 10f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_location)
        initSpLocation()
        val mapFragment : SupportMapFragment? =
                supportFragmentManager.findFragmentById(R.id.add_map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        add_location.setOnClickListener(this)
        mark_me.setOnClickListener(this)
    }
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap ?: return
    }

    private fun initSpLocation() {
        pDialog = ProgressDialog.show(this,
                "",
                "Tunggu Sebentar!")

        val service by lazy {
            RestClient.getClient(this)
        }
        val call = service.getLocationList()
        call.enqueue(object: Callback<List<ResponseLocation>>{
            override fun onFailure(call: Call<List<ResponseLocation>>?, t: Throwable?) {
                pDialog!!.dismiss()
                Log.e("on Failure", t.toString())
                Toast.makeText(applicationContext, R.string.cekkoneksi, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<ResponseLocation>>, response: Response<List<ResponseLocation>>) {
                Log.d("Location", "Status Code = " + response.code())
                if(response.isSuccessful){
                    pDialog!!.dismiss()
                    val result = response.body()
                    val data = result
                    val listSpinner = ArrayList<String>()
                    for (i in 0 until data.size) {
                        listSpinner.add(data[i].getName().toString())
                    }

                    val adapter = ArrayAdapter<String>(applicationContext,
                            android.R.layout.simple_spinner_item, listSpinner)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    sp_location.setAdapter(adapter)
                    sp_location.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                            val selectedLat = data[position].getLat()!!.toDouble()
                            val selectedLong = data[position].getLong()!!.toDouble()
                            latlngs.add(LatLng(selectedLat, selectedLong))
                            with(map) {
                                moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(selectedLat, selectedLong), ZOOM_LEVEL))
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {

                        }
                    }
                }
            }

        })

    }


}
