package com.helloyanis.rucoycalculator.ui.train

import GitHubReleaseChecker
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.helloyanis.rucoycalculator.MainActivity
import com.helloyanis.rucoycalculator.MainActivity.Companion.dataStore
import com.helloyanis.rucoycalculator.R
import com.helloyanis.rucoycalculator.databinding.TrainBinding
import com.helloyanis.rucoycalculator.ui.train.Formulas.accuracy_Calc
import com.helloyanis.rucoycalculator.ui.train.Formulas.average_damage_Calc
import com.helloyanis.rucoycalculator.ui.train.Formulas.findStatLevel_Calc
import com.helloyanis.rucoycalculator.ui.train.Formulas.max_crit_damage_Calc
import com.helloyanis.rucoycalculator.ui.train.Formulas.max_damage_Calc
import com.helloyanis.rucoycalculator.ui.train.Formulas.max_raw_crit_damage_Calc
import com.helloyanis.rucoycalculator.ui.train.Formulas.max_tickrate_Calc
import com.helloyanis.rucoycalculator.ui.train.Formulas.min_damage_Calc
import com.helloyanis.rucoycalculator.ui.train.Formulas.powertickrate_Calc
import com.helloyanis.rucoycalculator.ui.train.Formulas.special_magic_max_raw_damage_Calc
import com.helloyanis.rucoycalculator.ui.train.Formulas.special_magic_min_raw_damage_Calc
import com.helloyanis.rucoycalculator.ui.train.Formulas.special_meldist_max_raw_damage_Calc
import com.helloyanis.rucoycalculator.ui.train.Formulas.special_meldist_min_raw_damage_Calc
import com.helloyanis.rucoycalculator.ui.train.Formulas.stat0to54_Calc
import com.helloyanis.rucoycalculator.ui.train.Formulas.stat55to99_Calc
import com.helloyanis.rucoycalculator.ui.train.Formulas.threshold_Calc
import com.helloyanis.rucoycalculator.ui.train.Formulas.time_to_kill_Calc
import com.helloyanis.rucoycalculator.ui.train.Formulas.total_accuracy_Calc
import kotlinx.coroutines.launch
import java.util.concurrent.Flow



