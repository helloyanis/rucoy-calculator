package com.helloyanis.rucoycalculator.ui.train

import androidx.lifecycle.ViewModel

class TrainViewModel : ViewModel() {

}
object Formulas {
    fun auto_min_raw_damage_Calc(stat: Double, weaponatk: Double, base: Double): Double {
        return stat * weaponatk / 20 + base / 4
    }

    fun auto_max_raw_damage_Calc(stat: Double, weaponatk: Double, base: Double): Double {
        return stat * weaponatk / 10 + base / 4
    }

    fun special_meldist_min_raw_damage_Calc(stat: Double, weaponatk: Double, base: Double): Double {
        return 1.5 * (stat * weaponatk / 20 + base / 4)
    }

    fun special_meldist_max_raw_damage_Calc(stat: Double, weaponatk: Double, base: Double): Double {
        return 1.5 * (stat * weaponatk / 10 + base / 4)
    }

    fun special_magic_min_raw_damage_Calc(stat: Double, weaponatk: Double, base: Double): Double {
        return 1.5 * (1.05 * stat * weaponatk / 20 + 9 * base / 32)
    }

    fun special_magic_max_raw_damage_Calc(stat: Double, weaponatk: Double, base: Double): Double {
        return 1.5 * (1.05 * stat * weaponatk / 10 + 9 * base / 32)
    }

    fun min_damage_Calc(min_raw_damage: Double, pos: Int): Double {
        var min_damage: Double = min_raw_damage - mobs.get(pos).mob_defense
        if (min_damage < 0) {
            min_damage = 0.0
        }
        return min_damage
    }

    fun max_damage_Calc(max_raw_damage: Double, pos: Int): Double {
        return max_raw_damage - mobs.get(pos).mob_defense
    }

    fun max_raw_crit_damage_Calc(max_raw_damage: Double): Double {
        return max_raw_damage * 1.05
    }

    fun max_crit_damage_Calc(max_raw_crit_damage: Double, pos: Int): Double {
        return max_raw_crit_damage - mobs.get(pos).mob_defense
    }

    fun normal_accuracy_Calc(max_raw_damage: Double, min_raw_damage: Double, x: Int): Double {
        var normalaccuracy: Double =
            (max_raw_damage - mobs.get(x).mob_defense) / (max_raw_damage - min_raw_damage)
        if (normalaccuracy > 1.00) {
            normalaccuracy = 1.00
        }
        return normalaccuracy
    }

    fun crit_accuracy_Calc(max_raw_crit_damage: Double, max_raw_damage: Double, x: Int): Double {
        var critaccuracy: Double = (max_raw_crit_damage - mobs.get(x)
            .mob_defense) / (max_raw_crit_damage - max_raw_damage)
        if (critaccuracy > 1.00) {
            critaccuracy = 1.00
        }
        return critaccuracy
    }

    fun accuracy_Calc(
        max_raw_crit_damage: Double,
        max_raw_damage: Double,
        min_raw_damage: Double,
        x: Int
    ): Double {
        return normal_accuracy_Calc(max_raw_damage, min_raw_damage, x) * 0.99 + crit_accuracy_Calc(
            max_raw_crit_damage,
            max_raw_damage,
            x
        ) * 0.01
    }

    fun total_accuracy_Calc(accuracy: Double, tick: Double): Double {
        return 1.0 - Math.pow(Math.pow(1.0 - accuracy, tick), 10.0)
    }

    fun time_to_kill_Calc(avgdmg: Double, pos: Int): Double {
        return mobs.get(pos).mob_health / avgdmg
    }

    fun average_damage_Calc(
        accuracy: Double,
        max_damage: Double,
        min_damage: Double,
        max_crit_damage: Double
    ): Double {
        return accuracy * (.99 * ((max_damage + min_damage) / 2)) + 0.01 * ((max_crit_damage + max_damage) / 2)
    }

    fun tickrate_Calc(accuracy: Double, maxtickrate: Double): Double {
        return maxtickrate * (1.0 - Math.pow(1.0 - accuracy, 10.0))
    }

    fun powertickrate_Calc(totalaccuracy: Double, maxtickrate: Double): Double {
        return maxtickrate * totalaccuracy
    }

