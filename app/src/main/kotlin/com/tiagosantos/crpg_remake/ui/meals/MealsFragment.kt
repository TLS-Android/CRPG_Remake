package com.tiagosantos.crpg_remake.ui.meals

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.ViewTarget
import com.google.android.material.card.MaterialCardView
import com.hannesdorfmann.fragmentargs.annotation.Arg
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs
import com.tiagosantos.access.modal.settings.TTSSettings
import com.tiagosantos.common.ui.extension.eachChild
import com.tiagosantos.common.ui.extension.hide
import com.tiagosantos.common.ui.extension.setFrameOnClick
import com.tiagosantos.common.ui.extension.show
import com.tiagosantos.common.ui.extension.showAndBringToFront
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.base.BaseModalFragment
import com.tiagosantos.crpg_remake.base.FragmentSettings
import com.tiagosantos.crpg_remake.databinding.MealsFragmentBinding
import kotlinx.coroutines.launch

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
            appBarTitle = getString(R.string.title_refeicao),
            sharedPreferencesBooleanName = getString(R.string.datePickerHasRun),
        )
    }

    @delegate:Arg
    override val ttsSettings by lazy {
        TTSSettings(
            contextualHelp = R.string.indique_refeicao,
            isSpeaking = false
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
        isLunch = true
        println("ActionMap: " + srSettings.commandList)

        viewB.lyFrameMeals.apply {
            backgroundImageCarne.load(R.drawable.background_carne)
            backgroundImagePeixe.load(R.drawable.background_peixe)
            backgroundImageDieta.load(R.drawable.background_dieta)
            backgroundImageVegetariano.load(R.drawable.background_veg)

            actionList = mutableListOf(frameOpcaoCarne, frameOpcaoPeixe,
                frameOpcaoDieta, frameOpcaoVegetariano, viewB.buttonConfirmMeal)
            cardList = listOf(frameOpcaoCarne, frameOpcaoPeixe,
                frameOpcaoDieta, frameOpcaoVegetariano)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun setupUI() {
        lifecycleScope.launch {
            fun setChecks(cardList: List<MaterialCardView?>, card: MaterialCardView) {
                if (!card.isChecked) viewModel.updateSelectedOption(1)
                else viewModel.updateSelectedOption(0)
                for (s in cardList) {
                    if (s != card) s?.isChecked = false
                }
            }

            with(viewB) {
                lyFrameMeals.lyMealsOptions.eachChild {
                    (it as MaterialCardView).setFrameOnClick(cardList, ::setChecks)
                        .also { updateFlagMealChosen() }
                }

                buttonConfirmMeal.setOnClickListener {
                    if (viewModel.selectedOption.value != 0) {
                        success.mealChoiceSuccess.showAndBringToFront()
                        avisoNenhumaRefeicaoChecked.hide()
                        viewModel.updateMealChoiceOnLocalStorage(viewModel.selectedOption, isLunch)
                        success.buttonOk.setOnClickListener { success.mealChoiceSuccess.hide() }
                    } else {
                        success.mealChoiceSuccess.show()
                    }
                }
            }

        }
    }

    private fun updateFlagMealChosen() { flagMealChosen = !flagMealChosen }
}

/**
 *     A construct emitted by a Java compiler must be marked as synthetic if it does not correspond
 *     to a construct declared explicitly or implicitly in source code, unless the emitted construct
 *     is a class initialization method (JVMS §2.9).
 *     The @JvmSynthetic annotation does exactly that: prevent access from source code.
 *     The method will still appear in reflection and is then marked as synthetic.
 **/
@JvmSynthetic
fun ImageView.load(
    data: Any?,
): ViewTarget<ImageView, Drawable> {
    return Glide.with(this)
        .load(data)
        .into(this)
}
