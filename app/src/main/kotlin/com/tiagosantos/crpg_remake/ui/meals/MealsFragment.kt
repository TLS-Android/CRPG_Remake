package com.tiagosantos.crpg_remake.ui.meals

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.card.MaterialCardView
import com.hannesdorfmann.fragmentargs.annotation.Arg
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
) {

    @Arg
    override var ttsSettings = TTSSettings(
        contextualHelp = R.string.indique_refeicao,
        isSpeaking = false
    )

    @Arg
    override var srSettings = SRSettings(
        commandList = listOf("Carne", "Peixe", "Dieta", "Vegetariano", "Guardar"),
        isListening = false,
    )

    private val viewModel: MealsViewModel by viewModels()

    private var cardList = with(viewB) {
        listOf(frameOpcaoCarne, frameOpcaoPeixe, frameOpcaoDieta, frameOpcaoVegetariano)
    }

    private var flagMealChosen = false
    private var isLunch = false

    /**
     * The onActivityCreated() method is now deprecated.
     * Code touching the fragment's view should be done in onViewCreated()
     * (which is called immediately before onActivityCreated()) and other initialization code should be in onCreate().
     * To receive a callback specifically when the activity's onCreate() is complete,
     * a LifeCycleObserver should be registered on the activity's Lifecycle in onAttach(),
     * and removed once the onCreate() callback is received.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        isLunch = requireArguments().getBoolean("isLunch")
        viewB.apply {
            actionList = mutableListOf(frameOpcaoCarne, frameOpcaoPeixe,
                frameOpcaoDieta, frameOpcaoVegetariano, buttonConfirmMeal)
        }
        super.onCreate(savedInstanceState)
        setup()
    }

    override fun setupUI() {
        TODO("Not yet implemented")
    }

    private fun setup() {
        activity?.lifecycleScope?.launchWhenCreated {
            fun setChecks(cardList: List<MaterialCardView?>, card: MaterialCardView) {
                if (!card.isChecked) viewModel.updateSelectedOption(1)
                else viewModel.updateSelectedOption(0)
                for (s in cardList) {
                    if (s != card) s?.isChecked = false
                }
            }

            with(viewB) {
                frameMeals.eachChild {
                    (it as MaterialCardView).setFrameOnClick(cardList, ::setChecks)
                        .also { updateFlagMealChosen() }
                }

                with(success) {
                    buttonConfirmMeal.setOnClickListener {
                        if (viewModel.selectedOption.value != 0) {
                            mealChoiceSuccess.showAndBringToFront()
                            avisoNenhumaRefeicaoChecked.hide()
                            viewModel.updateMealChoiceOnLocalStorage(
                                viewModel.selectedOption,
                                isLunch
                            )
                            buttonOk.setOnClickListener { mealChoiceSuccess.hide() }
                        } else {
                            mealChoiceSuccess.show()
                        }
                    }
                }
            }
        }
    }

    private fun updateFlagMealChosen() { flagMealChosen = !flagMealChosen }
}