    fun max_tickrate_Calc(tick: Double): Number {
        return if (tick <= 5) {
            tick * 3600
        } else {
            18000
        }
    }

    fun exp_Calc(base: Double): Double {
        return Math.pow(base, base / 1000 + 3)
    }

    fun stat0to54_Calc(stat: Double): Double {
        return Math.pow(stat, stat / 1000 + 2.373)
    }

    fun stat55to99_Calc(stat: Double): Double {
        // final double offset = 4692.687;
        // double adjustedStat = stat + 15.68952;
        // return Math.pow(adjustedStat, (adjustedStat/1000) + 2.272) - offset;
        return Math.pow(stat, stat / 1000 + 2.171)
    }

    /*
    public static double stat100to599_Calc(double stat){
        final double offset = 22516.303;
        double adjustedStat = stat + 45.43406;
        // return Math.pow(adjustedStat, (adjustedStat/1000) + 2.171) - offset;
        return Math.pow(stat, (stat/1000) + 2.373);
    }
    public static double stat600plus_Calc(double stat){
        final double offset = 2766484;
        double adjustedStat = stat + 110.34322;
        return Math.pow(adjustedStat, (adjustedStat/1000) + 2.070) - offset;
    }
    */
    fun findStatLevel_Calc(ticks2: Double): Double {
        if (ticks2 <= stat0to54_Calc(54.0)) {
            for (stat in 5..54) {
                if (ticks2 <= stat0to54_Calc(stat.toDouble())) {
                    val fract =
                        (ticks2 - stat0to54_Calc((stat - 1).toDouble())) / (stat0to54_Calc(stat.toDouble()) - stat0to54_Calc(
                            (stat - 1).toDouble()
                        ))
                    return stat - 1 + fract
                }
            }
        } else { // if (ticks2 <= stat55to99_Calc(99)){
            for (stat in 55..1000) { // 99; stat++){
                if (ticks2 <= stat55to99_Calc(stat.toDouble())) {
                    val fract =
                        (ticks2 - stat55to99_Calc((stat - 1).toDouble())) / (stat55to99_Calc(stat.toDouble()) - stat55to99_Calc(
                            (stat - 1).toDouble()
                        ))
                    return stat - 1 + fract
                }
            }
        }
        /*
        else if (ticks2 <= stat100to599_Calc(599)) {
            for (int stat = 100; stat <= 599; stat++){
                if (ticks2 <= stat100to599_Calc(stat)){
                    double fract = (ticks2 - stat100to599_Calc(stat-1))/(stat100to599_Calc(stat) - stat100to599_Calc(stat-1));
                    return (stat-1) + fract;
                }
            }
        } else {
            for (int stat = 600; stat <= 1000; stat++){
                if (ticks2 <= stat600plus_Calc(stat)){
                    double fract = (ticks2 - stat600plus_Calc(stat-1))/(stat600plus_Calc(stat) - stat600plus_Calc(stat-1));
                    return (stat-1) + fract;
                }
            }
        }
        */return (-1).toDouble()
    }

    fun threshold_Calc(tick: Double): Double {
        return 1.0 - Math.pow(.8251, 1.0 / tick)
    }

