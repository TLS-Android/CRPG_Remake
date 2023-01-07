package com.tiagosantos.crpg_remake.ui.meals

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.card.MaterialCardView
import com.tiagosantos.access.modal.settings.SRSettings
import com.tiagosantos.access.modal.settings.TTSSettings
import com.tiagosantos.common.ui.extension.*
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.base.BaseModalFragment
import com.tiagosantos.crpg_remake.base.FragmentSettings
import com.tiagosantos.crpg_remake.databinding.MealsFragmentBinding

/**
 * The onCreate() is called first, for doing any non-graphical initialisations.
 * Next, you can assign and declare any View variables you want to use in onCreateView().
 * Afterwards, use onActivityCreated() to do any final initialisations you want to do once
 * everything has completed.
 */

class MealsFragment : BaseModalFragment<MealsFragmentBinding>(
        layoutId = R.layout.meals_fragment,
        FragmentSettings(
            appBarTitle = R.string.meal_action_bar_title,
            sharedPreferencesBooleanName = R.string.mealsHasRun.toString(),
        ),
        TTSSettings(
            R.string.indique_refeicao.toString(),
            isSpeaking = false
        ),
        srSettings = SRSettings(
            commandList = listOf("Carne", "Peixe", "Dieta", "Vegetariano", "Guardar"),
            isListening = false,
        ),
) {

    private val mealsVM: MealsViewModel by viewModels()

    private var cardList = with(viewB) {
        listOf(frameOpcaoCarne, frameOpcaoPeixe, frameOpcaoDieta, frameOpcaoVegetariano)
    }

    private var flagMealChosen = false
    private var isLunch = false

    override fun onCreate(savedInstanceState: Bundle?) {
        isLunch = requireArguments().getBoolean("isLunch")
        viewB.apply {
            actionList = mutableListOf(frameOpcaoCarne, frameOpcaoPeixe,
                frameOpcaoDieta, frameOpcaoVegetariano, buttonConfirmMeal)
        }
        super.onCreate(savedInstanceState)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fun setChecks(cardList: List<MaterialCardView?>, card: MaterialCardView) {
            if (!card.isChecked) mealsVM.updateSelectedOption(1)
                 else mealsVM.updateSelectedOption(0)
            for (s in cardList) { if (s != card) s?.isChecked = false }
        }

        fun MaterialCardView.setFrameOnClick() {
            this.setOnClickListener {
                setChecks(cardList,  this)
            }.also { updateFlagMealChosen() }
        }

        fun ViewGroup.eachChild(func: (view: View) -> Unit) {
            for (i in 0 until childCount) {
                func(getChildAt(i))
            }
        }

        with(viewB){
            frameMeals.eachChild { (it as MaterialCardView).setFrameOnClick() }

            with(success){
                buttonConfirmMeal.setOnClickListener {
                    if (mealsVM.selectedOption.value != 0) {
                        mealChoiceSuccess.showAndBringToFront()
                        avisoNenhumaRefeicaoChecked.hide()
                        mealsVM.updateMealChoiceOnLocalStorage(mealsVM.selectedOption, isLunch)
                        buttonOk.setOnClickListener { mealChoiceSuccess.hide() }
                    } else { mealChoiceSuccess.show() }
                }
            }
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
