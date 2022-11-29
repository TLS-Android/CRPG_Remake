package com.tiagosantos.crpg_remake.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tiagosantos.crpg_remake.base.BaseFragment
import com.tiagosantos.crpg_remake.base.FragmentSettings
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.databinding.FragmentHomeBinding
import org.koin.android.scope.getOrCreateScope
import org.koin.core.scope.Scope

class HomeFragment : BaseFragment<FragmentHomeBinding>(
    layoutId = R.layout.fragment_home,
    FragmentSettings(
        appBarTitle = R.string.home_fragment_appbar_title,
    )
) {

    override val scope: Scope by getOrCreateScope()

    private var _binding: FragmentHomeBinding? = null
    private val viewModel: HomeViewModel
            by lazy(LazyThreadSafetyMode.NONE) {  }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        viewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textHome

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onInitDataBinding() {
        TODO("Not yet implemented")
    }

    override fun observeLifecycleEvents() {
        TODO("Not yet implemented")
    }

}

