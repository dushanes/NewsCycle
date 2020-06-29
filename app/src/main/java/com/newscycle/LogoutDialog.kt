package com.newscycle

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth

class LogoutDialog: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        return activity.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.logout_dialog)
                .setPositiveButton(R.string.yes,
                    DialogInterface.OnClickListener {dialog, _ ->
                        dialog.dismiss()
                        FirebaseAuth.getInstance().signOut()
                        activity?.finish()
                    })
                .setNegativeButton(R.string.no,
                    DialogInterface.OnClickListener { dialog, _ ->
                        dialog.dismiss()
                }).create()
        }
    }
}