class TrainFragment : Fragment() {
    var _binding: TrainBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
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
    @SuppressLint("CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TrainBinding.inflate(inflater, container, false)
        val trainViewModel = ViewModelProvider(this).get(TrainViewModel::class.java)
        lifecycleScope.launch {
            dataStore?.data?.collect { preferences ->
                if(isInit) {

                    binding.root.findViewById<EditText>(R.id.baselevel).text =
                        Editable.Factory.getInstance().newEditable(preferences[BASE_LEVEL_KEY] ?: "")
                    binding.root.findViewById<EditText>(R.id.stat).text =
                        Editable.Factory.getInstance().newEditable(preferences[STAT_KEY] ?: "")
                    binding.root.findViewById<EditText>(R.id.weaponatk).text =
                        Editable.Factory.getInstance().newEditable(preferences[WEAPON_ATK_KEY] ?: "")
                    binding.root.findViewById<EditText>(R.id.tick).text =
                        Editable.Factory.getInstance().newEditable(preferences[TICK_KEY] ?: "4")
                    binding.root.findViewById<Spinner>(R.id.classspinner).setSelection(
                        preferences[PTRAIN_CLASS_KEY]?.toInt() ?: 0)
                    binding.root.findViewById<EditText>(R.id.hours).text =
                        Editable.Factory.getInstance().newEditable(preferences[HOURS_KEY] ?: "")
                    binding.root.findViewById<EditText>(R.id.statgoal).text =
                        Editable.Factory.getInstance().newEditable(preferences[STAT_GOAL_KEY] ?: "")

                    isInit = false
                }
            }
            updateoutput() // Call updateoutput() only once after restoring saved values
        }


        binding.tickhelp.setOnClickListener{
            Toast.makeText(context,"It's how many mobs you can hit with 1 power attack!", Toast.LENGTH_LONG).show()
        }
        val trainstylespinner = binding.root.findViewById<Spinner>(R.id.trainstylespinner)
        trainstylespinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, i: Int, l: Long) {
                updateoutput()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        })

        val classspinner = binding.root.findViewById<Spinner>(R.id.classspinner)
        classspinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, i: Int, l: Long) {
                updateoutput()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                // Handle case when nothing is selected
            }
        })


        val editTextBaseLevel = binding.root.findViewById<EditText>(R.id.baselevel)

        // Ajoutez un écouteur de texte à votre EditText
        editTextBaseLevel.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val userInput = s.toString()
                updateoutput()
            }
        })

        val editTextStat = binding.root.findViewById<EditText>(R.id.stat)

        // Ajoutez un écouteur de texte à votre EditText
        editTextStat.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val userInput = s.toString()
                updateoutput()
            }
        })

        val editTextWeaponAtk = binding.root.findViewById<EditText>(R.id.weaponatk)

        // Ajoutez un écouteur de texte à votre EditText
        editTextWeaponAtk.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val userInput = s.toString()
                updateoutput()
            }
        })
        val editTextTickrate = binding.root.findViewById<EditText>(R.id.tick)

        // Ajoutez un écouteur de texte à votre EditText
        editTextTickrate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val userInput = s.toString()
                updateoutput()
            }
        })

        val editTextHours = binding.root.findViewById<EditText>(R.id.hours)

        // Ajoutez un écouteur de texte à votre EditText
        editTextHours.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val userInput = s.toString()
                updateoutput()
            }
        })
        val editTextStatGoal = binding.root.findViewById<EditText>(R.id.statgoal)

        // Ajoutez un écouteur de texte à votre EditText
        editTextStatGoal.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val userInput = s.toString()
                updateoutput()
            }
        })

        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateoutput(){
        binding.root.findViewById<TextView>(R.id.str0).text = ""
        binding.root.findViewById<TextView>(R.id.str1).text = ""
        binding.root.findViewById<TextView>(R.id.str2).text = ""
        binding.root.findViewById<TextView>(R.id.str3).text = ""
        binding.root.findViewById<TextView>(R.id.str4).text = ""
        binding.root.findViewById<TextView>(R.id.str5).text = ""
        val baseLevelValue = binding.root.findViewById<EditText>(R.id.baselevel).text.toString()
        val statValue = binding.root.findViewById<EditText>(R.id.stat).text.toString()
        val weaponAtkValue = binding.root.findViewById<EditText>(R.id.weaponatk).text.toString()
        val tickValue = binding.root.findViewById<EditText>(R.id.tick).text.toString()
        val ptrainClassValue = binding.root.findViewById<Spinner>(R.id.trainstylespinner).selectedItemPosition.toString()
        val hoursValue = binding.root.findViewById<EditText>(R.id.hours).text.toString()
        val statGoalValue = binding.root.findViewById<EditText>(R.id.statgoal).text.toString()
        lifecycleScope.launch {
            // Save values to DataStore
            dataStore?.edit { preferences ->
                preferences[BASE_LEVEL_KEY] = baseLevelValue
                preferences[STAT_KEY] = statValue
                preferences[WEAPON_ATK_KEY] = weaponAtkValue
                preferences[TICK_KEY] = tickValue
                preferences[PTRAIN_CLASS_KEY] = ptrainClassValue
                preferences[HOURS_KEY] = hoursValue
                preferences[STAT_GOAL_KEY] = statGoalValue
            }
        }


        when(binding.root.findViewById<Spinner>(R.id.trainstylespinner).selectedItemPosition){
                0->{
                    binding.root.findViewById<Spinner>(R.id.classspinner).visibility = View.GONE
                    binding.root.findViewById<EditText>(R.id.baselevel).visibility=View.VISIBLE
                    binding.root.findViewById<EditText>(R.id.stat).visibility=View.VISIBLE
                    binding.root.findViewById<EditText>(R.id.statgoal).visibility=View.GONE
                    binding.root.findViewById<EditText>(R.id.weaponatk).visibility=View.VISIBLE
                    binding.root.findViewById<EditText>(R.id.tick).visibility=View.GONE
                    binding.root.findViewById<Button>(R.id.tickhelp).visibility=View.GONE
                    binding.root.findViewById<EditText>(R.id.hours).visibility=View.GONE
                    if (binding.root.findViewById<EditText>(R.id.baselevel).text.toString()!="" && binding.root.findViewById<EditText>(R.id.stat).text.toString()!="" && binding.root.findViewById<EditText>(R.id.weaponatk).text.toString()!="") {
                        train()
                    }
                }
                1->{
                    binding.root.findViewById<Spinner>(R.id.classspinner).visibility = View.VISIBLE
                    binding.root.findViewById<EditText>(R.id.baselevel).visibility=View.VISIBLE
                    binding.root.findViewById<EditText>(R.id.stat).visibility=View.VISIBLE
                    binding.root.findViewById<EditText>(R.id.statgoal).visibility=View.GONE
                    binding.root.findViewById<EditText>(R.id.weaponatk).visibility=View.VISIBLE
                    binding.root.findViewById<EditText>(R.id.tick).visibility=View.VISIBLE
                    binding.root.findViewById<Button>(R.id.tickhelp).visibility=View.VISIBLE
                    binding.root.findViewById<EditText>(R.id.hours).visibility=View.GONE
                    if (binding.root.findViewById<EditText>(R.id.baselevel).text.toString()!="" && binding.root.findViewById<EditText>(R.id.stat).text.toString()!="" && binding.root.findViewById<EditText>(R.id.weaponatk).text.toString()!="") {
                        ptrain()
                    }
                }
                2->{
                    binding.root.findViewById<Spinner>(R.id.classspinner).visibility = View.GONE
                    binding.root.findViewById<EditText>(R.id.baselevel).visibility=View.GONE
                    binding.root.findViewById<EditText>(R.id.stat).visibility=View.VISIBLE
                    binding.root.findViewById<EditText>(R.id.statgoal).visibility=View.VISIBLE
                    binding.root.findViewById<EditText>(R.id.weaponatk).visibility=View.GONE
                    binding.root.findViewById<EditText>(R.id.tick).visibility=View.GONE
                    binding.root.findViewById<Button>(R.id.tickhelp).visibility=View.GONE
                    binding.root.findViewById<EditText>(R.id.hours).visibility=View.VISIBLE
                    if(binding.root.findViewById<EditText>(R.id.stat).text.toString()!="") {
                        offline()
                    }
                }
            }
        }
    private fun train(){
        val stat1 = binding.root.findViewById<EditText>(R.id.stat).text.toString().toDouble()
        val weaponatk = binding.root.findViewById<EditText>(R.id.weaponatk).text.toString().toDouble()
        val base = binding.root.findViewById<EditText>(R.id.baselevel).text.toString().toDouble()
        val min_raw_damage: Double = Formulas.auto_min_raw_damage_Calc(stat1, weaponatk, base)
        val max_raw_damage: Double = Formulas.auto_max_raw_damage_Calc(stat1, weaponatk, base)
        val max_raw_crit_damage: Double = max_raw_crit_damage_Calc(max_raw_damage)
        var accuracy = 0.0
        var str0=""
        var str1=""
        var str2=""
        var str3=""
        var str4=""
        var str5=""

        //An index variable for mobs[]

        //An index variable for mobs[]
        var pos = 0

        //Iterate through loop until you find a mob you can train on with greater than .1749 accuracy

        //Iterate through loop until you find a mob you can train on with greater than .1749 accuracy
        for (x in mobs.size - 1 downTo 0) {
            if (x == 26 || x == 31) {
                continue
            }
            accuracy =
                accuracy_Calc(max_raw_crit_damage, max_raw_damage, min_raw_damage, x)
            if (accuracy >= 0.1749) {
                pos = x
                break
            }
        }

        //Calculate average damage which you need for average time to kill

        //Calculate average damage which you need for average time to kill
        val min_damage: Double = min_damage_Calc(min_raw_damage, pos)
        val max_damage: Double = max_damage_Calc(max_raw_damage, pos)
        val max_crit_damage: Double = max_crit_damage_Calc(max_raw_crit_damage, pos)
        val avgdmg: Double =
            average_damage_Calc(accuracy, max_damage, min_damage, max_crit_damage)
        val tickrate: Double = Formulas.tickrate_Calc(accuracy, 3600.toDouble())

        //In certain cases you can effective train on two mobs

        //In certain cases you can effective train on two mobs
        var onemob = true
        var checknextmob = true
        var newpos = pos + 1
        if (pos == 5 || pos == 20 || pos == 22 || pos == 28 || pos == 30) {
            pos--
            onemob = false
        }
        if (newpos > 40) {
            checknextmob = false
        }
        if (newpos == 26 || newpos == 31) {
            newpos++
        }

        val time: Double = time_to_kill_Calc(avgdmg, pos)
        str0 = """
            ${
            "👾 You can train effectively on " + mobs.get(pos).mob_name /*+ mobs.get(pos)
                .getEmoji_code()*/
        }!
            
            """.trimIndent()
        if (!onemob) {
            val time2: Double = time_to_kill_Calc(avgdmg, pos + 1)
            str0 = """
                ${
                "👾 You can train effectively on " + mobs.get(pos).mob_name /*+ mobs.get(pos)
                    .getEmoji_code()*/ + " & " + mobs.get(pos + 1).mob_name /*+ mobs.get(pos + 1)
                    .getEmoji_code()*/
            }!
                
                """.trimIndent()
            str3 = """
                ${
                "⏱️ Average time to kill " + mobs.get(pos + 1).mob_name /*+ mobs.get(pos + 1)
                    .getEmoji_code()*/
            }: ${time2.toInt() / 60} min. ${time2.toInt() % 60} sec.
                
                """.trimIndent() //part of output embed
        }

        //calculating stats you need to train on the next mob

        //calculating stats you need to train on the next mob
        var statadd = 0
        var checked = false
        var alrdealdamage = false
        var dealdamage = false
        var new_max_damage: Double
        var newaccuracy = 0.0
        while (newaccuracy < 0.1749 && checknextmob) {
            val statneeded = stat1 + statadd
            val new_min_raw_damage: Double =
                Formulas.auto_min_raw_damage_Calc(statneeded, weaponatk, base)
            val new_max_raw_damage: Double =
                Formulas.auto_max_raw_damage_Calc(statneeded, weaponatk, base)
            val new_max_raw_critdamage: Double =
                max_raw_crit_damage_Calc(new_max_raw_damage)
            new_max_damage = max_damage_Calc(new_max_raw_damage, newpos)
            newaccuracy = accuracy_Calc(
                new_max_raw_critdamage,
                new_max_raw_damage,
                new_min_raw_damage,
                newpos
            )
            if (new_max_damage >= 1 && !checked) { //if you can already deal damage to the next mob
                str5 =
                    "💥 You can deal " + new_max_damage.toInt() + " max damage to " + mobs.get(
                        newpos
                    ).mob_name /*+ mobs.get(newpos).getEmoji_code()*/ + "!" //part of output
                alrdealdamage = true
            } else if (new_max_damage > 1 && !alrdealdamage && !dealdamage) { //if you cant deal damage to the next mob yet, you can deal damage in a certain amount of stats!
                str5 =
                    "💥 You can deal " + new_max_damage.toInt() + " max damage to " + mobs.get(
                        newpos
                    ).mob_name /*+ mobs.get(newpos)
                        .getEmoji_code()*/ + " in " + statadd + " stats!" //part of output
                dealdamage = true
            }
            checked = true
            statadd++
        }
        if (checknextmob) {
            str1 =
                """${"🔥 Max. Damage: " + max_damage.toInt() + " " /*+ */} Tickrate: ${tickrate.toInt()} / 3600
"""
            str2 = """
                ${
                "⏱️ Average time to kill " + mobs.get(pos).mob_name /*+ mobs.get(pos)
                    .getEmoji_code()*/
            }: ${time.toInt() / 60} min. ${time.toInt() % 60} sec.
                
                """.trimIndent()
            str4 = """
                ${
                "💪 You need $statadd stats to train effectively on " + mobs.get(newpos)
                    .mob_name /*+ mobs.get(newpos).getEmoji_code()*/
            }!
                
                """.trimIndent()
        } else {
            str1 =
                """${"⏬ Min. Damage (Auto): " + min_damage.toInt() + " " /*+ slime_lord_emoji*/} ⏫ Max. Damage (Auto): ${max_damage.toInt()}
"""
            str2 = """
                ${
                "⏱️ Average time to kill " + mobs.get(pos).mob_name /*+ mobs.get(pos)
                    .getEmoji_code()*/
            }: ${time.toInt() / 60} min. ${time.toInt() % 60} sec.
                
                """.trimIndent()
        }
        binding.root.findViewById<TextView>(R.id.str0).text = str0
        binding.root.findViewById<TextView>(R.id.str1).text = str1
        binding.root.findViewById<TextView>(R.id.str2).text = str2
        binding.root.findViewById<TextView>(R.id.str3).text = str3
        binding.root.findViewById<TextView>(R.id.str4).text = str4
        binding.root.findViewById<TextView>(R.id.str5).text = str5
    }
    private fun ptrain(){
        val str0: String // You can power train effectively on...

        val str1: String // Max Damage... Tickrate...

        val str2: String // Average time to kill...

        var str3 = "" // You need... stats to train effectively on...

        var str4 = "" // You can deal... max damage to...

        var classEmoji = ""

        val stat1 = binding.root.findViewById<EditText>(R.id.stat).text.toString().toDouble()
        val weaponatk = binding.root.findViewById<EditText>(R.id.weaponatk).text.toString().toDouble()
        val base = binding.root.findViewById<EditText>(R.id.baselevel).text.toString().toDouble()
        val classtype = binding.root.findViewById<Spinner>(R.id.classspinner).selectedItemPosition
        var tick = 4.toDouble()
        if(binding.root.findViewById<EditText>(R.id.tick).text.toString()!=""){
            tick = binding.root.findViewById<EditText>(R.id.tick).text.toString().toDouble() //Change this later
        }

        val max_raw_damage: Double
        val min_raw_damage: Double

        if (classtype === 2) {
            min_raw_damage = special_magic_min_raw_damage_Calc(stat1.toDouble(), weaponatk, base)
            max_raw_damage = special_magic_max_raw_damage_Calc(stat1.toDouble(), weaponatk, base)
            classEmoji = "Magic 🔥"
        } else {
            min_raw_damage = special_meldist_min_raw_damage_Calc(stat1.toDouble(), weaponatk, base)
            max_raw_damage = special_meldist_max_raw_damage_Calc(stat1.toDouble(), weaponatk, base)
            classEmoji = if (classtype === 0) {
                "Melee ⚔️"
            } else {
                "Distance 🏹"
            }
        }

        val max_raw_crit_damage = max_raw_crit_damage_Calc(max_raw_damage)
        var accuracy = 0.0
        var pos = 0
        val threshold = threshold_Calc(tick)

        //Iterate through loop until you find a mob you can power train on with greater than .1749 accuracy

        //Iterate through loop until you find a mob you can power train on with greater than .1749 accuracy
        for (x in mobs.size - 1 downTo 0) {
            if (x == 13 || x == 19 || x == 20 || x == 22 || x == 24 || x == 25 || x == 26 || x == 29 || x == 31 || x == 33 || x == 36 || x == 37 || x >= 39) {
                continue
            }
            accuracy = accuracy_Calc(max_raw_crit_damage, max_raw_damage, min_raw_damage, x)
            if (accuracy >= threshold) {
                pos = x
                break
            }
        }

        //Calculate average damage which you need for average time to kill

        //Calculate average damage which you need for average time to kill
        val min_damage = min_damage_Calc(min_raw_damage, pos)
        val max_damage = max_damage_Calc(max_raw_damage, pos)
        val max_crit_damage = max_crit_damage_Calc(max_raw_crit_damage, pos)
        val avgdmg = average_damage_Calc(accuracy, max_damage, min_damage, max_crit_damage)
        val totalaccuracy = total_accuracy_Calc(accuracy, tick)
        val maxtickrate: Double = max_tickrate_Calc(tick).toDouble()
        val powertickrate = powertickrate_Calc(totalaccuracy, maxtickrate)
        val time = time_to_kill_Calc(avgdmg, pos)

        str0 =
            """
            ${"💪 You can power train  " + classEmoji + " on  " + mobs[pos].mob_name /*+ mobs[pos].getEmoji_code()*/}!
            
            """.trimIndent()

        //calculating stats you need to train on the next mob


        //calculating stats you need to train on the next mob
        var statadd = 0
        var checked = false
        var alrdealdamage = false
        var dealdamage = false
        var new_max_damage: Double
        var newaccuracy = 0.0
        var newpos = pos + 1
        while (true) {
            if (newpos == 13 || newpos == 19 || newpos == 20 || newpos == 22 || newpos == 24 || newpos == 25 || newpos == 26 || newpos == 29 || newpos == 31 || newpos == 33 || newpos == 36 || newpos == 37) {
                newpos++
            } else if (newpos >= 38) {
                newpos = 38
                break
            } else {
                break
            }
        }
        val checknextmob = pos != 38

        while (newaccuracy < threshold && checknextmob) {
            val statneeded = stat1 + statadd
            var new_max_raw_damage: Double
            var new_min_raw_damage: Double
            if (classtype === 2) {
                new_min_raw_damage =
                    special_magic_min_raw_damage_Calc(statneeded.toDouble(), weaponatk, base)
                new_max_raw_damage =
                    special_magic_max_raw_damage_Calc(statneeded.toDouble(), weaponatk, base)
            } else {
                new_min_raw_damage =
                    special_meldist_min_raw_damage_Calc(statneeded.toDouble(), weaponatk, base)
                new_max_raw_damage =
                    special_meldist_max_raw_damage_Calc(statneeded.toDouble(), weaponatk, base)
            }
            val new_max_raw_crit_damage = max_raw_crit_damage_Calc(new_max_raw_damage)
            new_max_damage = max_damage_Calc(new_max_raw_damage, newpos)
            newaccuracy = accuracy_Calc(
                new_max_raw_crit_damage,
                new_max_raw_damage,
                new_min_raw_damage,
                newpos
            )
            if (new_max_damage >= 1 && !checked) { //if you can already deal damage to the next mob
                str4 =
                    "💥 You can deal " + new_max_damage.toInt() + " max damage to " + mobs[newpos].mob_name /*+ mobs[newpos].getEmoji_code()*/ + "!" //part of output
                alrdealdamage = true
            } else if (new_max_damage > 1 && !alrdealdamage && !dealdamage) { //if you cant deal damage to the next mob yet, you can deal damage in a certain amount of stats!
                str4 =
                    "🔥 You can deal " + new_max_damage.toInt() + " max damage to " + mobs[newpos].mob_name /*+ mobs[newpos].getEmoji_code()*/ + " in " + statadd + " stats!" //part of output
                dealdamage = true
            }
            checked = true
            statadd++
        }

        //Building remaining Strings

        //Building remaining Strings
        if (checknextmob) {
            str1 =
                """${"⏫Max. Damage: " + max_damage.toInt() + " • "} 💫 Tickrate: ${powertickrate.toInt()} / ${maxtickrate.toInt()}
"""
            str2 =
                """
        ${"⏱️ Average time to kill " + mobs[pos].mob_name /*+ mobs[pos].getEmoji_code()*/}: ${time.toInt() / 60} min. ${time.toInt() % 60} sec.
        
        """.trimIndent()
            str3 =
                """
        ${"⏏️ You need " + statadd + " stats to power train effectively on " + mobs[newpos].mob_name /*+ mobs[newpos].getEmoji_code()*/}!
        
        """.trimIndent()
        } else {
            str1 =
                """${"⏬ Min. Damage (Auto): " + min_damage.toInt() + " • "} ⏫ Max. Damage (Auto): ${max_damage.toInt()}
"""
            str2 =
                """
        ${"⏱️ Average time to kill " + mobs[pos].mob_name /*+ mobs[pos].getEmoji_code()*/}: ${time.toInt() / 60} min. ${time.toInt() % 60} sec.
        
        """.trimIndent()
        }
        binding.root.findViewById<TextView>(R.id.str0).text = str0
        binding.root.findViewById<TextView>(R.id.str1).text = str1
        binding.root.findViewById<TextView>(R.id.str2).text = str2
        binding.root.findViewById<TextView>(R.id.str3).text = str3
        binding.root.findViewById<TextView>(R.id.str4).text = str4
        binding.root.findViewById<TextView>(R.id.str5).text = ""
    }

    @SuppressLint("SetTextI18n")
    private fun offline(){
        var stat1 = 0
        if(binding.root.findViewById<EditText>(R.id.stat).text.toString()!=""){
            stat1 = binding.root.findViewById<EditText>(R.id.stat).text.toString().toInt()
        }
        var stat2 = 0
        if(binding.root.findViewById<EditText>(R.id.statgoal).text.toString()!=""){
            stat2 = binding.root.findViewById<EditText>(R.id.statgoal).text.toString().toInt()
        }
        var hours = 0
        if(binding.root.findViewById<EditText>(R.id.hours).text.toString()!=""){
            hours = binding.root.findViewById<EditText>(R.id.hours).text.toString().toInt()
        }
        if (stat2 > 0 && hours <= 0) {
            if (stat1 > stat2) {
                binding.root.findViewById<TextView>(R.id.str0).text="Stat goal must be greater than your current stat"
            }else {
                val ticks1: Double
                val ticks2: Double
                if (stat1 <= 54) {
                    ticks1 = stat0to54_Calc(stat1.toDouble())
                } else {
                    ticks1 = stat55to99_Calc(stat1.toDouble())
                }
                if (stat2 <= 54) {
                    ticks2 = stat0to54_Calc(stat2.toDouble())
                } else {
                    ticks2 = stat55to99_Calc(stat2.toDouble())
                }
                val totalticks = ticks2 - ticks1
                binding.root.findViewById<TextView>(R.id.str0).text =
                    "💫 You need approximately " + String.format(
                        "%,.0f",
                        totalticks
                    ) + "ticks until you reach stat level " + java.lang.String.format(
                        "%,d",
                        stat2
                    ) + "!\n" +
                            "⏱️ This is around " + String.format(
                        "%,.1f",
                        totalticks * 60 / 600
                    ) + " minutes, or " + String.format(
                        "%,.1f",
                        totalticks / 600
                    ) + " hours of offline training at 600 exp/hr"
            }
        } else if (hours > 0 && stat2 <= 0) {
            val tickstrained: Int = 600 * hours
            val ticks1: Double
            val ticks2: Double
            if (stat1 <= 54) {
                ticks1 = stat0to54_Calc(stat1.toDouble())
            } else {
                ticks1 = stat55to99_Calc(stat1.toDouble())
            }
            ticks2 = tickstrained + ticks1
            val newStat = Math.round(100.0 * findStatLevel_Calc(ticks2)) / 100.0
            if (newStat < 5) {
                binding.root.findViewById<TextView>(R.id.str0).text="❌ Something went wrong: Could not find new stat!"
            }

            binding.root.findViewById<TextView>(R.id.str0).text=
                "⏱️ Your new stat will be approximately: $newStat with $hours hours of offline training"

        } else {
            binding.root.findViewById<TextView>(R.id.str0).text="❗Please enter either hours OR stat goal"
        }

    }
}

