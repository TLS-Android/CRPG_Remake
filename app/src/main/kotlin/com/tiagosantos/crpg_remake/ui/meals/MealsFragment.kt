package com.tiagosantos.crpg_remake.ui.meals

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.card.MaterialCardView
import com.tiagosantos.common.ui.base.BaseFragment
import com.tiagosantos.common.ui.base.FragmentSettings
import com.tiagosantos.common.ui.utils.Constants.MODALITY
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.databinding.FragmentMealsBinding

class MealsFragment : BaseFragment<FragmentMealsBinding>(
    layoutId = R.layout.fragment_meals,
    FragmentSettings(
        appBarTitle = R.string.title_dashboard,
        sharedPreferencesBooleanName = R.string.mealsHasRun.toString(),
    )
) {

    private var flagMealChosen = false
    val buttonList = listOf(true, true, true)

    companion object { fun newInstance() = MealsFragment() }

    init {
        val cardCarne: MaterialCardView? = view?.findViewById(R.id.frame_opcao_carne)
        val cardPeixe: MaterialCardView? = view?.findViewById(R.id.frame_opcao_peixe)
        val cardDieta: MaterialCardView? = view?.findViewById(R.id.frame_opcao_dieta)
        val cardVeg: MaterialCardView? = view?.findViewById(R.id.frame_opcao_vegetariano)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        val modalityPreferences = this.requireActivity().getSharedPreferences(
            MODALITY,
            Context.MODE_PRIVATE
        )

        showBackButton()
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle the back button event
                val fragment: Fragment = AgendaFragment()
                val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                fragmentManager.popBackStack()
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
                onPause()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )

        return view
        // return inflater.inflate(R.layout.meals_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mealsViewModel =
            ViewModelProvider(activity as AppCompatActivity).get(MealsViewModel::class.java)


    }

    fun updateFlagMealChosen() {flagMealChosen = !flagMealChosen}

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val mealsViewModel = ViewModelProvider(activity as AppCompatActivity).get(MealsViewModel::class.java)
        ViewModelProvider(activity as AppCompatActivity).get(SharedViewModel::class.java)

        val cardCarne: MaterialCardView? = view?.findViewById(R.id.frame_opcao_carne)
        val cardPeixe: MaterialCardView? = view?.findViewById(R.id.frame_opcao_peixe)
        val cardDieta: MaterialCardView? = view?.findViewById(R.id.frame_opcao_dieta)
        val cardVeg: MaterialCardView? = view?.findViewById(R.id.frame_opcao_vegetariano)

        val isLunch = requireArguments().getBoolean("isLunch")

        cardCarne?.setOnClickListener {
            if (!cardCarne.isChecked) {
                mealsViewModel.selectedOption = 1
            } else {
                mealsViewModel.selectedOption = 0
            }
            cardCarne.isChecked = !cardCarne.isChecked
            cardPeixe?.isChecked = false
            cardDieta?.isChecked = false
            cardVeg?.isChecked = false
        }.also { updateFlagMealChosen() }

        cardPeixe?.setOnClickListener {
            if (!cardPeixe.isChecked) {
                mealsViewModel.selectedOption = 2
            } else {
                mealsViewModel.selectedOption = 0
            }.apply { !cardPeixe.isChecked }
            cardCarne?.isChecked = false
            cardDieta?.isChecked = false
            cardVeg?.isChecked = false
        }.also { updateFlagMealChosen() }

        cardDieta?.setOnClickListener {
            if (!cardDieta.isChecked) {
                mealsViewModel.selectedOption = 3
            } else {
                mealsViewModel.selectedOption = 0
            }
            cardDieta.isChecked = !cardDieta.isChecked
            cardCarne?.isChecked = false
            cardPeixe?.isChecked = false
            cardVeg?.isChecked = false
        }.also { updateFlagMealChosen() }

        cardVeg?.setOnClickListener {

            if (!cardVeg.isChecked) {
                mealsViewModel.selectedOption = 4
            } else {
                mealsViewModel.selectedOption = 0
            }
            cardVeg.isChecked = !cardVeg.isChecked
            cardCarne?.isChecked = false
            cardPeixe?.isChecked = false
            cardDieta?.isChecked = false
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
            flagMealChosen = !flagMealChosen
        }
    }

    override fun performActionWithVoiceCommand(command: String) {
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
}



/*
text_opcao_carne.text = mealsViewModel.retrievedMeal.carne
text_opcao_peixe.text = mealsViewModel.retrievedMeal.peixe
text_opcao_dieta.text = mealsViewModel.retrievedMeal.dieta
text_opcao_vegetariano.text = mealsViewModel.retrievedMeal.vegetariano
*/