    fun consistency_Calc(
        max_raw_crit_damage: Double,
        max_raw_damage: Double,
        min_raw_damage: Double,
        mob: Int
    ): Any {
        val health: Int = mobs.get(mob).mob_health
        val defense: Int = mobs.get(mob).mob_defense
        val totaldefense = health + defense
        if (totaldefense - max_raw_crit_damage > 0) {
            return 0
        }
        val range = max_raw_damage - min_raw_damage
        val normaloneshots = max_raw_damage - totaldefense
        return if (normaloneshots > 0) {
            val normalconsistency = normaloneshots / range
            normalconsistency * 0.99 + 0.01
        } else {
            val critrange = max_raw_crit_damage - max_raw_damage
            val criticaloneshots = max_raw_crit_damage - totaldefense
            criticaloneshots / critrange * 0.01
        }
    }
}
val mobs = arrayOf(
    Mob("Rat Lv.1", "<:1_rat:781390912248348683>", 4, 25),
    Mob("Rat Lv.3", "<:3_rat:781390923753717781>", 7, 35),
    Mob("Crow Lv.6", "<:6_crow:781390951191937024>", 13, 40),
    Mob("Wolf Lv.9", "<:9_wolf:781390959735996436>", 17, 50),
    Mob("Scorpion Lv.12", "<:12_scorpion:781390969824215052>", 18, 50),
    Mob("Cobra Lv.13", "<:14_cobra:781390978988507158>", 18, 50),
    Mob("Worm Lv.14", "<:14_worm:781390986614014004>", 19, 55),
    Mob("Goblin Lv.15", "<:15_goblin:781390994376622140>", 21, 60),
    Mob("Mummy Lv.25", "<:25_mummy:781391003780513813>", 36, 80),
    Mob("Pharaoh Lv.35", "<:35_pharaoh:781391013591253063>", 51, 100),
    Mob("Assassin Lv.45", "<:45_assassin:781391026727682051>", 71, 120),
    Mob("Assassin Lv.50", "<:50_assassin:781391034990460958>", 81, 140),
    Mob("Assassin Ninja Lv.55", "<:55_ninja_assassin:781391057128390696>", 91, 160),
    Mob("Skeleton Archer Lv.80", "<:80_skeleton_archer:781391133023928359>", 101, 300),
    Mob("Zombie Lv.65", "<:65_zombie:781391115181228069>", 106, 200),
    Mob("Skeleton Lv.75", "<:75_skeleton:781391124929314827>", 121, 300),
    Mob("Skeleton Warrior Lv.90", "<:90_skeleton_warrior:781391140691771393>", 146, 375),
    Mob("Vampire Lv.100", "<:100_vampire:781391148982730763>", 171, 450),
    Mob("Vampire Lv.110", "<:110_vampire:781391156671807508>", 186, 530),
    Mob("Drow Ranger Lv.125", "<:120_drow_ranger:781391163743010867>", 191, 600),
    Mob("Drow Mage Lv. 130", "<:130_drow_mage:781391184470867968>", 191, 600),
    Mob("Drow Assassin Lv.120", "<:125_drow_assassin:781391173088575488>", 221, 620),
    Mob("Drow Sorceress Lv.140", "<:140_drow_sorceress:781391206755336222>", 221, 600),
    Mob("Drow Fighter Lv.135", "<:135_drow_fighter:781391196534472705>", 246, 680),
    Mob("Lizard Archer Lv.160", "<:160_lizard_archer:781391233582497793>", 271, 650),
    Mob("Lizard Shaman Lv.170", "<:170_lizard_shaman:781391275177672715>", 276, 600),
    Mob("Dead Eyes Lv.170", "<:170_dead_eyes:781391266923020309>", 276, 600),
    Mob("Lizard Warrior Lv.150", "<:150_lizard_warrior:781391223604117534>", 301, 680),
    Mob("Djinn Lv.150", "<:150_djinn:781391214833303562>", 301, 640),
    Mob("Lizard High Shaman Lv.190", "<:190_lizard_high_shaman:781391314947538944>", 326, 740),
    Mob("Gargoyle Lv.190", "<:190_gargoyle:781391298598010892>", 326, 740),
    Mob("Dragon Hatchling Lv. 240", "<:240_dragon_hatchling:781391338758471690>", 331, 10000),
    Mob("Lizard Captain lv.180", "<:180_lizard_captain:781391284002488370>", 361, 815),
    Mob("Dragon Lv.250", "<:250_dragon:781391347922894849>", 501, 20000),
    Mob("Minotaur Lv.225", "<:225_minotaur:781391328763314187>", 511, 4250),
    Mob("Minotaur Lv.250", "<:250_minotaur:781391355561246752>", 591, 5000),
    Mob("Dragon Warden Lv.280", "<:280_dragon_warden:781391390575165450>", 626, 30000),
    Mob("Ice Elemental Lv.300", "<:300_ice_elemental:781391399709835267>", 676, 40000),
    Mob("Minotaur Lv.275", "<:275_minotaur:781391363476291597>", 681, 5750),
    Mob("Ice Dragon Lv.320", "<:320_ice_dragon:781391412217249823>", 726, 50000),
    Mob("Yeti Lv.350", "<:350_yeti:781391428030431262>", 826, 60000)
)

