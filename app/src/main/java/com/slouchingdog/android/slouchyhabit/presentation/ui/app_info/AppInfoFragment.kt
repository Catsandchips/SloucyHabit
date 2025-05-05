package com.slouchingdog.android.slouchyhabit.presentation.ui.app_info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.slouchingdog.android.slouchyhabit.databinding.FragmentAppInfoBinding

class AppInfoFragment : Fragment() {
    lateinit var binding: FragmentAppInfoBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAppInfoBinding.inflate(inflater)
        return binding.root
    }
}
