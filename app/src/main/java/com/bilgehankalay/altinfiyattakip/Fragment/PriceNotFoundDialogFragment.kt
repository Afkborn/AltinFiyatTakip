package com.bilgehankalay.altinfiyattakip.Fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.bilgehankalay.altinfiyattakip.R

class PriceNotFoundDialogFragment() : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = AlertDialog.Builder(requireContext())
        .setMessage(getString(R.string.price_not_found_price_not_found))
        .setPositiveButton(R.string.price_not_found_possitive_button){ _, _ ->

        }
        .create()

    companion object{
        const val TAG = "ManuallyEnterPriceDailog"
    }
}