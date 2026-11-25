
package com.example.practroom_bodroff_pr32
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment

class loginActivity : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.activity_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val login = view.findViewById<EditText>(R.id.editLogin)
        val password = view.findViewById<EditText>(R.id.editPassword)
        val button = view.findViewById<Button>(R.id.btnEnter)

        val preferences = requireActivity().getSharedPreferences(
            "UserData",
            android.content.Context.MODE_PRIVATE
        )

        button.setOnClickListener {

            val enteredLogin = login.text.toString()
            val enteredPassword = password.text.toString()

            if (enteredLogin.isEmpty() || enteredPassword.isEmpty()) {

                Toast.makeText(requireContext(), "Заполните все поля!", Toast.LENGTH_SHORT).show()

            } else {

                if (!preferences.contains("login")) {

                    val editor = preferences.edit()

                    editor.putString("login", enteredLogin)
                    editor.putString("password", enteredPassword)

                    editor.apply()

                    Toast.makeText(requireContext(), "Регистрация успешна!", Toast.LENGTH_SHORT).show()

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, CrimeListFragment())
                        .addToBackStack(null)
                        .commit()

                } else {

                    val savedLogin = preferences.getString("login", "")
                    val savedPassword = preferences.getString("password", "")

                    if (enteredLogin == savedLogin && enteredPassword == savedPassword) {

                        Toast.makeText(requireContext(), "Вход выполнен!", Toast.LENGTH_SHORT).show()

                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, CrimeListFragment())
                            .addToBackStack(null)
                            .commit()

                    } else {

                        Toast.makeText(requireContext(), "Неверный логин или пароль!", Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }

    }
}
