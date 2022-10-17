package com.tiagosantos.common.ui.playground

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.tiagosantos.common.ui.base.FragmentSettings
import com.tiagosantos.crpg_remake.databinding.FragmentMeditationBinding
import java.util.*

data class Person(var name: String, var age: Int, var city: String) {
    fun moveTo(newCity: String) { city = newCity }
    fun incrementAge() { age++ }
}

class PlaygroundFragment : BaseModalFragment<FragmentMeditationBinding>(
    layoutId = null,
    FragmentSettings(
        appBarTitle = "hey",
        sharedPreferencesBooleanName = "john doe",
    )
) {
    private var onResumeFlag = false

    companion object {
        fun newInstance() = PlaygroundFragment()
    }


    fun exp() {
        val l = listOf(1, 2, 3)
        l.forEachIndexed { index, i -> println("Para o $index o valor e $i")  }

        Person("Alice", 20, "Amsterdam").let {
            println(it)
            it.moveTo("London")
            it.incrementAge()
            println(it)
        }

        val p = Pair(1, 2)
        val (first, _) = p
        println(p)
    }

}

fun main() {

    Person("Alice", 20, "Amsterdam").let {
        println(it)
        it.moveTo("London")
        it.incrementAge()
        println(it)
    }

}