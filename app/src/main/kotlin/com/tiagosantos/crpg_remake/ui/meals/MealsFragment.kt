package com.tiagosantos.crpg_remake.ui.meals

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.material.card.MaterialCardView
import com.tiagosantos.common.ui.base.BaseFragment
import com.tiagosantos.common.ui.base.FragmentSettings
import com.tiagosantos.common.ui.utils.VoiceCommandsProcessingHelper
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.databinding.MealsFragmentBinding

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

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val isLunch = requireArguments().getBoolean("isLunch")
        val mealsVM: MealsViewModel by viewModels()

        val cardList = listOf(
            view.frameOpcaoCarne, view.frameOpcaoPeixe, view.frameOpcaoDieta,
                view.frameOpcaoVegetariano
        )

        fun setChecks(cardList: List<MaterialCardView?>, card: MaterialCardView) {
            if (!card.isChecked) mealsVM.updateSelectedOption(1)
                 else mealsVM.updateSelectedOption(0)

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

        view.buttonConfirmMeal.setOnClickListener {
            if (mealsVM.selectedOption.value != 0) {
                view.success.mealChoiceSuccess.visibility =
                    View.VISIBLE.apply { it.bringToFront() }
                view.avisoNenhumaRefeicaoChecked.visibility = View.GONE
                mealsVM.updateMealChoiceOnLocalStorage(
                    mealsVM.selectedOption,
                    isLunch
                )
                view.success.buttonOk.setOnClickListener {
                    view.success.mealChoiceSuccess.visibility = View.GONE
                }
            } else {
                view.success.mealChoiceSuccess.visibility = View.VISIBLE
            }
        }
    }

    private fun performActionWithVoiceCommand(command: String){
        when {
            VoiceCommandsProcessingHelper.mealPickHelper(command,"Carne") -> view.frameOpcaoCarne.performClick()
            VoiceCommandsProcessingHelper.mealPickHelper(command,"Peixe")  -> view.frameOpcaoPeixe.performClick()
            VoiceCommandsProcessingHelper.mealPickHelper(command,"Dieta") -> view.frameOpcaoDieta .performClick()
            VoiceCommandsProcessingHelper.mealPickHelper(command,"Vegetariano")  -> view.frameOpcaoVegetariano.performClick()
            VoiceCommandsProcessingHelper.mealPickHelper(command,"Guardar")  -> view.buttonConfirmMeal.performClick()
        }
    }

    override fun onInitDataBinding() {
        TODO("Not yet implemented")
    }

    override fun observeLifecycleEvents() {
        TODO("Not yet implemented")
    }

    private fun updateFlagMealChosen() { flagMealChosen = !flagMealChosen }
}

