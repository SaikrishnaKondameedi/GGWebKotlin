package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.TextView
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.Call
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap


class MainActivity : AppCompatActivity() {

    private var t1: TextView? = null
    private var t2: TextView? = null
    private var t3: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        t1 = findViewById(R.id.textView1)
        t2 = findViewById(R.id.textView2)
        t3 = findViewById(R.id.textView3)

        getText()
    }
    private fun getText(){
        val retrofit = Retrofit.Builder()
            .baseUrl(BaseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        val request = retrofit.create(GetData::class.java)
        val call = request.data
        request.data.enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t1!!.text = t.message
            }
            override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                try {
                    str = String(response.body()!!.bytes())
                }
                catch (e: IOException) {
                    e.printStackTrace()
                }
                getFirst10()
                getEvery10()
                getCount()
            }
        })
    }
    fun getFirst10(){
        var output = ""
        val get = str!![10]
        output += get
        t1!!.text = output
    }
    fun getEvery10(){
        var output = ""
        for (i in 0 until str!!.length) {
            if (i % 10 == 0) {
                val y: Char = str!![i]
                output += y
            }
        }
        t2!!.text = output
    }
    fun getCount() {
        var output = ""
        val list = str!!.split(" ")
        val wordOut: HashMap<String, Int> = hashMapOf()
        for (i in 0 until list.size) {
            val word = list[i]
            if (word in wordOut.keys) {
                wordOut[word] = wordOut[word]!! + 1
            } else {
                wordOut[word] = 1
            }
        }
        val wordCT = wordOut.toList().sortedByDescending{(_, value) -> value}.toMap()
        for (entry in wordCT.entries) {
                val key1 = entry.key
                val value1 = entry.value
                output = output + "Word : " + key1 + " " + "Value : " + value1 + "\n"
            }
            t3!!.movementMethod = ScrollingMovementMethod()
            t3!!.text = output
        }
    companion object{
        const val BaseURL = "https://www.greedygame.com"
        private var str: String?= null
    }
}