package com.example.nrsdemoproject.view.activity

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import com.example.nrsdemoproject.R
import com.example.nrsdemoproject.modal.NRSDataModal

class DetailActivity : AppCompatActivity() {
    private val toolbar: Toolbar by lazy { findViewById(R.id.toolbar) }
    private val stateNameText : AppCompatTextView by lazy { findViewById(R.id.stateNameText) }
    private val stateOverAllPopulationText : AppCompatTextView by lazy { findViewById(R.id.stateOverAllPopulationText) }
    private val numberOfCountriesText : AppCompatTextView by lazy { findViewById(R.id.numberOfCountriesText) }
    private val totalCountryPopulationText : AppCompatTextView by lazy { findViewById(R.id.totalCountryPopulationText) }
    private val totalListOfCountryText : AppCompatTextView by lazy { findViewById(R.id.totalListOfCountryText) }
    private val stateEqualCountriesTotalText : AppCompatTextView by lazy { findViewById(R.id.stateEqualCountriesTotalText) }
    private val detailLayoutLL : LinearLayout by lazy { findViewById(R.id.detailLayoutLL) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity_layout)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowTitleEnabled(false)
        }
        if(intent != null && intent.extras != null && intent.hasExtra("detailModal")){
            val detailModal = intent.extras?.get("detailModal") as? NRSDataModal?
            setUpDetailInfo(detailModal)
        }
    }

    //region============SetUp Item Detail Info:-
    private fun setUpDetailInfo(nrsDataModal: NRSDataModal?) {
        stateNameText.text = nrsDataModal?.state
        stateOverAllPopulationText.text = nrsDataModal?.population?.toString()
        numberOfCountriesText.text = nrsDataModal?.counties?.toString()
        totalCountryPopulationText.text = nrsDataModal?.population?.toString()
        detailLayoutLL.visibility = View.VISIBLE
    }
    //endregion
}