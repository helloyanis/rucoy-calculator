package com.helloyanis.rucoycalculator.ui.train

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import androidx.core.net.toUri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.materialswitch.MaterialSwitch
import com.helloyanis.rucoycalculator.MainActivity
import com.helloyanis.rucoycalculator.MainActivity.Companion.dataStore
import com.helloyanis.rucoycalculator.R
import com.helloyanis.rucoycalculator.databinding.TrainBinding
import com.helloyanis.rucoycalculator.formulas.Formulas
import com.helloyanis.rucoycalculator.formulas.Formulas.accuracy_Calc
import com.helloyanis.rucoycalculator.formulas.Formulas.auto_max_raw_damage_Calc
import com.helloyanis.rucoycalculator.formulas.Formulas.auto_min_raw_damage_Calc
import com.helloyanis.rucoycalculator.formulas.Formulas.average_damage_Calc
import com.helloyanis.rucoycalculator.formulas.Formulas.consistency_Calc
import com.helloyanis.rucoycalculator.formulas.Formulas.crit_accuracy_Calc
import com.helloyanis.rucoycalculator.formulas.Formulas.defense_accuracy_Calc
import com.helloyanis.rucoycalculator.formulas.Formulas.findStatLevel_Calc
import com.helloyanis.rucoycalculator.formulas.Formulas.high_defense_calc
import com.helloyanis.rucoycalculator.formulas.Formulas.low_defense_calc
import com.helloyanis.rucoycalculator.formulas.Formulas.max_crit_damage_Calc
import com.helloyanis.rucoycalculator.formulas.Formulas.max_damage_Calc
import com.helloyanis.rucoycalculator.formulas.Formulas.max_damage_defense_Calc
import com.helloyanis.rucoycalculator.formulas.Formulas.max_raw_crit_damage_Calc
import com.helloyanis.rucoycalculator.formulas.Formulas.max_tickrate_Calc
import com.helloyanis.rucoycalculator.formulas.Formulas.min_damage_Calc
import com.helloyanis.rucoycalculator.formulas.Formulas.min_damage_defense_Calc
import com.helloyanis.rucoycalculator.formulas.Formulas.normal_accuracy_Calc
import com.helloyanis.rucoycalculator.formulas.Formulas.powertickrate_Calc
import com.helloyanis.rucoycalculator.formulas.Formulas.special_magic_max_raw_damage_Calc
import com.helloyanis.rucoycalculator.formulas.Formulas.special_magic_min_raw_damage_Calc
import com.helloyanis.rucoycalculator.formulas.Formulas.special_meldist_max_raw_damage_Calc
import com.helloyanis.rucoycalculator.formulas.Formulas.special_meldist_min_raw_damage_Calc
import com.helloyanis.rucoycalculator.formulas.Formulas.stat0to54_Calc
import com.helloyanis.rucoycalculator.formulas.Formulas.stat55to99_Calc
import com.helloyanis.rucoycalculator.formulas.Formulas.threshold_Calc
import com.helloyanis.rucoycalculator.formulas.Formulas.time_to_kill_Calc
import com.helloyanis.rucoycalculator.formulas.Formulas.total_accuracy_Calc
import com.helloyanis.rucoycalculator.formulas.Mob
import com.helloyanis.rucoycalculator.formulas.mobs
import kotlinx.coroutines.launch


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
    private val TRAIN_STYLE_KEY = stringPreferencesKey("train_style_key")
    private val ATK_STYLE_KEY = stringPreferencesKey("atk_style_key")
    private val MOB_KEY = stringPreferencesKey("mob_key")
    private val HOURS_KEY = stringPreferencesKey("hours_key")
    private val STAT_GOAL_KEY = stringPreferencesKey("stat_goal_key")
    private val VIEWED_APP_TUTORIAL = stringPreferencesKey("viewed_app_tutorial_key")
    private val VIEWED_ONLINE_TRAIN_TUTORIAL = stringPreferencesKey("viewed_online_train_tutorial_key")
    private val VIEWED_POWER_TRAIN_TUTORIAL = stringPreferencesKey("viewed_power_train_tutorial_key")
    private val VIEWED_OFFLINE_TRAIN_TUTORIAL = stringPreferencesKey("viewed_offline_train_tutorial_key")
    private val VIEWED_DAMAGE_TUTORIAL = stringPreferencesKey("viewed_damage_tutorial_key")
    private val VIEWED_DEFESE_TUTORIAL = stringPreferencesKey("viewed_defense_tutorial_key")
    private val DEFENSE_KEY = stringPreferencesKey("defense_key")
    private val ARMOR_SHIELD_KEY = stringPreferencesKey("armor_shield_key")
    private val ARMOR_OTHER_KEY = stringPreferencesKey("armor_other_key")
    private val MAX_MOB_ATTACK_KEY = stringPreferencesKey("max_mob_attack_key")
    private val MIN_MOB_ATTACK_KEY = stringPreferencesKey("min_mob_attack_key")
    private val CRIT_RING_KEY = stringPreferencesKey("crit_ring_key")
    private val VIEWED_PLAYSTORE_PHASEOUT_KEY = stringPreferencesKey("viewed_playstore_phaseout_key")
    // Define keys for other preferences as needed
    private var trainstyleValue: Int = 0
    private var isInit = true
    @SuppressLint("CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TrainBinding.inflate(inflater, container, false)
        lifecycleScope.launch {
            dataStore.data.collect { preferences ->
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
                    trainstyleValue = preferences[TRAIN_STYLE_KEY]?.toInt() ?: 0
                    binding.root.findViewById<AutoCompleteTextView>(R.id.trainstylespinner).setText(resources.getStringArray(R.array.train_methods_array)[trainstyleValue], false)
                    binding.root.findViewById<Spinner>(R.id.mobspinner).setSelection(
                        preferences[MOB_KEY]?.toInt() ?: 0)
                    binding.root.findViewById<Spinner>(R.id.atkstylespinner).setSelection(
                        preferences[ATK_STYLE_KEY]?.toInt() ?: 0)
                    binding.root.findViewById<EditText>(R.id.hours).text =
                        Editable.Factory.getInstance().newEditable(preferences[HOURS_KEY] ?: "")
                    binding.root.findViewById<EditText>(R.id.statgoal).text =
                        Editable.Factory.getInstance().newEditable(preferences[STAT_GOAL_KEY] ?: "")
                    binding.root.findViewById<MaterialSwitch>(R.id.critring).isChecked =
                        preferences[CRIT_RING_KEY]?.toBoolean() == true
                    binding.root.findViewById<EditText>(R.id.defense).text =
                        Editable.Factory.getInstance().newEditable(preferences[DEFENSE_KEY] ?: "")
                    binding.root.findViewById<EditText>(R.id.shieldArmor).text =
                        Editable.Factory.getInstance().newEditable(preferences[ARMOR_SHIELD_KEY] ?: "")
                    binding.root.findViewById<EditText>(R.id.otherArmor).text =
                        Editable.Factory.getInstance().newEditable(preferences[ARMOR_OTHER_KEY] ?: "")
                    binding.root.findViewById<EditText>(R.id.mobMaxAtk).text =
                        Editable.Factory.getInstance().newEditable(preferences[MAX_MOB_ATTACK_KEY] ?: "")
                    binding.root.findViewById<EditText>(R.id.mobMinAtk).text =
                        Editable.Factory.getInstance().newEditable(preferences[MIN_MOB_ATTACK_KEY] ?: "")

                    isInit = false
                }
            }
            updateoutput() // Call updateoutput() only once after restoring saved values
        }
        // Sort the array based on mob_level
        val sortedMobs = mobs.sortedBy { it.mob_level }

        // Setup mob spinner
        val mobNamesList = sortedMobs.map { it.mob_name }
        val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this.requireContext(),
            android.R.layout.simple_spinner_item,
            mobNamesList
        )

        binding.mobspinner.adapter = spinnerArrayAdapter


        binding.showtutorial.setOnClickListener {
            when(trainstyleValue){
                0->{
                    context?.let { ctx ->
                        MaterialAlertDialogBuilder(ctx)
                            .setTitle(resources.getString(R.string.onlinetraintutorial_title))
                            .setMessage(resources.getString(R.string.onlinetraintutorial_desc))
                            .setPositiveButton(resources.getString(R.string.popups_okbtn)) { _, _ ->

                            }.show()
                    }
                }
                1->{
                    context?.let { ctx ->
                        MaterialAlertDialogBuilder(ctx)
                            .setTitle(resources.getString(R.string.ptraintutorial_title))
                            .setMessage(resources.getString(R.string.ptraintutorial_desc))
                            .setPositiveButton(resources.getString(R.string.popups_okbtn)) { _, _ ->

                            }
                            .show()
                    }
                }
                2->{
                    context?.let { ctx ->
                        MaterialAlertDialogBuilder(ctx)
                            .setTitle(resources.getString(R.string.offlinetutorial_title))
                            .setMessage(resources.getString(R.string.offlinetutorial_desc))
                            .setPositiveButton(resources.getString(R.string.popups_okbtn)) { _, _ ->

                            }
                            .show()
                    }
                }
                3->{
                    context?.let { ctx ->
                        MaterialAlertDialogBuilder(ctx)
                            .setTitle(resources.getString(R.string.damagetutorial_title))
                            .setMessage(resources.getString(R.string.damagetutorial_desc))
                            .setPositiveButton(resources.getString(R.string.popups_okbtn)) { _, _ ->

                            }
                            .show()
                    }
                }
                4->{
                    context?.let { ctx ->
                        MaterialAlertDialogBuilder(ctx)
                            .setTitle(resources.getString(R.string.defensetutorial_title))
                            .setMessage(resources.getString(R.string.defensetutorial_desc))
                            .setPositiveButton(resources.getString(R.string.popups_okbtn)) { _, _ ->

                            }
                            .show()
                        MaterialAlertDialogBuilder(ctx)
                            .setTitle(resources.getString(R.string.defensetutorial_title))
                            .setMessage(resources.getString(R.string.defensetutorial_wip_desc))
                            .setPositiveButton(resources.getString(R.string.popups_okbtn)) { _, _ ->

                            }
                            .show()
                    }
                }
            }
        }

        val trainstylespinner = binding.root.findViewById<AutoCompleteTextView>(R.id.trainstylespinner)
        trainstylespinner.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            trainstyleValue = position
            updateoutput()
            when (position) {
                0 -> {
                    context?.let {
                        lifecycleScope.launch {
                            dataStore.data.collect { preferences ->
                                if (preferences[VIEWED_ONLINE_TRAIN_TUTORIAL].toString() != "1") {
                                    MaterialAlertDialogBuilder(it)
                                        .setTitle(resources.getString(R.string.onlinetraintutorial_title))
                                        .setMessage(resources.getString(R.string.onlinetraintutorial_desc))
                                        .setPositiveButton(resources.getString(R.string.popups_okbtn)) { _, _ ->

                                        }
                                        .show()

                                    // Save values to DataStore
                                    dataStore.edit { preferences ->
                                        preferences[VIEWED_ONLINE_TRAIN_TUTORIAL] = "1"
                                    }
                                }
                            }
                        }
                    }
                }

                1 -> {
                    context?.let {
                        lifecycleScope.launch {
                            dataStore.data.collect { preferences ->
                                if (preferences[VIEWED_POWER_TRAIN_TUTORIAL].toString() != "1") {
                                    MaterialAlertDialogBuilder(it)
                                        .setTitle(resources.getString(R.string.ptraintutorial_title))
                                        .setMessage(resources.getString(R.string.ptraintutorial_desc))
                                        .setPositiveButton(resources.getString(R.string.popups_okbtn)) { _, _ ->

                                        }
                                        .show()

                                    // Save values to DataStore
                                    dataStore.edit { preferences ->
                                        preferences[VIEWED_POWER_TRAIN_TUTORIAL] = "1"
                                    }
                                }
                            }
                        }
                    }
                }

                2 -> {
                    context?.let {
                        lifecycleScope.launch {
                            dataStore.data.collect { preferences ->
                                if (preferences[VIEWED_OFFLINE_TRAIN_TUTORIAL].toString() != "1") {
                                    MaterialAlertDialogBuilder(it)
                                        .setTitle(resources.getString(R.string.offlinetutorial_title))
                                        .setMessage(resources.getString(R.string.offlinetutorial_desc))
                                        .setPositiveButton(resources.getString(R.string.popups_okbtn)) { _, _ ->

                                        }
                                        .show()

                                    // Save values to DataStore
                                    dataStore.edit { preferences ->
                                        preferences[VIEWED_OFFLINE_TRAIN_TUTORIAL] = "1"
                                    }
                                }
                            }
                        }
                    }
                }

                3 -> {
                    context?.let {
                        lifecycleScope.launch {
                            dataStore.data.collect { preferences ->
                                if (preferences[VIEWED_DAMAGE_TUTORIAL].toString() != "1") {
                                    MaterialAlertDialogBuilder(it)
                                        .setTitle(resources.getString(R.string.damagetutorial_title))
                                        .setMessage(resources.getString(R.string.damagetutorial_desc))
                                        .setPositiveButton(resources.getString(R.string.popups_okbtn)) { _, _ ->

                                        }
                                        .show()

                                    // Save values to DataStore
                                    dataStore.edit { preferences ->
                                        preferences[VIEWED_DAMAGE_TUTORIAL] = "1"
                                    }
                                }
                            }
                        }
                    }
                }
                4->{
                    context?.let {
                        lifecycleScope.launch {
                            dataStore.data.collect { preferences ->
                                if (preferences[VIEWED_DEFESE_TUTORIAL].toString() != "1") {
                                    MaterialAlertDialogBuilder(it)
                                        .setTitle(resources.getString(R.string.defensetutorial_title))
                                        .setMessage(resources.getString(R.string.defensetutorial_desc))
                                        .setPositiveButton(resources.getString(R.string.popups_okbtn)) { _, _ ->

                                        }
                                        .show()

                                    MaterialAlertDialogBuilder(it)
                                        .setTitle(resources.getString(R.string.defensetutorial_title))
                                        .setMessage(resources.getString(R.string.defensetutorial_wip_desc))
                                        .setPositiveButton(resources.getString(R.string.popups_okbtn)) { _, _ ->

                                        }
                                        .show()

                                    // Save values to DataStore
                                    dataStore.edit { preferences ->
                                        preferences[VIEWED_DEFESE_TUTORIAL] = "1"
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        //Display app tutorial
        context?.let {
            lifecycleScope.launch {
                dataStore.data.collect { preferences ->
                    if (preferences[VIEWED_APP_TUTORIAL].toString() != "1") {
                        MaterialAlertDialogBuilder(it)
                            .setTitle(resources.getString(R.string.apptutorial_title))
                            .setMessage(resources.getString(R.string.apptutorial_desc))
                            .setPositiveButton(resources.getString(R.string.popups_okbtn)) { _, _ ->

                            }
                            .show()

                        // Save values to DataStore
                        dataStore.edit { preferences ->
                            preferences[VIEWED_APP_TUTORIAL] = "1"
                        }
                    }else{
                        // Show play store phase out popup
                        if (preferences[VIEWED_PLAYSTORE_PHASEOUT_KEY].toString() != "1") {
                            MaterialAlertDialogBuilder(it)
                                .setTitle(resources.getString(R.string.playstore_phaseOut_dialog_title))
                                .setMessage(resources.getString(R.string.playstore_phaseOut_dialog_content))
                                .setNegativeButton(resources.getString(R.string.playstore_phaseOut_dialog_dismiss)) { _, _ ->

                                }
                                .setPositiveButton(resources.getString(R.string.playstore_phaseOut_dialog_learnMore)) { _, _ ->
                                    val i = Intent(
                                        Intent.ACTION_VIEW,
                                        "https://github.com/helloyanis/rucoy-calculator/blob/main/Google Play phase out.md".toUri()
                                    )
                                    startActivity(i)
                                }
                                .show()

                            // Save values to DataStore
                            dataStore.edit { preferences ->
                                preferences[VIEWED_PLAYSTORE_PHASEOUT_KEY] = "1"
                            }
                        }
                    }
                }
            }
        }
        val classspinner = binding.root.findViewById<Spinner>(R.id.classspinner)
        classspinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, i: Int, l: Long) {
                updateoutput()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                // Handle case when nothing is selected
            }
        }
        val mobspinner = binding.root.findViewById<Spinner>(R.id.mobspinner)
        mobspinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, i: Int, l: Long) {
                updateoutput()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                // Handle case when nothing is selected
            }
        }
        val atkstylespinner = binding.root.findViewById<Spinner>(R.id.atkstylespinner)
        atkstylespinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, i: Int, l: Long) {
                updateoutput()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                // Handle case when nothing is selected
            }
        }


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

        val editTextDefense = binding.root.findViewById<EditText>(R.id.defense)
        // Ajoutez un écouteur de texte à votre EditText
        editTextDefense.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val userInput = s.toString()
                updateoutput()
            }
        })

        val editTextShieldArmor = binding.root.findViewById<EditText>(R.id.shieldArmor)
        // Ajoutez un écouteur de texte à votre EditText
        editTextShieldArmor.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val userInput = s.toString()
                updateoutput()
            }
        })

        val editTextOtherArmor = binding.root.findViewById<EditText>(R.id.otherArmor)
        // Ajoutez un écouteur de texte à votre EditText
        editTextOtherArmor.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val userInput = s.toString()
                updateoutput()
            }
        })

        val editTextMaxMobAttack = binding.root.findViewById<EditText>(R.id.mobMaxAtk)
        // Ajoutez un écouteur de texte à votre EditText
        editTextMaxMobAttack.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val userInput = s.toString()
                updateoutput()
            }
        })

        val editTextMinMobAttack = binding.root.findViewById<EditText>(R.id.mobMinAtk)
        // Ajoutez un écouteur de texte à votre EditText
        editTextMinMobAttack.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val userInput = s.toString()
                updateoutput()
            }
        })

        val critring = binding.root.findViewById<MaterialSwitch>(R.id.critring)
        // Ajoutez un écouteur de texte à votre Switch
        critring.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
            if (b) {
                lifecycleScope.launch {
                    dataStore.edit { preferences ->
                        preferences[CRIT_RING_KEY] = "true"
                    }
                }
            } else {
                lifecycleScope.launch {
                    dataStore.edit { preferences ->
                        preferences[CRIT_RING_KEY] = "false"
                    }
                }
            }
            updateoutput()
        }


        binding.critring.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
            if (b) updateoutput()
            else updateoutput()
        }


        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        val trainMethods = resources.getStringArray(R.array.train_methods_array)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, trainMethods)
        val trainStyleSpinner = binding.root.findViewById<AutoCompleteTextView>(R.id.trainstylespinner)

        // Attach the adapter to the AutoCompleteTextView
        trainStyleSpinner.dismissDropDown()
        trainStyleSpinner.setAdapter(adapter)



        // Set the selected item again (if needed)
        lifecycleScope.launch {
            dataStore.data.collect { preferences ->
                if (isInit) {
                    trainstyleValue = preferences[TRAIN_STYLE_KEY]?.toInt() ?: 0
                    trainStyleSpinner.setText(trainMethods[trainstyleValue], false)
                }
            }
        }
    }


    private fun updateoutput(){
        binding.root.findViewById<TextView>(R.id.str0).text = ""
        val baseLevelValue = binding.root.findViewById<EditText>(R.id.baselevel).text.toString()
        val statValue = binding.root.findViewById<EditText>(R.id.stat).text.toString()
        val weaponAtkValue = binding.root.findViewById<EditText>(R.id.weaponatk).text.toString()
        val tickValue = binding.root.findViewById<EditText>(R.id.tick).text.toString()
        val ptrainClassValue = binding.root.findViewById<Spinner>(R.id.classspinner).selectedItemPosition.toString()
        val trainStyleValue = trainstyleValue.toString()
        val atkStyleValue = binding.root.findViewById<Spinner>(R.id.atkstylespinner).selectedItemPosition.toString()
        val mobValue = binding.root.findViewById<Spinner>(R.id.mobspinner).selectedItemPosition.toString()
        val hoursValue = binding.root.findViewById<EditText>(R.id.hours).text.toString()
        val statGoalValue = binding.root.findViewById<EditText>(R.id.statgoal).text.toString()
        val critRingValue = binding.root.findViewById<MaterialSwitch>(R.id.critring).isChecked.toString()
        val defenseValue = binding.root.findViewById<EditText>(R.id.defense).text.toString()
        val shieldArmorValue = binding.root.findViewById<EditText>(R.id.shieldArmor).text.toString()
        val otherArmorValue = binding.root.findViewById<EditText>(R.id.otherArmor).text.toString()
        val maxMobAttackValue = binding.root.findViewById<EditText>(R.id.mobMaxAtk).text.toString()
        val minMobAttackValue = binding.root.findViewById<EditText>(R.id.mobMinAtk).text.toString()
        lifecycleScope.launch {
            // Save values to DataStore
            dataStore.edit { preferences ->
                preferences[BASE_LEVEL_KEY] = baseLevelValue
                preferences[STAT_KEY] = statValue
                preferences[WEAPON_ATK_KEY] = weaponAtkValue
                preferences[TICK_KEY] = tickValue
                preferences[PTRAIN_CLASS_KEY] = ptrainClassValue
                preferences[TRAIN_STYLE_KEY] = trainStyleValue
                preferences[ATK_STYLE_KEY] = atkStyleValue
                preferences[MOB_KEY] = mobValue
                preferences[HOURS_KEY] = hoursValue
                preferences[STAT_GOAL_KEY] = statGoalValue
                preferences[DEFENSE_KEY] = defenseValue
                preferences[ARMOR_SHIELD_KEY] = shieldArmorValue
                preferences[ARMOR_OTHER_KEY] = otherArmorValue
                preferences[MAX_MOB_ATTACK_KEY] = maxMobAttackValue
                preferences[MIN_MOB_ATTACK_KEY] = minMobAttackValue
                preferences[CRIT_RING_KEY] = critRingValue
            }
        }


        when(trainstyleValue) {
            0 -> {
                binding.root.findViewById<Spinner>(R.id.classspinner).visibility = View.GONE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.baselevelLayout).visibility = View.VISIBLE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.statLayout).visibility = View.VISIBLE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.statgoalLayout).visibility = View.GONE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.weaponatkLayout).visibility = View.VISIBLE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.tickLayout).visibility = View.GONE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.hoursLayout).visibility = View.GONE
                binding.root.findViewById<Spinner>(R.id.atkstylespinner).visibility = View.GONE
                binding.root.findViewById<Spinner>(R.id.mobspinner).visibility = View.GONE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.defenseLayout).visibility = View.GONE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.shieldArmorLayout).visibility = View.GONE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.otherArmorLayout).visibility = View.GONE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.mobMaxAtkLayout).visibility = View.GONE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.mobMinAtkLayout).visibility = View.GONE
                binding.root.findViewById<com.google.android.material.materialswitch.MaterialSwitch>(R.id.critring).visibility = View.VISIBLE
                if (binding.root.findViewById<EditText>(R.id.baselevel).text.toString() != "" && binding.root.findViewById<EditText>(
                        R.id.stat
                    ).text.toString() != "" && binding.root.findViewById<EditText>(R.id.weaponatk).text.toString() != ""
                ) {
                    train()
                }
            }

            1 -> {
                binding.root.findViewById<Spinner>(R.id.classspinner).visibility = View.VISIBLE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.baselevelLayout).visibility = View.VISIBLE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.statLayout).visibility = View.VISIBLE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.statgoalLayout).visibility = View.GONE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.weaponatkLayout).visibility = View.VISIBLE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.tickLayout).visibility = View.VISIBLE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.hoursLayout).visibility = View.GONE
                binding.root.findViewById<Spinner>(R.id.atkstylespinner).visibility = View.GONE
                binding.root.findViewById<Spinner>(R.id.mobspinner).visibility = View.GONE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.defenseLayout).visibility = View.GONE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.shieldArmorLayout).visibility = View.GONE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.otherArmorLayout).visibility = View.GONE
                binding.root.findViewById<com.google.android.material.materialswitch.MaterialSwitch>(R.id.critring).visibility = View.VISIBLE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.mobMaxAtkLayout).visibility = View.GONE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.mobMinAtkLayout).visibility = View.GONE
                if (binding.root.findViewById<EditText>(R.id.baselevel).text.toString() != "" && binding.root.findViewById<EditText>(
                        R.id.stat
                    ).text.toString() != "" && binding.root.findViewById<EditText>(R.id.weaponatk).text.toString() != ""
                ) {
                    ptrain()
                }
            }

            2 -> {
                binding.root.findViewById<Spinner>(R.id.classspinner).visibility = View.GONE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.baselevelLayout).visibility = View.GONE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.statLayout).visibility = View.VISIBLE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.statgoalLayout).visibility = View.VISIBLE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.weaponatkLayout).visibility = View.GONE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.tickLayout).visibility = View.GONE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.hoursLayout).visibility = View.VISIBLE
                binding.root.findViewById<Spinner>(R.id.atkstylespinner).visibility = View.GONE
                binding.root.findViewById<Spinner>(R.id.mobspinner).visibility = View.GONE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.defenseLayout).visibility = View.GONE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.shieldArmorLayout).visibility = View.GONE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.otherArmorLayout).visibility = View.GONE
                binding.root.findViewById<com.google.android.material.materialswitch.MaterialSwitch>(R.id.critring).visibility = View.GONE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.mobMaxAtkLayout).visibility = View.GONE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.mobMinAtkLayout).visibility = View.GONE
                if (binding.root.findViewById<EditText>(R.id.stat).text.toString() != "") {
                    offline()
                }
            }

            3 -> {
                binding.root.findViewById<Spinner>(R.id.classspinner).visibility = View.GONE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.baselevelLayout).visibility = View.VISIBLE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.statLayout).visibility = View.VISIBLE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.statgoalLayout).visibility = View.GONE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.weaponatkLayout).visibility = View.VISIBLE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.tickLayout).visibility = View.GONE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.hoursLayout).visibility = View.GONE
                binding.root.findViewById<Spinner>(R.id.atkstylespinner).visibility = View.VISIBLE
                binding.root.findViewById<Spinner>(R.id.mobspinner).visibility = View.VISIBLE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.defenseLayout).visibility = View.GONE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.shieldArmorLayout).visibility = View.GONE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.otherArmorLayout).visibility = View.GONE
                binding.root.findViewById<com.google.android.material.materialswitch.MaterialSwitch>(R.id.critring).visibility = View.VISIBLE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.mobMaxAtkLayout).visibility = View.GONE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.mobMinAtkLayout).visibility = View.GONE
                if (binding.root.findViewById<EditText>(R.id.baselevel).text.toString() != "" && binding.root.findViewById<EditText>(
                        R.id.stat
                    ).text.toString() != "" && binding.root.findViewById<EditText>(R.id.weaponatk).text.toString() != ""
                ) {
                    dmg()
                }
            }
            4->{
                binding.root.findViewById<Spinner>(R.id.classspinner).visibility = View.VISIBLE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.baselevelLayout).visibility = View.VISIBLE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.statLayout).visibility = View.GONE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.statgoalLayout).visibility = View.GONE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.weaponatkLayout).visibility = View.GONE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.tickLayout).visibility = View.GONE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.hoursLayout).visibility = View.GONE
                binding.root.findViewById<Spinner>(R.id.atkstylespinner).visibility = View.GONE
                binding.root.findViewById<Spinner>(R.id.mobspinner).visibility = View.GONE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.defenseLayout).visibility = View.VISIBLE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.shieldArmorLayout).visibility = View.VISIBLE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.otherArmorLayout).visibility = View.VISIBLE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.mobMinAtkLayout).visibility = View.VISIBLE
                binding.root.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.mobMaxAtkLayout).visibility = View.VISIBLE
                binding.root.findViewById<com.google.android.material.materialswitch.MaterialSwitch>(R.id.critring).visibility = View.GONE
                if (binding.root.findViewById<EditText>(R.id.baselevel).text.toString() != ""  && binding.root.findViewById<EditText>(R.id.defense).text.toString() != "" && binding.root.findViewById<EditText>(R.id.otherArmor).text.toString() != "" && binding.root.findViewById<EditText>(R.id.shieldArmor).text.toString() != "" &&
                    binding.root.findViewById<EditText>(R.id.mobMaxAtk).text.toString() != "" && binding.root.findViewById<EditText>(R.id.mobMinAtk).text.toString() != ""
                ) {
                    defense()
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
        val max_raw_crit_damage: Double = max_raw_crit_damage_Calc(max_raw_damage,binding.critring.isChecked)
        var accuracy = 0.0
        var str0=""

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
                accuracy_Calc(max_raw_crit_damage, max_raw_damage, min_raw_damage, x,binding.critring.isChecked)
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
            average_damage_Calc(accuracy, max_damage, min_damage, max_crit_damage,binding.critring.isChecked)
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
            getString(R.string.traineffec) + mobs.get(pos).mob_name /*+ mobs.get(pos)
                .getEmoji_code()*/
        }!
            
            """.trimIndent()
        if (!onemob) {
            val time2: Double = time_to_kill_Calc(avgdmg, pos + 1)
            str0 = """
                ${
                getString(R.string.traineffec) + mobs.get(pos).mob_name /*+ mobs.get(pos)
                    .getEmoji_code()*/ + " & " + mobs.get(pos + 1).mob_name /*+ mobs.get(pos + 1)
                    .getEmoji_code()*/
            }!
                
                """.trimIndent()
            str0+= """
                ${
                getString(R.string.averagetime) + mobs.get(pos + 1).mob_name /*+ mobs.get(pos + 1)
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
                max_raw_crit_damage_Calc(new_max_raw_damage,binding.critring.isChecked)
            new_max_damage = max_damage_Calc(new_max_raw_damage, newpos)
            newaccuracy = accuracy_Calc(
                new_max_raw_critdamage,
                new_max_raw_damage,
                new_min_raw_damage,
                newpos,
                binding.critring.isChecked
            )
            if (new_max_damage >= 1 && !checked) { //if you can already deal damage to the next mob
                str0+=
                    getString(R.string.youcandeal) + new_max_damage.toInt() + getString(R.string.maxdmg2) + mobs.get(
                        newpos
                    ).mob_name /*+ mobs.get(newpos).getEmoji_code()*/ + "!\n" //part of output
                alrdealdamage = true
            } else if (new_max_damage > 1 && !alrdealdamage && !dealdamage) { //if you cant deal damage to the next mob yet, you can deal damage in a certain amount of stats!
                str0 +=
                    getString(R.string.youcandeal) + new_max_damage.toInt() + getString(R.string.maxdmg2) + mobs.get(
                        newpos
                    ).mob_name /*+ mobs.get(newpos)
                        .getEmoji_code()*/ + getString(R.string.inXstats1) + statadd + getString(R.string.inXstats2) //part of output
                dealdamage = true
            }
            checked = true
            statadd++
        }
        if (checknextmob) {
            str0+=
                """${getString(R.string.maxdmg1) + max_damage.toInt() + " " /*+ */} Tickrate: ${tickrate.toInt()} / 3600
"""
            str0 += """
                ${
                getString(R.string.averagetime) + mobs.get(pos).mob_name /*+ mobs.get(pos)
                    .getEmoji_code()*/
            }: ${time.toInt() / 60} min. ${time.toInt() % 60} sec.
                
                """.trimIndent()
            str0 += """
                ${
                getString(R.string.youneednext1) + statadd +getString(R.string.youneednext2train) + mobs.get(newpos)
                    .mob_name /*+ mobs.get(newpos).getEmoji_code()*/
            }!
                
                """.trimIndent()
        } else {
            str0 +=
                """${getString(R.string.mindmgauto) + min_damage.toInt() + " • " /*+ slime_lord_emoji*/}${getString(R.string.maxdmgauto)}${max_damage.toInt()}
"""
            str0 += """
                ${
                getString(R.string.averagetime) + mobs.get(pos).mob_name /*+ mobs.get(pos)
                    .getEmoji_code()*/
            }: ${time.toInt() / 60} min. ${time.toInt() % 60} sec.
                
                """.trimIndent()
        }
        binding.root.findViewById<TextView>(R.id.str0).text = str0
    }
    private fun ptrain(){
        var str0: String // You can power train effectively on...
        var classEmoji = ""

        val stat1 = binding.root.findViewById<EditText>(R.id.stat).text.toString().toDouble()
        val weaponatk = binding.root.findViewById<EditText>(R.id.weaponatk).text.toString().toDouble()
        val base = binding.root.findViewById<EditText>(R.id.baselevel).text.toString().toDouble()
        val classtype = binding.root.findViewById<Spinner>(R.id.classspinner).selectedItemPosition
        var tick = binding.root.findViewById<EditText>(R.id.tick).text.toString().toDouble()
        if(binding.root.findViewById<EditText>(R.id.tick).text.toString()!=""){
            tick = binding.root.findViewById<EditText>(R.id.tick).text.toString().toDouble() //Change this later
        }

        val max_raw_damage: Double
        val min_raw_damage: Double

        if (classtype === 2) {
            min_raw_damage = special_magic_min_raw_damage_Calc(stat1.toDouble(), weaponatk, base)
            max_raw_damage = special_magic_max_raw_damage_Calc(stat1.toDouble(), weaponatk, base)
            classEmoji = getString(R.string.magic)
        } else {
            min_raw_damage = special_meldist_min_raw_damage_Calc(stat1.toDouble(), weaponatk, base)
            max_raw_damage = special_meldist_max_raw_damage_Calc(stat1.toDouble(), weaponatk, base)
            classEmoji = if (classtype === 0) {
                getString(R.string.melee)
            } else {
                getString(R.string.distance)
            }
        }

        val max_raw_crit_damage = max_raw_crit_damage_Calc(max_raw_damage,binding.critring.isChecked)
        var accuracy = 0.0
        var pos = 0
        val threshold = threshold_Calc(tick)

        //Iterate through loop until you find a mob you can power train on with greater than .1749 accuracy

        //Iterate through loop until you find a mob you can power train on with greater than .1749 accuracy
        for (x in mobs.size - 1 downTo 0) {
            if (x == 13 || x == 19 || x == 20 || x == 22 || x == 24 || x == 25 || x == 26 || x == 29 || x == 31 || x == 33 || x == 36 || x == 37 || x >= 39) {
                continue
            }
            accuracy = accuracy_Calc(max_raw_crit_damage, max_raw_damage, min_raw_damage, x,binding.critring.isChecked)
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
        val avgdmg = average_damage_Calc(accuracy, max_damage, min_damage, max_crit_damage,binding.critring.isChecked)
        val totalaccuracy = total_accuracy_Calc(accuracy, tick)
        val maxtickrate: Double = max_tickrate_Calc(tick).toDouble()
        val powertickrate = powertickrate_Calc(totalaccuracy, maxtickrate)
        val time = time_to_kill_Calc(avgdmg, pos)

        str0 =
            """
            ${getString(R.string.ptraineffec1) + classEmoji + getString(R.string.ptraineffec2) + mobs[pos].mob_name /*+ mobs[pos].getEmoji_code()*/}!
            
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
            val new_max_raw_crit_damage = max_raw_crit_damage_Calc(new_max_raw_damage,binding.critring.isChecked)
            new_max_damage = max_damage_Calc(new_max_raw_damage, newpos)
            newaccuracy = accuracy_Calc(
                new_max_raw_crit_damage,
                new_max_raw_damage,
                new_min_raw_damage,
                newpos,
                binding.critring.isChecked
            )
            if (new_max_damage >= 1 && !checked) { //if you can already deal damage to the next mob
                str0 +=
                    getString(R.string.youcandeal) + new_max_damage.toInt() + getString(R.string.maxdmg2) + mobs[newpos].mob_name /*+ mobs[newpos].getEmoji_code()*/ + "!" //part of output
                alrdealdamage = true
            } else if (new_max_damage > 1 && !alrdealdamage && !dealdamage) { //if you cant deal damage to the next mob yet, you can deal damage in a certain amount of stats!
                str0 +=
                    getString(R.string.youcandeal) + new_max_damage.toInt() + getString(R.string.maxdmg2) + mobs[newpos].mob_name /*+ mobs[newpos].getEmoji_code()*/ + getString(R.string.inXstats1) + statadd + getString(R.string.inXstats2) //part of output
                dealdamage = true
            }
            checked = true
            statadd++
        }

        //Building remaining Strings

        //Building remaining Strings
        if (checknextmob) {
            str0 +=
                """${getString(R.string.maxdmg1) + max_damage.toInt() + " • "}${getString(R.string.tickrate)}${powertickrate.toInt()} / ${maxtickrate.toInt()}
"""
            str0 +=
                """
        ${getString(R.string.averagetime) + mobs[pos].mob_name /*+ mobs[pos].getEmoji_code()*/}: ${time.toInt() / 60} min. ${time.toInt() % 60} sec.
        
        """.trimIndent()
            str0 +=
                """
        ${getString(R.string.youneednext1) + statadd + getString(R.string.youneednext2ptrain) + mobs[newpos].mob_name /*+ mobs[newpos].getEmoji_code()*/}!
        
        """.trimIndent()
        } else {
            str0 +=
                """${getString(R.string.maxdmgauto) + max_damage.toInt() + " • "} ${getString(R.string.mindmgauto)} ${min_damage.toInt()}
"""
            str0 +=
                """
        ${getString(R.string.averagetime) + mobs[pos].mob_name /*+ mobs[pos].getEmoji_code()*/}: ${time.toInt() / 60} min. ${time.toInt() % 60} sec.
        
        """.trimIndent()
        }
        binding.root.findViewById<TextView>(R.id.str0).text = str0
    }

    @SuppressLint("SetTextI18n")
    private fun offline(){
        var stat1 = 0.toDouble()
        if(binding.root.findViewById<EditText>(R.id.stat).text.toString()!=""){
            stat1 = binding.root.findViewById<EditText>(R.id.stat).text.toString().toDouble()
        }
        var stat2 = 0.toDouble()
        if(binding.root.findViewById<EditText>(R.id.statgoal).text.toString()!=""){
            stat2 = binding.root.findViewById<EditText>(R.id.statgoal).text.toString().toDouble()
        }
        var hours = 0.toDouble()
        if(binding.root.findViewById<EditText>(R.id.hours).text.toString()!=""){
            hours = binding.root.findViewById<EditText>(R.id.hours).text.toString().toDouble()
        }
        if (stat2 > 0 && hours <= 0) {
            if (stat1 > stat2) {
                binding.root.findViewById<TextView>(R.id.str0).text=getString(R.string.statgoaltoolow)
            }else {
                val ticks1: Double
                val ticks2: Double
                if (stat1 <= 54) {
                    ticks1 = stat55to99_Calc(stat1.toDouble())
                } else {
                    ticks1 = stat55to99_Calc(stat1.toDouble())
                }
                if (stat2 <= 54) {
                    ticks2 = stat55to99_Calc(stat2.toDouble())
                } else {
                    ticks2 = stat55to99_Calc(stat2.toDouble())
                }
                val totalticks = ticks2 - ticks1
                binding.root.findViewById<TextView>(R.id.str0).text =
                    getString(R.string.offlineoutput1) + String.format(
                        "%,.0f",
                        totalticks
                    ) + getString(R.string.offlineoutput2) + java.lang.String.format(
                        "%,.1f",
                        stat2
                    ) + "!\n" +
                            getString(R.string.offlineoutput3) + String.format(
                        "%,.1f",
                        totalticks * 60 / 600
                    ) + getString(R.string.offlineoutput4) + String.format(
                        "%,.1f",
                        totalticks / 600
                    ) + getString(R.string.offlineoutput5)
            }
        } else if (hours > 0 && stat2 <= 0) {
            val tickstrained: Double = 600 * hours
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
                binding.root.findViewById<TextView>(R.id.str0).text=getString(R.string.offlineerror)
            }

            binding.root.findViewById<TextView>(R.id.str0).text=
                "${getString(R.string.offlinenewstat1)}${newStat.toInt()}${getString(R.string.offlinenewstat2)}${hours.toInt()} ${getString(R.string.offlineoutput5)}"

        } else {
            binding.root.findViewById<TextView>(R.id.str0).text=getString(R.string.offlineinvalidinput)
        }
    }
    private fun dmg() {
        val base = binding.root.findViewById<EditText>(R.id.baselevel).text.toString().toDouble()
        val stat1 = binding.root.findViewById<EditText>(R.id.stat).text.toString().toDouble()
        val weaponatk = binding.root.findViewById<EditText>(R.id.weaponatk).text.toString().toDouble()
        val attacktype = binding.root.findViewById<Spinner>(R.id.atkstylespinner).selectedItemPosition
        val sortedMobs = mobs.sortedBy { it.mob_level }
        val mob = mobs.indexOf(sortedMobs[binding.root.findViewById<Spinner>(R.id.mobspinner).selectedItemPosition])
        val str1: String
        val str2: String
        var attacktypestring = ""
        val max_raw_damage: Double
        val min_raw_damage: Double
        if (attacktype == 0) {
            min_raw_damage =
                auto_min_raw_damage_Calc(stat1.toDouble(), weaponatk.toDouble(), base.toDouble())
            max_raw_damage =
                auto_max_raw_damage_Calc(stat1.toDouble(), weaponatk.toDouble(), base.toDouble())
            attacktypestring = getString(R.string.auto_attack)
        } else if (attacktype == 3) {
            min_raw_damage = special_magic_min_raw_damage_Calc(
                stat1.toDouble(),
                weaponatk.toDouble(),
                base.toDouble()
            )
            max_raw_damage = special_magic_max_raw_damage_Calc(
                stat1.toDouble(),
                weaponatk.toDouble(),
                base.toDouble()
            )
            attacktypestring = getString(R.string.special_attack)+ " 🔥"
        } else {
            min_raw_damage = special_meldist_min_raw_damage_Calc(
                stat1.toDouble(),
                weaponatk.toDouble(),
                base.toDouble()
            )
            max_raw_damage = special_meldist_max_raw_damage_Calc(
                stat1.toDouble(),
                weaponatk.toDouble(),
                base.toDouble()
            )
            attacktypestring = if (attacktype == 1) {
                getString(R.string.special_attack)+ " ⚔️"
            } else {
                getString(R.string.special_attack)+ " 🏹"
            }
        }
        val max_raw_crit_damage = max_raw_crit_damage_Calc(max_raw_damage,binding.critring.isChecked)
        val min_damage = min_damage_Calc(min_raw_damage, mob)
        val max_damage = max_damage_Calc(max_raw_damage, mob)
        val max_crit_damage = max_crit_damage_Calc(max_raw_crit_damage, mob)
        val normalaccuracy = normal_accuracy_Calc(max_raw_damage, min_raw_damage, mob)
        val critaccuracy = crit_accuracy_Calc(max_raw_crit_damage, max_raw_damage, mob)
        val str0: String = """${getString(R.string.mob)+": " + mobs[mob].mob_name}
             """.trimIndent()
        str1 = if (normalaccuracy == 1.00) {
            """${getString(R.string.mindmg)} ($attacktypestring) ${min_damage.toInt()}
${getString(R.string.maxdmg1)} ($attacktypestring) ${max_damage.toInt()}
            """.trimIndent()
        } else if (normalaccuracy > 0) {
            """${getString(R.string.maxdmg1)}($attacktypestring) ${max_damage.toInt()}
            """.trimIndent()
        } else {
            getString(R.string.not_strong_enough)
        }
        str2 = if (critaccuracy > 0) {
            """
     ${getString(R.string.maxcritdmg)} ($attacktypestring) ${max_crit_damage.toInt()}
     
     """.trimIndent()
        } else {
            getString(R.string.not_strong_enough_crit)
        }
        val oneShotData = oneshot(attacktype, mob, base.toInt(), stat1.toInt(),0,weaponatk.toInt())
        binding.str0.text=str0 + "\n" + str1 + "\n" + str2 + "\n" + oneShotData

    }
    private fun defense() {
            val base = binding.root.findViewById<EditText>(R.id.baselevel).text.toString().toDouble()
            val defense = binding.root.findViewById<EditText>(R.id.defense).text.toString().toDouble()
            var armorShield = binding.root.findViewById<EditText>(R.id.shieldArmor).text.toString().toDouble()
            val classtype = binding.root.findViewById<Spinner>(R.id.classspinner).selectedItemPosition
            if(classtype!=0){
                armorShield=10.0
            }
            val armorOther = binding.root.findViewById<EditText>(R.id.otherArmor).text.toString().toDouble()
            val sortedMobs = mobs.sortedBy { it.mob_level }
            val mob = mobs.indexOf(sortedMobs[binding.root.findViewById<Spinner>(R.id.mobspinner).selectedItemPosition])
            val str1: String
            val str2: String
            val max_raw_damage= binding.root.findViewById<EditText>(R.id.mobMaxAtk).text.toString().toDouble() //TODO replace with actual mob values
            val min_raw_damage= binding.root.findViewById<EditText>(R.id.mobMinAtk).text.toString().toDouble() //TODO replace with actual mob values
            val high_defense = high_defense_calc(base, defense, armorShield, armorOther)
            val low_defense = low_defense_calc(base, defense, armorShield, armorOther)
            val normalaccuracy = defense_accuracy_Calc(mobs[mob].mob_defense.toDouble(), low_defense, high_defense)
            val min_damage_high_def = min_damage_defense_Calc(high_defense,min_raw_damage.toInt())
            val max_damage_high_def = max_damage_defense_Calc(high_defense,max_raw_damage.toInt())
            val str0: String = """${getString(R.string.mob)+" " + mobs[mob].mob_name}
             """.trimIndent()
            str1 = if (max_damage_high_def > 0) {
                """${getString(R.string.high_def)}
${getString(R.string.mindmg)} ${min_damage_high_def.toInt()}
${getString(R.string.maxdmg1)}${max_damage_high_def.toInt()}
            """.trimIndent()
            } else {
                """${getString(R.string.high_def)}${getString(R.string.can_not_hurt_you)}"""
            }
            val min_damage_low_def = min_damage_defense_Calc(low_defense,min_raw_damage.toInt())
            val max_damage_low_def = max_damage_defense_Calc(low_defense,max_raw_damage.toInt())
            val low_defense_accuracy = defense_accuracy_Calc(mobs[mob].mob_defense.toDouble(), min_damage_low_def.toDouble(), max_damage_low_def.toDouble())
            str2 = if (max_damage_low_def > 0) {
                """${getString(R.string.low_def)}
${getString(R.string.mindmg)} ${min_damage_low_def.toInt()}
${getString(R.string.maxdmg1)}${max_damage_low_def.toInt()}
        """.trimIndent()
                } else {
                """${getString(R.string.low_def)}${getString(R.string.can_not_hurt_you)}"""
                }
            binding.str0.text=str0 + "\n" + str1 + "\n" + str2
    }

    fun oneshot(
        attacktype: Int,
        mob: Int,
        base: Int,
        stat: Int,
        buff: Int,
        weaponatk: Int
    ): String {
        var str0 = ""
        var currentConsistency = 0.0

        var min_raw_damage = 0.0
        var max_raw_damage = 0.0
        var max_raw_crit_damage = 0.0

        if (stat >= 5) {
            val stat1 = stat + buff
            when (attacktype) {
                0 -> {
                    min_raw_damage = Formulas.auto_min_raw_damage_Calc(
                        stat1.toDouble(),
                        weaponatk.toDouble(),
                        base.toDouble()
                    )
                    max_raw_damage = Formulas.auto_max_raw_damage_Calc(
                        stat1.toDouble(),
                        weaponatk.toDouble(),
                        base.toDouble()
                    )
                    max_raw_crit_damage = Formulas.max_raw_crit_damage_Calc(
                        max_raw_damage,
                        binding.critring.isChecked
                    )
                }

                3 -> {
                    min_raw_damage = Formulas.special_magic_min_raw_damage_Calc(
                        stat1.toDouble(),
                        weaponatk.toDouble(),
                        base.toDouble()
                    )
                    max_raw_damage = Formulas.special_magic_max_raw_damage_Calc(
                        stat1.toDouble(),
                        weaponatk.toDouble(),
                        base.toDouble()
                    )
                    max_raw_crit_damage = Formulas.max_raw_crit_damage_Calc(
                        max_raw_damage,
                        binding.critring.isChecked
                    )
                }

                else -> {
                    min_raw_damage = Formulas.special_meldist_min_raw_damage_Calc(
                        stat1.toDouble(),
                        weaponatk.toDouble(),
                        base.toDouble()
                    )
                    max_raw_damage = Formulas.special_meldist_max_raw_damage_Calc(
                        stat1.toDouble(),
                        weaponatk.toDouble(),
                        base.toDouble()
                    )
                    max_raw_crit_damage = Formulas.max_raw_crit_damage_Calc(
                        max_raw_damage,
                        binding.critring.isChecked
                    )
                }
            }

            currentConsistency =
                consistency_Calc(max_raw_crit_damage, max_raw_damage, min_raw_damage, mob)
            if (currentConsistency > 0) {
                str0 =
                    "${getString(R.string.can_oneshot)}${mobs[mob].mob_name}${getString(R.string.oneshot_consistency_1)}${(currentConsistency * 100).toInt()}${getString(R.string.oneshot_consistency_2)}"
            } else {
                str0 =
                    "${getString(R.string.can_not_oneshot_1)}${mobs[mob].mob_name}${getString(R.string.can_not_oneshot_2)}"
            }
        }
        return str0
    }
}

