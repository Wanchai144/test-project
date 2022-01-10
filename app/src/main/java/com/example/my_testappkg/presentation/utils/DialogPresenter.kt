package com.example.my_testappkg.presentation.utils

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.example.my_testappkg.R
import com.example.my_testappkg.databinding.DialogAlertInfoBinding

class DialogPresenter (private var context: Context) {




    fun dialogInfoChangeButton(
        title: String,
        text: String?,
        conText: String,
        canText: String,
        ClickCallback: ((Boolean) -> Unit)
    ) {

        val dialog = Dialog(context)
        dialog.setCanceledOnTouchOutside(false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val binding: DialogAlertInfoBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.dialog_alert_info, null, false
            )
        dialog.setContentView(binding.root)
        dialog.window?.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.attributes!!.width = (getDeviceMetrics(context).widthPixels * 0.8).toInt()

        binding.title = title
        binding.texterror = text
        binding.tvOkey.text = conText
        binding.tvCancle.text = canText

        binding.tvOkey.setOnClickListener {
            dialog.dismiss()
            ClickCallback.invoke(true)
        }
        binding.tvCancle.setOnClickListener {
            dialog.dismiss()
            ClickCallback.invoke(false)
        }

        dialog.show()
    }

    fun getDeviceMetrics(context: Context): DisplayMetrics {
        val metrics = DisplayMetrics()
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        display.getMetrics(metrics)
        return metrics
    }

    fun getDialog(): Dialog = Dialog(context)

    fun getDialogFullScreen(): Dialog = Dialog(context, R.style.Dialog_FullScreen)
}