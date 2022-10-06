package com.tiagosantos.crpg_remake.ui.meals

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.card.MaterialCardView
import com.tiagosantos.common.ui.base.BaseFragment
import com.tiagosantos.common.ui.base.FragmentSettings
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.databinding.FragmentMealsBinding
import java.util.*

class MealsFragment : BaseFragment<FragmentMealsBinding>(
    layoutId = R.layout.fragment_meals,
    FragmentSettings(
        appBarTitle = R.string.title_dashboard,
        sharedPreferencesBooleanName = R.string.mealsHasRun.toString(),
    )
) {

    private var flagMealChosen = false
    val buttonList = listOf(
        true, true, true
    )

    private val cardCarne: MaterialCardView? = view?.findViewById(R.id.frame_opcao_carne)
    private val cardPeixe: MaterialCardView? = view?.findViewById(R.id.frame_opcao_peixe)
    private val cardDieta: MaterialCardView? = view?.findViewById(R.id.frame_opcao_dieta)
    private val cardVeg: MaterialCardView? = view?.findViewById(R.id.frame_opcao_vegetariano)


    companion object { fun newInstance() = MealsFragment() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mealsViewModel =
            ViewModelProvider(activity as AppCompatActivity)[MealsViewModel::class.java]
    }


    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val mealsViewModel = ViewModelProvider(activity as AppCompatActivity).get(MealsViewModel::class.java)
        ViewModelProvider(activity as AppCompatActivity).get(SharedViewModel::class.java)
        val isLunch = requireArguments().getBoolean("isLunch")

        val cardList = listOf(
            cardCarne, cardPeixe, cardDieta, cardVeg
        )

        fun setChecks(cardList: List<MaterialCardView?>, card: MaterialCardView) {
            if (!card.isChecked) {
                mealsViewModel.selectedOption = 1
            } else mealsViewModel.selectedOption = 0

            for (s in cardList) {
                if (s != card) s?.isChecked = false
            }
        }

        cardCarne?.setOnClickListener {
            setChecks(cardList, cardCarne)
        }.also { updateFlagMealChosen() }

        cardPeixe?.setOnClickListener {
            setChecks(cardList, cardPeixe)
        }.also { updateFlagMealChosen() }

        cardDieta?.setOnClickListener {
            setChecks(cardList, cardDieta)
        }.also { updateFlagMealChosen() }

        cardVeg?.setOnClickListener {
            setChecks(cardList, cardVeg)
        }.also { updateFlagMealChosen() }

        val mealSuccessView = view?.findViewById<View>(R.id.meal_choice_success)
        val nothingCheckedWarning = view?.findViewById<View>(R.id.aviso_nenhuma_refeicao_checked)

        /*
        button_confirm_meal.setOnClickListener {
            if (mealsViewModel.selectedOption != 0) {
                mealSuccessView?.visibility = View.VISIBLE
                mealSuccessView?.bringToFront()
                nothingCheckedWarning?.visibility = View.GONE
                mealsViewModel.updateMealChoiceOnLocalStorage(
                    mealsViewModel.selectedOption,
                    isLunch
                )
                button_ok.setOnClickListener {
                    mealSuccessView?.visibility = View.GONE
                    handler.removeCallbacksAndMessages(null)
                    if (handler.hasMessages(0)) {
                        handler.removeCallbacks(runnable)
                    }
                }
            } else {
                nothingCheckedWarning?.visibility = View.VISIBLE
            }
        }*/
    }

     fun performActionWithVoiceCommand(command: String) {
        val commandToActionMap = mapOf(
            "Carne" to cardCarne?.performClick(),
            "Peixe" to cardPeixe?.performClick(),
            "Dieta" to cardDieta?.performClick(),
            "Vegetariano" to cardVeg?.performClick(),
        )

        val a = commandToActionMap.keys.find {
            command.contains(it) ||
                (command.contains(it) && command.contains("Prato"))
        }

        commandToActionMap[a]
    }

    override fun onInitDataBinding() {
        TODO("Not yet implemented")
    }

    override fun observeLifecycleEvents() {
        TODO("Not yet implemented")
    }

    private fun updateFlagMealChosen() {flagMealChosen = !flagMealChosen}

}


