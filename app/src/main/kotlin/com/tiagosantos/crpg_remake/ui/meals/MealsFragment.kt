package com.tiagosantos.crpg_remake.ui.meals

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.material.card.MaterialCardView
import com.tiagosantos.access.modal.settings.SRSettings
import com.tiagosantos.access.modal.settings.TTSSettings
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
            isListening = false,
            actionMap = null
        )
) {

    private val mealsVM: MealsViewModel by viewModels()

    private val actionMap: Map<String, Any> by lazy {
        mapOf(
            "Carne" to viewB.frameOpcaoCarne.performClick(),
            "Peixe" to viewB.frameOpcaoPeixe.performClick(),
            "Dieta" to  viewB.frameOpcaoDieta.performClick(),
            "Vegetariano" to viewB.frameOpcaoVegetariano.performClick(),
            "Guardar" to viewB.buttonConfirmMeal.performClick()
        )
    }

    private var cardList = listOf(
        viewB.frameOpcaoCarne, viewB.frameOpcaoPeixe,
        viewB.frameOpcaoDieta, viewB.frameOpcaoVegetariano
    )

    private var flagMealChosen = false
    private var isLunch = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isLunch = requireArguments().getBoolean("isLunch")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        gossip.contextualHelp

        val command = "ispis lorem"
        performActionWithVoiceCommand(command,actionMap)

        fun setChecks(cardList: List<MaterialCardView?>, card: MaterialCardView) {
            if (!card.isChecked) mealsVM.updateSelectedOption(1)
                 else mealsVM.updateSelectedOption(0)

            for (s in cardList) { if (s != card) s?.isChecked = false }
        }

        with(viewB){
            frameOpcaoCarne.setOnClickListener {
                setChecks(cardList,  frameOpcaoCarne)
            }.also { updateFlagMealChosen() }

            frameOpcaoPeixe.setOnClickListener {
                setChecks(cardList,  frameOpcaoPeixe)
            }.also { updateFlagMealChosen() }

            frameOpcaoDieta.setOnClickListener {
                setChecks(cardList,  frameOpcaoPeixe)
            }.also { updateFlagMealChosen() }

            frameOpcaoVegetariano.setOnClickListener {
                setChecks(cardList, frameOpcaoVegetariano )
            }.also { updateFlagMealChosen() }

            buttonConfirmMeal.setOnClickListener {
                if (mealsVM.selectedOption.value != 0) {
                    success.mealChoiceSuccess.visibility =
                        View.VISIBLE.apply { it.bringToFront() }
                    avisoNenhumaRefeicaoChecked.visibility = View.GONE
                    mealsVM.updateMealChoiceOnLocalStorage(
                        mealsVM.selectedOption,
                        isLunch
                    )
                    success.buttonOk.setOnClickListener {
                        success.mealChoiceSuccess.visibility = View.GONE
                    }
                } else {
                    success.mealChoiceSuccess.visibility = View.VISIBLE
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
