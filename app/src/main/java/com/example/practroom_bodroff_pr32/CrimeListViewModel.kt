
package com.example.practroom_bodroff_pr32

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.UUID

class CrimeListViewModel : ViewModel() {

    private val crimeRepository = CrimeRepository.get()

    val crimeListLiveData = crimeRepository.getCrimes()

    fun addCrime(crime: Crime) {
        viewModelScope.launch {
            crimeRepository.addCrime(crime)
        }
    }

    fun updateCrime(crime: Crime) {
        viewModelScope.launch {
            crimeRepository.updateCrime(crime)
        }
    }

    fun getCrime(id: UUID) = crimeRepository.getCrime(id)
}
