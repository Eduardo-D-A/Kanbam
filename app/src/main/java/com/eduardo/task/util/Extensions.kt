package com.eduardo.task.util

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.eduardo.task.R
import com.eduardo.task.databinding.BottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

//pag 65 a 67
fun Fragment.initToolbar(toolbar: Toolbar) {
    (activity as AppCompatActivity).setSupportActionBar(toolbar)
    (activity as AppCompatActivity).title=""
    (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    toolbar.setNavigationOnClickListener {
        activity?.onBackPressedDispatcher?.onBackPressed()
    }
}

//pag 75 a 77
fun Fragment.showBottomSheet(
    titleDialog: Int? = null,
    titleButtom: Int? = null,
    message: String,
    onClick: () -> Unit ={}
) {
    val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
    val binding: BottomSheetBinding =
        BottomSheetBinding.inflate(layoutInflater, null, false)

    binding.textViewTitle.text = getText(titleDialog ?: R.string.text_title_warning)
    binding.texViewMessage.text = message
    binding.buttomOk.text = getText(titleButtom ?: R.string.text_button_warning)
    binding.buttomOk.setOnClickListener {
        onClick()
        bottomSheetDialog.dismiss()
    }

    //pag82
    bottomSheetDialog.setContentView(binding.root)
    bottomSheetDialog.show()
}