
package com.example.practroom_bodroff_pr32

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CrimeAdapter(
    private var crimes: List<Crime>,
    private val onCrimeClick: (Crime) -> Unit
) : RecyclerView.Adapter<CrimeAdapter.CrimeHolder>() {

    class CrimeHolder(view: View) : RecyclerView.ViewHolder(view) {

        val titleTextView: TextView =
            view.findViewById(R.id.crime_title)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CrimeHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_crime, parent, false)

        return CrimeHolder(view)
    }

    override fun onBindViewHolder(holder: CrimeHolder, position: Int) {

        val crime = crimes[position]

        holder.titleTextView.text = crime.title

        holder.itemView.setOnClickListener {
            onCrimeClick(crime)
        }
    }

    override fun getItemCount(): Int {
        return crimes.size
    }

    fun updateCrimes(newCrimes: List<Crime>) {
        crimes = newCrimes
        notifyDataSetChanged()
    }
}
