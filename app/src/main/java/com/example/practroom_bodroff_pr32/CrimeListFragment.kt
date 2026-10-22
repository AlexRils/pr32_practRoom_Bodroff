package com.example.practroom_bodroff_pr32

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.widget.EditText

class CrimeListFragment : Fragment(R.layout.fragment_crime_list) {

    private val crimeListViewModel: CrimeListViewModel by viewModels()

    private var adapter: CrimeAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(
            R.id.crime_recycler_view
        )

        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = CrimeAdapter(emptyList())
        recyclerView.adapter = adapter

        val titleInput = view.findViewById<EditText>(R.id.crime_title_input)
        val addButton = view.findViewById<Button>(R.id.add_crime_button)

        addButton.setOnClickListener {
            val title = titleInput.text.toString()

            if (title.isNotBlank()) {
                val crime = Crime(title = title)
                crimeListViewModel.addCrime(crime)
                titleInput.text.clear()
            }
        }

        crimeListViewModel.crimeListLiveData.observe(
            viewLifecycleOwner,
            Observer { crimes ->
                adapter?.updateCrimes(crimes)
            }
        )
    }
}
