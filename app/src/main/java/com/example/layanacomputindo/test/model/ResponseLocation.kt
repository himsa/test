package com.example.layanacomputindo.test.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseLocation {
    @SerializedName("name")
    @Expose
    private var name: String? = null
    @SerializedName("lat")
    @Expose
    private var lat: String? = null
    @SerializedName("long")
    @Expose
    private var _long: String? = null

    fun getName(): String? {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getLat(): String? {
        return lat
    }

    fun setLat(lat: String) {
        this.lat = lat
    }

    fun getLong(): String? {
        return _long
    }

    fun setLong(_long: String) {
        this._long = _long
    }
}