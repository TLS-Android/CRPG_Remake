package com.tiagosantos.crpg_remake.ui.meals

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
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
            if (!card.isChecked) {
                mealsVM.selectedOption = 1
            } else mealsVM.selectedOption = 0

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
            if (mealsVM.selectedOption != 0) {
                mealSuccessView?.visibility = View.VISIBLE
                mealSuccessView?.bringToFront()
                nothingCheckedWarning?.visibility = View.GONE
                mealsVM.updateMealChoiceOnLocalStorage(
                    mealsVM.selectedOption,
                    isLunch
                )
                view.button_ok?.setOnClickListener {
                    mealSuccessView?.visibility = View.GONE
                     }
            } else {
                view.root.
                    .visibility = View.VISIBLE
            }
        }
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


