package com.tiagosantos.crpg_remake.ui.meals

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.google.android.material.card.MaterialCardView
import com.tiagosantos.common.ui.base.BaseFragment
import com.tiagosantos.common.ui.base.FragmentSettings
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.databinding.MealsFragmentBinding
import java.util.*

class MealsFragment : BaseFragment<MealsFragmentBinding>(
    layoutId = R.layout.meals_fragment,
    FragmentSettings(
        appBarTitle = R.string.title_dashboard,
        sharedPreferencesBooleanName = R.string.mealsHasRun.toString(),
    )
) {

    private lateinit var view: MealsFragmentBinding
    private var flagMealChosen = false
    val buttonList = listOf(
        true, true, true
    )

    val button_confirm = view?.findViewById<View>(R.id.button_confirm_meal)
    val button_ok = view?.findViewById<View>(R.id.button_ok)
    val mealSuccessView = view?.findViewById<View>(R.id.meal_choice_success)
    val nothingCheckedWarning = view?.findViewById<View>(R.id.aviso_nenhuma_refeicao_checked)

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
            view.frameOpcaoCarne, view.frameOpcaoPeixe, view.frameOpcaoDieta,
                view.frameOpcaoVegetariano
        )

        fun setChecks(cardList: List<MaterialCardView?>, card: MaterialCardView) {
            if (!card.isChecked) {
                mealsViewModel.selectedOption = 1
            } else mealsViewModel.selectedOption = 0

            for (s in cardList) {
                if (s != card) s?.isChecked = false
            }
        }

        view.frameOpcaoCarne.setOnClickListener {
            setChecks(cardList,  view.frameOpcaoCarne)
        }.also { updateFlagMealChosen() }

        view.frameOpcaoPeixe.setOnClickListener {
            setChecks(cardList,  view.frameOpcaoPeixe)
        }.also { updateFlagMealChosen() }

        view.frameOpcaoDieta.setOnClickListener {
            setChecks(cardList,  view.frameOpcaoPeixe)
        }.also { updateFlagMealChosen() }

        view.frameOpcaoVegetariano.setOnClickListener {
            setChecks(cardList, view.frameOpcaoVegetariano )
        }.also { updateFlagMealChosen() }

        button_confirm?.setOnClickListener {
            if (mealsViewModel.selectedOption != 0) {
                mealSuccessView?.visibility = View.VISIBLE
                mealSuccessView?.bringToFront()
                nothingCheckedWarning?.visibility = View.GONE
                mealsViewModel.updateMealChoiceOnLocalStorage(
                    mealsViewModel.selectedOption,
                    isLunch
                )
                button_ok?.setOnClickListener {
                    mealSuccessView?.visibility = View.GONE
                     }
            } else {
                nothingCheckedWarning?.visibility = View.VISIBLE
            }
        }
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

//handler.removeCallbacksAndMessages(null)
//                    if (handler.hasMessages(0)) {
//                        handler.removeCallbacks(runnable)
//                    }