private val weapons = arrayOf( /*00*/
    Weapon(
        "Training Weapon(4)",
        "<:4_7_9_11_13_golden_dagger:802411966684987422>",
        "<:4_7_9_11_13_golden_bow:802411945369927711>",
        "<:4_7_9_11_13_golden_wand:802411976001191946>",
        4,
        0
    ),  /*01*/
    Weapon(
        "Training Weapon(5)",
        "<:4_5_15_17_19_dagger:781573394603966504>",
        "<:4_5_15_17_19_bow:781573383217348658>",
        "<:4_5_15_17_19_wand:781573410525413376>",
        5,
        0
    ),  /*02*/
    Weapon(
        "Training Weapon(7)",
        "<:4_7_9_11_13_golden_dagger:802411966684987422>",
        "<:4_7_9_11_13_golden_bow:802411945369927711>",
        "<:4_7_9_11_13_golden_wand:802411976001191946>",
        7,
        0
    ),  /*03*/
    Weapon(
        "Training Weapon(9)",
        "<:4_7_9_11_13_golden_dagger:802411966684987422>",
        "<:4_7_9_11_13_golden_bow:802411945369927711>",
        "<:4_7_9_11_13_golden_wand:802411976001191946>",
        9,
        0
    ),  /*04*/
    Weapon(
        "Training Weapon(11)",
        "<:4_7_9_11_13_golden_dagger:802411966684987422>",
        "<:4_7_9_11_13_golden_bow:802411945369927711>",
        "<:4_7_9_11_13_golden_wand:802411976001191946>",
        11,
        0
    ),  /*05*/
    Weapon(
        "Training Weapon(13)",
        "<:4_7_9_11_13_golden_dagger:802411966684987422>",
        "<:4_7_9_11_13_golden_bow:802411945369927711>",
        "<:4_7_9_11_13_golden_wand:802411976001191946>",
        13,
        0
    ),  /*06*/
    Weapon(
        "Weapon(15)",
        "<:4_5_15_17_19_dagger:781573394603966504>",
        "<:4_5_15_17_19_bow:781573383217348658>",
        "<:4_5_15_17_19_wand:781573410525413376>",
        15,
        0
    ),  /*07*/
    Weapon(
        "Weapon(17)",
        "<:4_5_15_17_19_dagger:781573394603966504>",
        "<:4_5_15_17_19_bow:781573383217348658>",
        "<:4_5_15_17_19_wand:781573410525413376>",
        17,
        0
    ),  /*08*/
    Weapon(
        "Weapon(19)",
        "<:4_5_15_17_19_dagger:781573394603966504>",
        "<:4_5_15_17_19_bow:781573383217348658>",
        "<:4_5_15_17_19_wand:781573410525413376>",
        19,
        0
    ),  /*09*/
    Weapon(
        "Weapon(20)",
        "<:20_22_24_short_sword:781573430142566460>",
        "<:20_22_24_studded_bow:781573439143673922>",
        "<:20_22_24_novice_wand:781573421523402753>",
        20,
        0
    ),  /*10*/
    Weapon(
        "Weapon(22)",
        "<:20_22_24_short_sword:781573430142566460>",
        "<:20_22_24_studded_bow:781573439143673922>",
        "<:20_22_24_novice_wand:781573421523402753>",
        22,
        0
    ),  /*11*/
    Weapon(
        "Weapon(24)",
        "<:20_22_24_short_sword:781573430142566460>",
        "<:20_22_24_studded_bow:781573439143673922>",
        "<:20_22_24_novice_wand:781573421523402753>",
        24,
        0
    ),  /*12*/
    Weapon(
        "Weapon(25)",
        "<:25_27_29_sword:781573471007146035>",
        "<:25_27_29_iron_bow:781573448425275392>",
        "<:25_27_29_priest_wand:781573463285956668>",
        25,
        0
    ),  /*13*/
    Weapon(
        "Weapon(27)",
        "<:25_27_29_sword:781573471007146035>",
        "<:25_27_29_iron_bow:781573448425275392>",
        "<:25_27_29_priest_wand:781573463285956668>",
        27,
        0
    ),  /*14*/
    Weapon(
        "Weapon(29)",
        "<:25_27_29_sword:781573471007146035>",
        "<:25_27_29_iron_bow:781573448425275392>",
        "<:25_27_29_priest_wand:781573463285956668>",
        29,
        0
    ),  /*15*/
    Weapon(
        "Drow Weapon(30)",
        "<:30_32_34_341_broadsword:781573477776883723>",
        "<:30_32_34_341_drow_bow:781573484400607243>",
        "<:30_32_34_341_royal_priest_wand:781573492524449794>",
        30,
        0
    ),  /*16*/
    Weapon(
        "Drow Weapon(32)",
        "<:30_32_34_341_broadsword:781573477776883723>",
        "<:30_32_34_341_drow_bow:781573484400607243>",
        "<:30_32_34_341_royal_priest_wand:781573492524449794>",
        32,
        0
    ),  /*17*/
    Weapon(
        "Drow Weapon(34)",
        "<:30_32_34_341_broadsword:781573477776883723>",
        "<:30_32_34_341_drow_bow:781573484400607243>",
        "<:30_32_34_341_royal_priest_wand:781573492524449794>",
        34,
        0
    ),  /*18*/
    Weapon(
        "Drow Weapon(34+1)",
        "<:30_32_34_341_broadsword:781573477776883723>",
        "<:30_32_34_341_drow_bow:781573484400607243>",
        "<:30_32_34_341_royal_priest_wand:781573492524449794>",
        34,
        1
    ),  /*19*/
    Weapon(
        "Lizard/Gargoyle Weapon(35)",
        "<:35_37_39_391_lizard_slayer:781573548241059950><:35_37_39_391_gargoyle_slayer:781573514564993034>",
        "<:35_37_39_391_lizard_bow:781573535676104736><:35_37_39_391_gargoyle_bow:781573500762193921>",
        "<:35_37_39_391_shaman_wand:781573555975749663><:35_37_39_391_gargoyle_wand:781573524255014942>",
        35,
        0
    ),  /*20*/
    Weapon(
        "Lizard/Gargoyle Weapon(37)",
        "<:35_37_39_391_lizard_slayer:781573548241059950><:35_37_39_391_gargoyle_slayer:781573514564993034>",
        "<:35_37_39_391_lizard_bow:781573535676104736><:35_37_39_391_gargoyle_bow:781573500762193921>",
        "<:35_37_39_391_shaman_wand:781573555975749663><:35_37_39_391_gargoyle_wand:781573524255014942>",
        37,
        0
    ),  /*21*/
    Weapon(
        "Lizard/Gargoyle Weapon(39)",
        "<:35_37_39_391_lizard_slayer:781573548241059950><:35_37_39_391_gargoyle_slayer:781573514564993034>",
        "<:35_37_39_391_lizard_bow:781573535676104736><:35_37_39_391_gargoyle_bow:781573500762193921>",
        "<:35_37_39_391_shaman_wand:781573555975749663><:35_37_39_391_gargoyle_wand:781573524255014942>",
        39,
        0
    ),  /*22*/
    Weapon(
        "Lizard/Gargoyle Weapon(39+1)",
        "<:35_37_39_391_lizard_slayer:781573548241059950><:35_37_39_391_gargoyle_slayer:781573514564993034>",
        "<:35_37_39_391_lizard_bow:781573535676104736><:35_37_39_391_gargoyle_bow:781573500762193921>",
        "<:35_37_39_391_shaman_wand:781573555975749663><:35_37_39_391_gargoyle_wand:781573524255014942>",
        39,
        1
    ),  /*23*/
    Weapon(
        "Dragon/Minotaur Weapon(40+1)",
        "<:401_422_443_dragon_slayer:781573572421484555><:401_422_443_minotaur_slayer:781573599219286066>",
        "<:401_422_443_dragon_bow:781573565287891024><:401_422_443_minotaur_bow:781573590403121172>",
        "<:401_422_443_dragon_wand:781573581246955601><:401_422_443_minotaur_wand:781573607322681384>",
        40,
        1
    ),  /*24*/
    Weapon(
        "Dragon/Minotaur Weapon(42+2)",
        "<:401_422_443_dragon_slayer:781573572421484555><:401_422_443_minotaur_slayer:781573599219286066>",
        "<:401_422_443_dragon_bow:781573565287891024><:401_422_443_minotaur_bow:781573590403121172>",
        "<:401_422_443_dragon_wand:781573581246955601><:401_422_443_minotaur_wand:781573607322681384>",
        42,
        2
    ),  /*25*/
    Weapon(
        "Dragon/Minotaur Weapon(44+3)",
        "<:401_422_443_dragon_slayer:781573572421484555><:401_422_443_minotaur_slayer:781573599219286066>",
        "<:401_422_443_dragon_bow:781573565287891024><:401_422_443_minotaur_bow:781573590403121172>",
        "<:401_422_443_dragon_wand:781573581246955601><:401_422_443_minotaur_wand:781573607322681384>",
        44,
        3
    ),  /*26*/
    Weapon(
        "Icy Weapon(45+1)",
        "<:451_472_493_icy_broadsword:781573626281328690>",
        "<:451_472_493_icy_bow:781573616629841951>",
        "<:451_472_493_icy_wand:781573633792540692>",
        45,
        1
    ),  /*27*/
    Weapon(
        "Icy Weapon(47+2)",
        "<:451_472_493_icy_broadsword:781573626281328690>",
        "<:451_472_493_icy_bow:781573616629841951>",
        "<:451_472_493_icy_wand:781573633792540692>",
        47,
        2
    ),  /*28*/
    Weapon(
        "Icy Weapon(49+3)",
        "<:451_472_493_icy_broadsword:781573626281328690>",
        "<:451_472_493_icy_bow:781573616629841951>",
        "<:451_472_493_icy_wand:781573633792540692>",
        49,
        3
    ),  /*29*/
    Weapon(
        "Golden Weapon(50+1)",
        "<:501_522_543_golden_broadsword:802412010616520716>",
        "<:501_522_543_golden_bow:802412021806792755>",
        "<:501_522_543_golden_wand:802411996715679794>",
        50,
        1
    ),  /*30*/
    Weapon(
        "Golden Weapon(52+2)",
        "<:501_522_543_golden_broadsword:802412010616520716>",
        "<:501_522_543_golden_bow:802412021806792755>",
        "<:501_522_543_golden_wand:802411996715679794>",
        52,
        2
    ),  /*31*/
    Weapon(
        "Golden Weapon(54+3)",
        "<:501_522_543_golden_broadsword:802412010616520716>",
        "<:501_522_543_golden_bow:802412021806792755>",
        "<:501_522_543_golden_wand:802411996715679794>",
        54,
        3
    ),  /*32*/
    Weapon(
        "Golden Weapon(56+4)",
        "<:501_522_543_golden_broadsword:802412010616520716>",
        "<:501_522_543_golden_bow:802412021806792755>",
        "<:501_522_543_golden_wand:802411996715679794>",
        54,
        3
    ),  /*32*/
    Weapon(
        "Golden Weapon(58+5)",
        "<:501_522_543_golden_broadsword:802412010616520716>",
        "<:501_522_543_golden_bow:802412021806792755>",
        "<:501_522_543_golden_wand:802411996715679794>",
        54,
        3
    )
)

