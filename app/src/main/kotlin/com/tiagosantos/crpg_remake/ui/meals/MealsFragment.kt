package com.tiagosantos.crpg_remake.ui.meals

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.material.card.MaterialCardView
import com.tiagosantos.access.modal.BaseModalFragment
import com.tiagosantos.access.modal.settings.TTSFragmentSettings
import com.tiagosantos.common.ui.base.FragmentSettings
import com.tiagosantos.common.ui.utils.VoiceCommandsProcessingHelper
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.databinding.MealsFragmentBinding

//The onCreate() is called first, for doing any non-graphical initialisations.
//// Next, you can assign and declare any View variables you want to use in onCreateView().
// Afterwards, use onActivityCreated() to do any final initialisations you want to do once
// everything has completed.

class MealsFragment(ttsSettings: TTSFragmentSettings) : BaseModalFragment<MealsFragmentBinding>(
    layoutId = R.layout.meals_fragment,
    FragmentSettings(
        appBarTitle = R.string.meal_action_bar_title,
        sharedPreferencesBooleanName = R.string.mealsHasRun.toString(),
    ),
    TTSFragmentSettings(R.string.indique_refeicao.toString())
) {
    private lateinit var view: MealsFragmentBinding
    private var flagMealChosen = false

    private val actionMap: Map<String, Any> by lazy {
        mapOf("Carne" to view.frameOpcaoCarne.performClick(),
            "Peixe" to view.frameOpcaoPeixe.performClick(),
            "Dieta" to  view.frameOpcaoDieta.performClick(),
            "Vegetariano" to view.frameOpcaoVegetariano.performClick(),
            "Guardar" to view.buttonConfirmMeal.performClick())
    }
    private var isLunch = false
    private val mealsVM: MealsViewModel by viewModels()
    private lateinit var cardList: List<MaterialCardView>

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isLunch = requireArguments().getBoolean("isLunch")
        cardList = listOf(
            view.frameOpcaoCarne, view.frameOpcaoPeixe, view.frameOpcaoDieta,
            view.frameOpcaoVegetariano
        )
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val command = "ispis lorem"
        performActionWithVoiceCommand(command,actionMap)

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

    override fun onInitDataBinding() {
        TODO("Not yet implemented")
    }

    override fun observeLifecycleEvents() {
        TODO("Not yet implemented")
    }

    private fun updateFlagMealChosen() { flagMealChosen = !flagMealChosen }
}
