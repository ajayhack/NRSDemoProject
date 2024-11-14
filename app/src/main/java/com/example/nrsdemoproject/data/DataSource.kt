package com.example.nrsdemoproject.data

import android.util.Log
import com.example.nrsdemoproject.MyApplication
import com.example.nrsdemoproject.modal.NRSDataModal
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader


object DataSource {

    //region================Method to Read NRS JSON File Data:-
    fun readNRSData(fileName : String? = null) : MutableList<NRSDataModal>? {
        var inputStream: InputStream? = null
        val builder = StringBuilder()
        var nrsDataModal : NRSDataModal? = null
        val nrsContentList : MutableList<NRSDataModal> = mutableListOf()

        try {
            var jsonString: String? = null
            inputStream = MyApplication.appContext?.assets?.open(fileName ?: "")
            val bufferedReader = BufferedReader(
                InputStreamReader(inputStream, "UTF-8")
            )
            while (bufferedReader.readLine().also { jsonString = it } != null) {
                builder.append(jsonString)
            }
        } finally {
            inputStream?.close()
        }
        val data =  String(builder)
        if(data.isNotEmpty()){
            val jsonObject = JSONObject(data)
            val jsonContentsArray: JSONArray = jsonObject.getJSONArray("data")

            for(i in 0 until jsonContentsArray.length()){
                val itemObj = jsonContentsArray.getJSONObject(i)
                val state = itemObj.getString("state")
                val population = itemObj.getLong("population")
                val counties = itemObj.getInt("counties")
                val detail = itemObj.getString("detail")
                nrsContentList.add(NRSDataModal(state = state , population = population , counties = counties , detail = detail))
            }
            Log.d("DataSource:- " , Gson().toJson(nrsContentList))
        }
        return nrsContentList
    }
    //endregion
}