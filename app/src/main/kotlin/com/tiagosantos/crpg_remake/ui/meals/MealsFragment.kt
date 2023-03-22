package com.tiagosantos.crpg_remake.ui.meals

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.card.MaterialCardView
import com.hannesdorfmann.fragmentargs.annotation.Arg
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs
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
 *
 * You must not initialize a Fragment's view properties at the declaration site,
 * because a Fragment is initialized before it is has any view or is attached to any Activity.
 * Also, the same Fragment instance might be reused but have a new view and Activity instance,
 * so you should make sure you are re-assigning these properties every time there is a new view.
 */
@FragmentWithArgs
class MealsFragment : BaseModalFragment<MealsFragmentBinding>() {

    @Arg override var layoutId = R.layout.meals_fragment

    @delegate:Arg
    override val settings by lazy {
        FragmentSettings(
            appBarTitle = R.string.meal_action_bar_title,
            sharedPreferencesBooleanName = R.string.datePickerHasRun.toString(),
        )
    }

    @delegate:Arg
    override val ttsSettings by lazy {
        TTSSettings(
            contextualHelp = R.string.indique_refeicao,
            isSpeaking = false
        )
    }

    @delegate:Arg
    override val srSettings by lazy {
        SRSettings(
            commandList = listOf("Carne", "Peixe", "Dieta", "Vegetariano", "Guardar"),
            isListening = false,
        )
    }

    private val viewModel: MealsViewModel by viewModels()

    private lateinit var cardList : List<MaterialCardView>

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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //isLunch = requireArguments().getBoolean("isLunch")
        isLunch = true
        viewB.apply {
            actionList = mutableListOf(frameOpcaoCarne, frameOpcaoPeixe,
                frameOpcaoDieta, frameOpcaoVegetariano, buttonConfirmMeal)
            cardList = listOf(frameOpcaoCarne, frameOpcaoPeixe,
                frameOpcaoDieta, frameOpcaoVegetariano)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun setupUI() {
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

    private fun setup() {

    }

    private fun updateFlagMealChosen() { flagMealChosen = !flagMealChosen }
}
