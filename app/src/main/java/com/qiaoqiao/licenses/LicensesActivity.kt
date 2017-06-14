package com.qiaoqiao.licenses

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Bundle.EMPTY
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import com.qiaoqiao.R
import com.qiaoqiao.databinding.ActivityLicensesBinding

class LicensesActivity : AppCompatActivity() {


    companion object {
        fun showInstance(cxt: Activity) {
            val intent = Intent(cxt, LicensesActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            ActivityCompat.startActivity(cxt, intent, EMPTY)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityLicensesBinding>(this, R.layout.activity_licenses)
    }
}