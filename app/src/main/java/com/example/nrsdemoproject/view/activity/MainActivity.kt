package com.example.nrsdemoproject.view.activity

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nrsdemoproject.R
import com.example.nrsdemoproject.modal.NRSDataModal
import com.example.nrsdemoproject.view.adapter.DefaultStateListAdapter
import com.example.nrsdemoproject.view.adapter.SearchableStateListAdapter
import com.example.nrsdemoproject.viewmodal.AppViewModal
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private val toolbar: Toolbar by lazy { findViewById(R.id.toolbar) }
    private val searchEditText: AppCompatEditText by lazy { findViewById(R.id.searchEditText) }
    private val openDetailScreenButton: AppCompatButton by lazy { findViewById(R.id.openDetailScreenButton) }
    private val defaultStateRV: RecyclerView by lazy { findViewById(R.id.defaultStateRV) }
    private val searchableStateRV: RecyclerView by lazy { findViewById(R.id.searchableStateRV) }
    private var viewModal : AppViewModal? = null
    private var nrsDataList : MutableList<NRSDataModal>? = null
    private var nrsSearchableDataList : MutableList<NRSDataModal>? = null
    private var userSearchDataList : MutableList<NRSDataModal>? = null
    private val defaultStateListAdapter by lazy { DefaultStateListAdapter(nrsDataList , ::onItemClick) }
    private val searchableStateListAdapter by lazy { SearchableStateListAdapter(nrsSearchableDataList , ::onItemClick) }
    private val stateNameText : AppCompatTextView by lazy { findViewById(R.id.stateNameText) }
    private val stateOverAllPopulationText : AppCompatTextView by lazy { findViewById(R.id.stateOverAllPopulationText) }
    private val numberOfCountriesText : AppCompatTextView by lazy { findViewById(R.id.numberOfCountriesText) }
    private val totalCountryPopulationText : AppCompatTextView by lazy { findViewById(R.id.totalCountryPopulationText) }
    private val totalListOfCountryText : AppCompatTextView by lazy { findViewById(R.id.totalListOfCountryText) }
    private val stateEqualCountriesTotalText : AppCompatTextView by lazy { findViewById(R.id.stateEqualCountriesTotalText) }
    private val detailLayoutLL : LinearLayout by lazy { findViewById(R.id.detailLayoutLL) }
    private var detailSelectedModal : NRSDataModal? = null
    private var textWatcher : TextWatcher? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowTitleEnabled(false)
        }
        viewModal = ViewModelProvider(this)[AppViewModal ::class.java]
        loadData()

        openDetailScreenButton.setOnClickListener {
            if(detailSelectedModal != null){
                val intent = Intent(this@MainActivity , DetailActivity :: class.java)
                intent.putExtra("detailModal" , detailSelectedModal)
                startActivityForResult(intent, 320)
            }else{
                Toast.makeText(this , "No State Selected....!!" , Toast.LENGTH_SHORT).show()
            }
        }

        searchEditText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable) {
                updateSearchAdapter(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
            }
        })
    }

    //region===============Function to Read Data from JSON File through AppViewModal:-
    private fun loadData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModal?.getNRSData(fileName = "NRSData.json")
                viewModal?.nrsFlowData?.collect {
                    nrsDataList = it
                    nrsSearchableDataList = it
                    Log.d("DataSource:- " , Gson().toJson(nrsDataList))
                    withContext(Dispatchers.Main){
                        setUpDefaultStateRVData()
                        setUpSearchableStateRVData()
                    }
                }
            }
        }
    }
    //endregion

    //region===========Update Search Adapter:-
    private fun updateSearchAdapter(searchText : String?) {
        if ((searchText?.length ?: 0) > 0) {
            userSearchDataList = nrsSearchableDataList?.filter { it.state?.contains(searchText ?: "", ignoreCase = true) == true } as MutableList<NRSDataModal>
            searchableStateListAdapter.updateDataList(userSearchDataList)
        } else {
            userSearchDataList?.clear()
            searchableStateListAdapter.updateDataList(nrsSearchableDataList)
        }
    }
    //endregion

    //region=============SetUp Default State RecyclerView:-
    private fun setUpDefaultStateRVData(){
        if(!nrsDataList.isNullOrEmpty()){
            defaultStateRV.apply {
                adapter = defaultStateListAdapter
                layoutManager = LinearLayoutManager(this@MainActivity)
                itemAnimator = DefaultItemAnimator()
            }
        }
    }
    //endregion

    //region=============SetUp Default State RecyclerView:-
    private fun setUpSearchableStateRVData(){
        if(!nrsSearchableDataList.isNullOrEmpty()){
            searchableStateRV.apply {
                adapter = searchableStateListAdapter
                layoutManager = LinearLayoutManager(this@MainActivity)
                itemAnimator = DefaultItemAnimator()
            }
        }
    }
    //endregion

    //region=================On Default State Item click event Handle:-
    private fun onItemClick(position : Int , isDefaultStateList : Boolean){
        println("DefaultStateListItemClick:- $isDefaultStateList")
        if(position > -1){
            if(!isDefaultStateList && userSearchDataList?.isNotEmpty() == true){
                setUpDetailInfo(userSearchDataList?.get(position))
            }else{
                setUpDetailInfo(if(isDefaultStateList) nrsDataList?.get(position) else nrsSearchableDataList?.get(position))
            }
        }
    }
    //endregion

    //region============SetUp Item Detail Info:-
    private fun setUpDetailInfo(nrsDataModal: NRSDataModal?) {
        stateNameText.text = nrsDataModal?.state
        stateOverAllPopulationText.text = nrsDataModal?.population?.toString()
        numberOfCountriesText.text = nrsDataModal?.counties?.toString()
        totalCountryPopulationText.text = nrsDataModal?.population?.toString()
        detailLayoutLL.visibility = View.VISIBLE
        detailSelectedModal = nrsDataModal
    }
    //endregion
}