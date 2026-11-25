
package com.example.practroom_bodroff_pr32

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CrimeListFragment : Fragment(R.layout.fragment_crime_list) {

    private val crimeListViewModel: CrimeListViewModel by viewModels()

    private var adapter: CrimeAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(
            R.id.crime_recycler_view
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = CrimeAdapter(emptyList()) { crime ->

            val fragment = CrimeFragment()

            val bundle = Bundle()
            bundle.putString("crime_id", crime.id.toString())

            fragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        recyclerView.adapter = adapter

        val addButton = view.findViewById<Button>(
            R.id.add_crime_button
        )

        addButton.setOnClickListener {

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CrimeFragment())
                .addToBackStack(null)
                .commit()
        }

        crimeListViewModel.crimeListLiveData.observe(
            viewLifecycleOwner
        ) { crimes ->

            adapter?.updateCrimes(crimes)
        }
    }
}
