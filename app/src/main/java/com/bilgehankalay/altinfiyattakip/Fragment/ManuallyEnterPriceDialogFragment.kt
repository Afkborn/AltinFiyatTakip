package com.bilgehankalay.altinfiyattakip.Fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.bilgehankalay.altinfiyattakip.R

class ManuallyEnterPriceDialogFragment() : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = AlertDialog.Builder(requireContext())
        .setMessage(getString(R.string.manually_enter_price_message))
        .setPositiveButton(R.string.manually_enter_price_possitive_button){ _, _ ->

        }
        .create()

    companion object{
        const val TAG = "ManuallyEnterPriceDailog"
    }
}