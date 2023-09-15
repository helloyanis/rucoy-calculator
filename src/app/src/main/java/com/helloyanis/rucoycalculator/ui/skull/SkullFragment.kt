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
                    calcskull(userInput.toDouble())
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
    private fun calcskull(double: Double) {
        val str0 = if((double*50).toString().substringAfter(".").length==1){
            (double*50).toString().substringBefore(".")
        }else {
            (double * 50).toString()
        }
        binding.root.findViewById<TextView>(R.id.whiteskullvalue).text = str0 + "G needed"
        val str1 = if((double*150).toString().substringAfter(".").length==1){
            (double*150).toString().substringBefore(".")
        }else {
            (double*150).toString()
        }
        binding.root.findViewById<TextView>(R.id.yellowskullvalue).text = str1 + "G needed"
        val str2 = if((150*4*double).toString().substringAfter(".").length==1){
            (150*4*double).toString().substringBefore(".")
        }else {
            (150*4*double).toString()
        }
        binding.root.findViewById<TextView>(R.id.orangeskullvalue).text = str2 + "G needed"
        val str3 = if((150*13*double).toString().substringAfter(".").length==1){
            (150*13*double).toString().substringBefore(".")
        }else {
            (150*13*double).toString()
        }
        binding.root.findViewById<TextView>(R.id.redskullvalue).text = str3 + "G needed"
        val str4 = if((double*50).toString().substringAfter(".").length==1){
            (((4-3)*(4-3))*double*4050).toString().substringBefore(".")
        }else {
            (150*40*double).toString()
        }
        binding.root.findViewById<TextView>(R.id.blackskullvalue).text = str4 + "G needed"

    }
}
