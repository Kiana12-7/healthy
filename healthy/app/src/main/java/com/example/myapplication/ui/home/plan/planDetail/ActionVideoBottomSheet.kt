package com.example.myapplication.ui.home.plan.planDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.myapplication.databinding.DialogActionVideoBinding

class ActionVideoBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: DialogActionVideoBinding
    private lateinit var actionItem: TrainActionItem

    companion object {
        fun newInstance(actionItem: TrainActionItem): ActionVideoBottomSheet {
            val fragment = ActionVideoBottomSheet()
            val args = Bundle()
            args.putSerializable("ACTION_ITEM", actionItem)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            @Suppress("DEPRECATION")
            actionItem = it.getSerializable("ACTION_ITEM") as TrainActionItem
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogActionVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvActionTitle.text = actionItem.actionName
        binding.tvActionDesc.text = actionItem.actionDesc
        binding.btnClose.setOnClickListener { dismiss() }
    }
}