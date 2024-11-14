package com.example.nrsdemoproject.viewmodal
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nrsdemoproject.data.DataSource
import com.example.nrsdemoproject.modal.NRSDataModal
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AppViewModal : ViewModel() {

    //Romantic Comedy First Content List Flow Data:-
    private val nrsMutableSharedData = MutableSharedFlow<MutableList<NRSDataModal>?>()
    val nrsFlowData = nrsMutableSharedData.asSharedFlow()

    suspend fun getNRSData(fileName : String? = null) {
        viewModelScope.launch {
            DataSource.readNRSData(fileName)?.let { nrsMutableSharedData.emit(it) }
        }
    }
}