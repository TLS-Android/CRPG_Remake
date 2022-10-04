package com.tiagosantos.crpg_remake.ui.meals

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
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

    companion object { fun newInstance() = MealsFragment() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        showBackButton()
        return view
        // return inflater.inflate(R.layout.meals_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mealsViewModel =
            ViewModelProvider(activity as AppCompatActivity)[MealsViewModel::class.java]
    }

    private fun updateFlagMealChosen() {flagMealChosen = !flagMealChosen}

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val mealsViewModel = ViewModelProvider(activity as AppCompatActivity).get(MealsViewModel::class.java)
        ViewModelProvider(activity as AppCompatActivity).get(SharedViewModel::class.java)
        val isLunch = requireArguments().getBoolean("isLunch")

        val cardCarne: MaterialCardView? = view?.findViewById(R.id.frame_opcao_carne)
        val cardPeixe: MaterialCardView? = view?.findViewById(R.id.frame_opcao_peixe)
        val cardDieta: MaterialCardView? = view?.findViewById(R.id.frame_opcao_dieta)
        val cardVeg: MaterialCardView? = view?.findViewById(R.id.frame_opcao_vegetariano)

        val cardList = listOf(
            cardCarne, cardPeixe, cardDieta, cardVeg
        )

        fun setChecks(cardList: List<MaterialCardView?>, card: MaterialCardView) {
            if (!card.isChecked) {
                mealsViewModel.selectedOption = 1
            } else mealsViewModel.selectedOption = 0

            for (s in cardList) {
                if (s != card) s.isChecked = false
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
        }
    }

     fun performActionWithVoiceCommand(command: String) {
        val commandToActionMap = mapOf(
            "Carne" to frame_opcao_carne.performClick(),
            "Peixe" to frame_opcao_peixe.performClick(),
            "Dieta" to frame_opcao_dieta.performClick(),
            "Vegetariano" to frame_opcao_vegetariano.performClick(),
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

    fun setChecks(selOption: Int, booleanList: List<Boolean>) {
        if (cardCarne?.isChecked == true) {
            mealsViewModel.selectedOption = selOption
        } else {
            mealsViewModel.selectedOption = 0
        }
        cardCarne?.isChecked = cardCarne?.isChecked == booleanList[0]
        cardPeixe?.isChecked = booleanList[1]
        cardDieta?.isChecked = booleanList[2]
        cardVeg?.isChecked = booleanList[3]
        updateFlagMealChosen()
    }
}