class Mob(val mob_name: String, val emoji_code: String, val mob_defense: Int, val mob_health: Int) {
}

class Weapon(
    val weapon_name: String,
    melee_emoji: String?,
    distance_emoji: String?,
    magic_emoji: String?,
    val weapon_attack: Int,
    val weapon_buffs: Int
) {
    private val emoji_codes: Array<String?>

    init {
        emoji_codes = arrayOfNulls(3)
        emoji_codes[0] = melee_emoji
        emoji_codes[1] = distance_emoji
        emoji_codes[2] = magic_emoji
    }

    fun getEmoji_code(classtype: Int): String? {
        return emoji_codes[classtype]
    }

    val emoji_code: String
        get() = emoji_codes[0] + emoji_codes[1] + emoji_codes[2]

    object Formulas {
        fun auto_min_raw_damage_Calc(stat: Double, weaponatk: Double, base: Double): Double {
            return stat * weaponatk / 20 + base / 4
        }

        fun auto_max_raw_damage_Calc(stat: Double, weaponatk: Double, base: Double): Double {
            return stat * weaponatk / 10 + base / 4
        }

        fun special_meldist_min_raw_damage_Calc(
            stat: Double,
            weaponatk: Double,
            base: Double
        ): Double {
            return 1.5 * (stat * weaponatk / 20 + base / 4)
        }

        fun special_meldist_max_raw_damage_Calc(
            stat: Double,
            weaponatk: Double,
            base: Double
        ): Double {
            return 1.5 * (stat * weaponatk / 10 + base / 4)
        }

        fun special_magic_min_raw_damage_Calc(
            stat: Double,
            weaponatk: Double,
            base: Double
        ): Double {
            return 1.5 * (1.05 * stat * weaponatk / 20 + 9 * base / 32)
        }

        fun special_magic_max_raw_damage_Calc(
            stat: Double,
            weaponatk: Double,
            base: Double
        ): Double {
            return 1.5 * (1.05 * stat * weaponatk / 10 + 9 * base / 32)
        }

        fun min_damage_Calc(min_raw_damage: Double, pos: Int): Double {
            var min_damage: Double = min_raw_damage - mobs.get(pos).mob_defense
            if (min_damage < 0) {
                min_damage = 0.0
            }
            return min_damage
        }

        fun max_damage_Calc(max_raw_damage: Double, pos: Int): Double {
            return max_raw_damage - mobs.get(pos).mob_defense
        }

        fun max_raw_crit_damage_Calc(max_raw_damage: Double): Double {
            return max_raw_damage * 1.05
        }

        fun max_crit_damage_Calc(max_raw_crit_damage: Double, pos: Int): Double {
            return max_raw_crit_damage - mobs.get(pos).mob_defense
        }

        fun normal_accuracy_Calc(max_raw_damage: Double, min_raw_damage: Double, x: Int): Double {
            var normalaccuracy: Double = (max_raw_damage - mobs.get(x)
                .mob_defense) / (max_raw_damage - min_raw_damage)
            if (normalaccuracy > 1.00) {
                normalaccuracy = 1.00
            }
            return normalaccuracy
        }

        fun crit_accuracy_Calc(
            max_raw_crit_damage: Double,
            max_raw_damage: Double,
            x: Int
        ): Double {
            var critaccuracy: Double = (max_raw_crit_damage - mobs.get(x)
                .mob_defense) / (max_raw_crit_damage - max_raw_damage)
            if (critaccuracy > 1.00) {
                critaccuracy = 1.00
            }
            return critaccuracy
        }

        fun accuracy_Calc(
            max_raw_crit_damage: Double,
            max_raw_damage: Double,
            min_raw_damage: Double,
            x: Int
        ): Double {
            return normal_accuracy_Calc(
                max_raw_damage,
                min_raw_damage,
                x
            ) * 0.99 + crit_accuracy_Calc(max_raw_crit_damage, max_raw_damage, x) * 0.01
        }

        fun total_accuracy_Calc(accuracy: Double, tick: Double): Double {
            return 1.0 - Math.pow(Math.pow(1.0 - accuracy, tick), 10.0)
        }

        fun time_to_kill_Calc(avgdmg: Double, pos: Int): Double {
            return mobs.get(pos).mob_health / avgdmg
        }

        fun average_damage_Calc(
            accuracy: Double,
            max_damage: Double,
            min_damage: Double,
            max_crit_damage: Double
        ): Double {
            return accuracy * (.99 * ((max_damage + min_damage) / 2)) + 0.01 * ((max_crit_damage + max_damage) / 2)
        }

        fun tickrate_Calc(accuracy: Double, maxtickrate: Double): Double {
            return maxtickrate * (1.0 - Math.pow(1.0 - accuracy, 10.0))
        }

        fun powertickrate_Calc(totalaccuracy: Double, maxtickrate: Double): Double {
            return maxtickrate * totalaccuracy
        }

        fun max_tickrate_Calc(tick: Double): Double {
            return if (tick <= 5) {
                tick * 3600
            } else {
                18000.toDouble()
            }
        }

        fun exp_Calc(base: Double): Double {
            return Math.pow(base, base / 1000 + 3)
        }

        fun stat0to54_Calc(stat: Double): Double {
            return Math.pow(stat, stat / 1000 + 2.373)
        }

        fun stat55to99_Calc(stat: Double): Double {
            // final double offset = 4692.687;
            // double adjustedStat = stat + 15.68952;
            // return Math.pow(adjustedStat, (adjustedStat/1000) + 2.272) - offset;
            return Math.pow(stat, stat / 1000 + 2.171)
        }

        /*
    public static double stat100to599_Calc(double stat){
        final double offset = 22516.303;
        double adjustedStat = stat + 45.43406;
        // return Math.pow(adjustedStat, (adjustedStat/1000) + 2.171) - offset;
        return Math.pow(stat, (stat/1000) + 2.373);
    }
    public static double stat600plus_Calc(double stat){
        final double offset = 2766484;
        double adjustedStat = stat + 110.34322;
        return Math.pow(adjustedStat, (adjustedStat/1000) + 2.070) - offset;
    }
    */
        fun findStatLevel_Calc(ticks2: Double): Double {
            if (ticks2 <= stat0to54_Calc(54.0)) {
                for (stat in 5..54) {
                    if (ticks2 <= stat0to54_Calc(stat.toDouble())) {
                        val fract =
                            (ticks2 - stat0to54_Calc((stat - 1).toDouble())) / (stat0to54_Calc(stat.toDouble()) - stat0to54_Calc(
                                (stat - 1).toDouble()
                            ))
                        return stat - 1 + fract
                    }
                }
            } else { // if (ticks2 <= stat55to99_Calc(99)){
                for (stat in 55..1000) { // 99; stat++){
                    if (ticks2 <= stat55to99_Calc(stat.toDouble())) {
                        val fract =
                            (ticks2 - stat55to99_Calc((stat - 1).toDouble())) / (stat55to99_Calc(
                                stat.toDouble()
                            ) - stat55to99_Calc((stat - 1).toDouble()))
                        return stat - 1 + fract
                    }
                }
            }
            /*
        else if (ticks2 <= stat100to599_Calc(599)) {
            for (int stat = 100; stat <= 599; stat++){
                if (ticks2 <= stat100to599_Calc(stat)){
                    double fract = (ticks2 - stat100to599_Calc(stat-1))/(stat100to599_Calc(stat) - stat100to599_Calc(stat-1));
                    return (stat-1) + fract;
                }
            }
        } else {
            for (int stat = 600; stat <= 1000; stat++){
                if (ticks2 <= stat600plus_Calc(stat)){
                    double fract = (ticks2 - stat600plus_Calc(stat-1))/(stat600plus_Calc(stat) - stat600plus_Calc(stat-1));
                    return (stat-1) + fract;
                }
            }
        }
        */return (-1).toDouble()
        }

        fun threshold_Calc(tick: Double): Double {
            return 1.0 - Math.pow(.8251, 1.0 / tick)
        }

        fun consistency_Calc(
            max_raw_crit_damage: Double,
            max_raw_damage: Double,
            min_raw_damage: Double,
            mob: Int
        ): Double {
            val health: Int = mobs.get(mob).mob_health
            val defense: Int = mobs.get(mob).mob_defense
            val totaldefense = health + defense
            if (totaldefense - max_raw_crit_damage > 0) {
                return 0.toDouble()
            }
            val range = max_raw_damage - min_raw_damage
            val normaloneshots = max_raw_damage - totaldefense
            return if (normaloneshots > 0) {
                val normalconsistency = normaloneshots / range
                normalconsistency * 0.99 + 0.01
            } else {
                val critrange = max_raw_crit_damage - max_raw_damage
                val criticaloneshots = max_raw_crit_damage - totaldefense
                criticaloneshots / critrange * 0.01
            }
        }
    }
}