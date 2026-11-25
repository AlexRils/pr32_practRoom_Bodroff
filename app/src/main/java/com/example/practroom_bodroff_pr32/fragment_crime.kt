
package com.example.practroom_bodroff_pr32

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import java.util.Date
import java.util.UUID

class CrimeFragment : Fragment(R.layout.fragment_crime) {

    private val crimeListViewModel: CrimeListViewModel by viewModels()

    private var currentCrime: Crime? = null
    private var selectedPhone: String = ""


    private val selectContact = registerForActivityResult(
        ActivityResultContracts.PickContact()
    ) { contactUri ->

        if (contactUri != null) {

            var contactName = ""
            var phoneNumber = ""

            val cursor = requireActivity().contentResolver.query(
                contactUri,
                arrayOf(ContactsContract.Contacts.DISPLAY_NAME),
                null,
                null,
                null
            )

            cursor?.use {
                if (it.moveToFirst()) {
                    contactName = it.getString(0)
                }
            }

            val phoneCursor = requireActivity().contentResolver.query(
                Uri.withAppendedPath(
                    contactUri,
                    ContactsContract.Contacts.Data.CONTENT_DIRECTORY
                ),
                arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
                "${ContactsContract.CommonDataKinds.Phone.MIMETYPE} = ?",
                arrayOf(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE),
                null
            )

            phoneCursor?.use {
                if (it.moveToFirst()) {
                    phoneNumber = it.getString(0)
                }
            }

            if (phoneNumber.isNotEmpty()) {

                selectedPhone = phoneNumber

                view?.findViewById<EditText>(R.id.crime_suspect)
                    ?.setText(contactName)

                Toast.makeText(
                    requireContext(),
                    "Контакт выбран",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                Toast.makeText(
                    requireContext(),
                    "У контакта нет номера телефона",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleInput = view.findViewById<EditText>(R.id.crime_title)
        val suspectInput = view.findViewById<EditText>(R.id.crime_suspect)
        val dateText = view.findViewById<TextView>(R.id.crime_date)
        val solvedCheckBox = view.findViewById<CheckBox>(R.id.crime_solved)

        val saveButton = view.findViewById<Button>(R.id.save_crime_button)
        val selectButton = view.findViewById<Button>(R.id.select_contact_button)
        val callButton = view.findViewById<Button>(R.id.call_suspect_button)

        dateText.text = Date().toString()


        val crimeId = arguments?.getString("crime_id")

        if (crimeId != null) {

            crimeListViewModel.getCrime(UUID.fromString(crimeId))
                .observe(viewLifecycleOwner) { crime ->

                    if (crime != null) {

                        currentCrime = crime

                        titleInput.setText(crime.title)
                        suspectInput.setText(crime.suspect)
                        dateText.text = crime.date.toString()
                        solvedCheckBox.isChecked = crime.isSolved

                        selectedPhone = crime.suspectPhone
                    }
                }
        }

        selectButton.setOnClickListener {
            selectContact.launch(null)
        }


        saveButton.setOnClickListener {

            val title = titleInput.text.toString()
            val suspect = suspectInput.text.toString()

            if (title.isBlank()) {

                Toast.makeText(
                    requireContext(),
                    "Введите название преступления",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                if (currentCrime == null) {

                    val crime = Crime(
                        title = title,
                        suspect = suspect,
                        suspectPhone = selectedPhone
                    )

                    crimeListViewModel.addCrime(crime)

                } else {

                    currentCrime!!.title = title
                    currentCrime!!.suspect = suspect
                    currentCrime!!.suspectPhone = selectedPhone
                    currentCrime!!.isSolved = solvedCheckBox.isChecked

                    crimeListViewModel.updateCrime(currentCrime!!)
                }

                Toast.makeText(
                    requireContext(),
                    "Данные сохранены",
                    Toast.LENGTH_SHORT
                ).show()

                parentFragmentManager.popBackStack()
            }
        }


        callButton.setOnClickListener {

            if (selectedPhone.isBlank()) {

                Toast.makeText(
                    requireContext(),
                    "Сначала выберите контакт",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                val phoneUri = Uri.parse("tel:$selectedPhone")
                val intent = Intent(Intent.ACTION_DIAL, phoneUri)

                startActivity(intent)
            }
        }
    }
}
