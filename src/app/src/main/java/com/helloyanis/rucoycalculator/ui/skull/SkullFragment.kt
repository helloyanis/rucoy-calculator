package com.helloyanis.rucoycalculator.ui.skull

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.helloyanis.rucoycalculator.R
import com.helloyanis.rucoycalculator.databinding.SkullBinding


class SkullFragment : Fragment() {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data")
    private var _binding: SkullBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SkullBinding.inflate(inflater, container, false)
        setalldisplays("No data")
        val editTextNumber = binding.root.findViewById<EditText>(R.id.editTextNumber)

        // Ajoutez un écouteur de texte à votre EditText
        editTextNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val userInput = s.toString()
                if (userInput.isNotEmpty()) {
                    calcskull(userInput.toInt())
                }
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun setalldisplays(string: String){
        binding.root.findViewById<TextView>(R.id.whiteskullvalue).text = string
        binding.root.findViewById<TextView>(R.id.yellowskullvalue).text = string
        binding.root.findViewById<TextView>(R.id.orangeskullvalue).text = string
        binding.root.findViewById<TextView>(R.id.redskullvalue).text = string
        binding.root.findViewById<TextView>(R.id.blackskullvalue).text = string
    }
    private fun calcskull(int: Int) {
        if(int<1){
            setalldisplays("Invalid data")
        }else{
            binding.root.findViewById<TextView>(R.id.whiteskullvalue).text = (int*50).toString() + "G needed\n"+(int*50).toString()+"G lost"
            binding.root.findViewById<TextView>(R.id.yellowskullvalue).text = (int*150).toString() + "G needed\n" + (int*150).toString() +"G lost"
            binding.root.findViewById<TextView>(R.id.orangeskullvalue).text = (int*600).toString() + "G needed \n" + (int*450) + "G lost"
            binding.root.findViewById<TextView>(R.id.redskullvalue).text = (int*1950).toString() + "G needed\n" + (int*1350) + "G lost"
            binding.root.findViewById<TextView>(R.id.blackskullvalue).text = (int*6000).toString() + "G needed\n" + (int*4050) + "G lost"
        }
    }
}
