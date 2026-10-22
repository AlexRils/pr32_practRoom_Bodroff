
package com.example.practroom_bodroff_pr32

import android.content.Context
import androidx.room.Room
import androidx.lifecycle.LiveData
import com.example.practroom_bodroff_pr32.database.CrimeDatabase
import java.util.UUID

private const val DATABASE_NAME = "crimedatabase"

class CrimeRepository private constructor(context: Context) {

    private val database = Room.databaseBuilder(
        context.applicationContext,
        CrimeDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val crimeDao = database.crimeDao()

    fun getCrimes(): LiveData<List<Crime>> = crimeDao.getCrimes()

    suspend fun addCrime(crime: Crime) {
        crimeDao.insertCrime(crime)
    }

    fun getCrime(id: UUID): LiveData<Crime?> = crimeDao.getCrime(id)

    companion object {
        private var INSTANCE: CrimeRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }

        fun get(): CrimeRepository {
            return INSTANCE
                ?: throw IllegalStateException(
                    "CrimeRepository must be initialized"
                )
        }
    }
}
