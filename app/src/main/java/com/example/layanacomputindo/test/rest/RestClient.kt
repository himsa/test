package com.example.layanacomputindo.test.rest

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.example.layanacomputindo.test.BuildConfig
import com.example.layanacomputindo.test.config.Config
import com.example.layanacomputindo.test.model.ResponseLocation
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

class RestClient {
    companion object {
        private var toast: Toast? = null
        private var mContext: Context? = null
        private var gitApiInterface: GitApiInterface? = null
        private val baseUrl = BuildConfig.BASE_URL_API
        private var sharedPref: SharedPreferences? = null
        private val httpClient = OkHttpClient.Builder().connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
        private val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create()
        private val builder = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL_API)
                .addConverterFactory(GsonConverterFactory.create(gson))

        fun retrofit(): Retrofit {
            val client = httpClient.build()
            return builder.client(client).build()
        }

        fun getClient(): GitApiInterface? {
            if (gitApiInterface == null) {

                val gson = GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                        .create()

                val client = Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()
                gitApiInterface = client.create(GitApiInterface::class.java)
            }
            return gitApiInterface
        }

        fun getClient(context: Context?): GitApiInterface {
            if (context != null) {
                mContext = context
                sharedPref = mContext!!.getSharedPreferences(Config.PREF_NAME, Activity.MODE_PRIVATE)
//                httpClient.interceptors().add(contentType)
//                httpClient.interceptors().add(authentication)
//                httpClient.interceptors().add(logging)
            }
            val client = httpClient.build()
            val retrofit = builder.client(client).build()
            return retrofit.create(GitApiInterface::class.java)
        }

        private val contentType = Interceptor { chain ->
            val originalRequest = chain.request()
            val authenticationRequest = originalRequest.newBuilder()
                    .header("Content-Type", "application/json")
                    .build()

            chain.proceed(authenticationRequest)
        }

        private val authentication = Interceptor { chain ->
            val originalRequest = chain.request()
            val authenticationRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer " + sharedPref!!.getString(Config.USER_TOKEN, Config.EMPTY)!!)
                    .build()

            chain.proceed(authenticationRequest)
        }

        private val logging = Interceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)

            val responseBody = response.body()
            val source = responseBody.source()
            source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
            val buffer = source.buffer()
            val responseBodyString = buffer.clone().readString(Charset.forName("UTF-8"))

            if (response.code() != 200) {
                Handler(Looper.getMainLooper()).post {
                    try {
                        Log.d("onFailure Body", responseBodyString)
                        val error = gson.fromJson(responseBodyString, Error::class.java)
                        if (error.message.equals("token_expired", ignoreCase = true)) {
                            showAToast("Sesi Anda telah habis atau Anda tidak memiliki akses pada menu ini. Silahkan logout terlebih dahulu")
                        }
                        //showAToast(error.getMessage());
                    } catch (e: Exception) {

                    }
                }
            }

            response
        }

        private fun showAToast(st: String) { //"Toast toast" is declared in the class
            try {
                toast!!.view.isShown     // true if visible
                toast!!.setText(st)
            } catch (e: Exception) {         // invisible if exception
                toast = Toast.makeText(mContext, st, Toast.LENGTH_LONG)
            }

            toast!!.show()  //finally display it
        }
    }

    interface GitApiInterface {
        @GET("all")
        fun getLocationList(): Call<List<ResponseLocation>>
    }
}