package com.example.coursespace

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cat.RecyclerAdapter
import com.example.coursespace.api.Courses
import kotlinx.android.synthetic.main.fragment_catalog.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

private var titlesList = mutableListOf<String>()
private var summaryList = mutableListOf<String>()
private var dateList = mutableListOf<String>()
private var newsSiteList = mutableListOf<String>()
private var links = mutableListOf<String>()


const val BASE_URL = "https://course-space.herokuapp.com/api/v1/"
val TAG = "CatalogFragment"


class CatalogFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_catalog, container, false)

        getCurrentData(view)
        return view
    }

    private fun initRecyclerView(view: View){
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_NewsList)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = RecyclerAdapter(titlesList, summaryList, dateList, newsSiteList, links)
    }

    private fun addToList(title: String, summary: String, date: String, newsSite: String){
        titlesList.add(title)
        summaryList.add(summary)
        dateList.add(date)
        newsSiteList.add(newsSite)
    }

    private fun postToList(data: Courses){
        for(i in 0 until data.size){
            addToList(data[i].title, data[i].description, data[i].date, data[i].author.toString())
        }
    }

    private fun getCurrentData(m: View)
    {

        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRequests::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.getCorses().awaitResponse()
                if (response.isSuccessful) {

                    val data = response.body()!!
                    //Log.d(TAG, data.toString())

                    withContext(Dispatchers.Main) {
                        postToList(data)

                        initRecyclerView(m)
                    }

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main){
//                    Toast.makeText(
//                        applicationContext,
//                        "Seems like something went wrong...",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }
            }
        }

    }
}