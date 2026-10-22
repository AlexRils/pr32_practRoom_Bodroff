package com.example.practroom_bodroff_pr32

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CrimeListViewModel : ViewModel() {

    fun addCrime(crime: Crime) {
        viewModelScope.launch {
            crimeRepository.addCrime(crime)
        }
    }

    private val crimeRepository = CrimeRepository.get()



    val crimeListLiveData = crimeRepository.getCrimes()


}