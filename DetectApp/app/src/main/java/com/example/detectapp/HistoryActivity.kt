package com.example.detectapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_history.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryActivity : AppCompatActivity() {
    private val mHistoryListAdapter = HistoryListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        setTitle("History")

        val linearLayoutManager = LinearLayoutManager(this@HistoryActivity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        list_history.layoutManager = linearLayoutManager
        list_history.adapter = mHistoryListAdapter
        list_history.setHasFixedSize(false)

        MyRetrofit.create(this@HistoryActivity)
            .history().enqueue(MyCallback(this@HistoryActivity, mHistoryListAdapter))
    }

    private class MyCallback(val context: Context, val mHistoryListAdapter: HistoryListAdapter) : Callback<List<History>> {
        override fun onResponse(call: Call<List<History>>, response: Response<List<History>>) {
            if (response.isSuccessful) {
                val historyList = response.body()!!
//                Toast.makeText(context, historyList.count().toString(), Toast.LENGTH_SHORT).show()
                mHistoryListAdapter.setAllData(historyList)
                mHistoryListAdapter.notifyDataSetChanged()
            } else {
                Toast.makeText(context, "Unsuccessful", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<List<History>>, t: Throwable) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }
    }
}