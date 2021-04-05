package com.newscycle

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.newscycle.viewmodel.LoginViewModel

class LogoutDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val loginViewModel: LoginViewModel =
            ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
        return activity.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.logout_dialog)
                .setPositiveButton(R.string.yes,
                    DialogInterface.OnClickListener { dialog, _ ->
                        dialog.dismiss()
                        loginViewModel.logOut()
                        val intent = Intent(it, Login::class.java)
                        startActivity(intent)
                        it?.finish()
                        it?.overridePendingTransition(
                            R.anim.slide_enter_left,
                            R.anim.slide_exit_right
                        )
                    })
                .setNegativeButton(R.string.no,
                    DialogInterface.OnClickListener { dialog, _ ->
                        dialog.dismiss()
                    }).create()
        }
    }
}