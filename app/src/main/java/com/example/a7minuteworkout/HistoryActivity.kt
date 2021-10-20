package com.example.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.activity_main.*

class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        setSupportActionBar(TlHistory)

        val actionBar = supportActionBar

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title = "History"
        }
        TlHistory.setNavigationOnClickListener {
            onBackPressed()
        }
        getAllCompletedDates()
    }

    private fun getAllCompletedDates(){
        val dbHandler = SqliteOpenHelper(this, null)
        val allCompletedDateList = dbHandler.getAllCompletedDateList()

        if(allCompletedDateList.size>0)
        {
            tvHistory.visibility = View.VISIBLE
            rvHistory.visibility = View.VISIBLE

            tvNoDataAvailable.visibility = View.GONE
            rvHistory.layoutManager = LinearLayoutManager(this)
            val historyAdapter = HistoryAdapter(this, allCompletedDateList)
            rvHistory.adapter = historyAdapter
        }
        else
        {
            tvHistory.visibility = View.GONE
            rvHistory.visibility = View.GONE

            tvNoDataAvailable.visibility = View.VISIBLE
        }
        for(i in allCompletedDateList)
        {
            Log.i("DATE GET ALL HISTORY","" + i)
        }
    }
}