package com.nawrot.mateusz.compass.home.destination

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import com.nawrot.mateusz.compass.R
import com.nawrot.mateusz.compass.base.toOptionalDouble
import kotlinx.android.synthetic.main.fragment_destination_dialog.*


class DestinationDialog : DialogFragment() {

    private var activityInterface: DestinationDialogActivityInterface? = null

    companion object {
        fun getInstance(): DestinationDialog {
            return DestinationDialog()
        }

        val TAG: String = DestinationDialog::class.java.simpleName
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        context?.let {
            attach(context as Activity)
        }
    }

    private fun attach(activity: Activity) {
        if (activity is DestinationDialogActivityInterface) {
            activityInterface = activity
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_destination_dialog, container, true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        RxView.clicks(dialogCancelButton).subscribe { cancelButtonClicked() }
        RxView.clicks(dialogApplyButton).subscribe { applyButtonClicked() }
    }

    override fun onDestroy() {
        super.onDestroy()
        activityInterface = null
    }

    private fun cancelButtonClicked() {
        dismiss()
    }

    private fun applyButtonClicked() {
        activityInterface?.destinationCoordinatesEntered(latitudeInput.text.toString().toOptionalDouble(), longitudeInput.text.toString().toOptionalDouble())
        dismiss()
    }

}