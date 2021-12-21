package com.nakib.iamhere.ui.privacyPolicy

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.nakib.iamhere.R
import com.nakib.iamhere.databinding.FragmentPrivacyPolicyBinding

class PrivacyPolicyFragment : Fragment() {

    lateinit var binding : FragmentPrivacyPolicyBinding
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPrivacyPolicyBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun handleView() {
        sharedPreferences = requireActivity().getSharedPreferences("iamhere", Context.MODE_PRIVATE)

        binding.webViewId.settings.javaScriptEnabled = true
        binding.webViewId.loadUrl("file:///android_asset/privacy_policy.html")
        binding.acceptBtnId.setOnClickListener {
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("IsFirstTime", "true")
            editor.apply()
            Navigation.findNavController(it).navigate(R.id.action_privacyPolicyFragment_to_loginFragment)
        }
    }


}