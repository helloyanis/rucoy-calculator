package com.helloyanis.rucoycalculator.ui.skull

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.helloyanis.rucoycalculator.MainActivity
import com.helloyanis.rucoycalculator.MainActivity.Companion.dataStore
import com.helloyanis.rucoycalculator.R
import com.helloyanis.rucoycalculator.databinding.SkullBinding
import com.helloyanis.rucoycalculator.ui.train.Formulas
import kotlinx.coroutines.launch


class SkullFragment : Fragment() {
     var _binding: SkullBinding? = null
    private val binding get() = _binding!!
    private val dataStore: DataStore<Preferences>
        get() = (requireActivity() as MainActivity).dataStore
    private val STAT_KEY = stringPreferencesKey("stat_key")
    private val WEAPON_ATK_KEY = stringPreferencesKey("weapon_atk_key")
    private val BASE_LEVEL_KEY = stringPreferencesKey("base_level_key")
    private val TICK_KEY = stringPreferencesKey("tick_key")
    private val PTRAIN_CLASS_KEY = stringPreferencesKey("ptrain_class_key")
    private val HOURS_KEY = stringPreferencesKey("hours_key")
    private val STAT_GOAL_KEY = stringPreferencesKey("stat_goal_key")
    // Define keys for other preferences as needed
    private var isInit = true
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SkullBinding.inflate(inflater, container, false)
        lifecycleScope.launch {
            dataStore?.data?.collect { preferences ->
                if(isInit) {

                    binding.root.findViewById<EditText>(R.id.baselevelskull).text =
                        Editable.Factory.getInstance().newEditable(preferences[BASE_LEVEL_KEY] ?: "")

                    isInit = false
                    if (binding.root.findViewById<EditText>(R.id.baselevelskull).text.toString()!="") {
                        calcskull(binding.root.findViewById<EditText>(R.id.baselevelskull).text.toString().toDouble())
                    }else{
                        setalldisplays("No data")
                    }

                }
            }
        }

        val editTextNumber = binding.root.findViewById<EditText>(R.id.baselevelskull)
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
        val baseLevelValue = binding.root.findViewById<EditText>(R.id.baselevelskull).text.toString()
        lifecycleScope.launch {
            // Save values to DataStore
            dataStore?.edit { preferences ->
                preferences[BASE_LEVEL_KEY] = baseLevelValue
            }
        }
        binding.root.findViewById<TextView>(R.id.mainstat).text =  "❤️ "+String.format(
            "%,.0f",
            100+double*15
        ) + " ${getString(R.string.hp)}\n🪄" +  String.format(
            "%,.0f",
            100+double*20
        )+ " ${getString(R.string.hp)}\n${getString(R.string.levelexp1)}" + String.format("%,.0f", double) + getString(R.string.levelexp2) + String.format("%,.0f", Formulas.exp_Calc(double)) + getString(R.string.levelexp3) +
                getString(R.string.nextlevel1) + String.format("%,.0f", (Formulas.exp_Calc(double + 1) - Formulas.exp_Calc(double))) + getString(R.string.nextlevel2) + String.format("%,.0f", double + 1) + "!"

        binding.root.findViewById<TextView>(R.id.whiteskullvalue).text =  String.format(
            "%,.0f",
            double*150
        ) + "${getString(R.string.skullGneeded)}\n" +  String.format(
            "%,.0f",
            double*50
        )+ "${getString(R.string.skullGlost)}."
        binding.root.findViewById<TextView>(R.id.yellowskullvalue).text =  String.format(
            "%,.0f",
            double*150
        ) + "${getString(R.string.skullGneeded)}\n" +  String.format(
            "%,.0f",
            double*150
        )+ "${getString(R.string.skullGlost)}."
        binding.root.findViewById<TextView>(R.id.orangeskullvalue).text =  String.format(
            "%,.0f",
            double*600
        ) + "${getString(R.string.skullGneeded)}\n" +  String.format(
            "%,.0f",
            double*450
        )+ "${getString(R.string.skullGlost)}."
        binding.root.findViewById<TextView>(R.id.redskullvalue).text =  String.format(
            "%,.0f",
            double*1950
        ) + "${getString(R.string.skullGneeded)}\n" +  String.format(
            "%,.0f",
            double*1350
        )+ "${getString(R.string.skullGlost)}."
        binding.root.findViewById<TextView>(R.id.blackskullvalue).text =  String.format(
            "%,.0f",
            double*6000
        ) + "${getString(R.string.skullGneeded)}\n" +  String.format(
            "%,.0f",
            double*4050
        )+ "${getString(R.string.skullGlost)}."

    }
}
