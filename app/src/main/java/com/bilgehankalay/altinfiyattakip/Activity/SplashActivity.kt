package com.bilgehankalay.altinfiyattakip.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bilgehankalay.altinfiyattakip.BackgroundService.MyBackgroundService
import com.bilgehankalay.altinfiyattakip.Global.SPLASH_ACTIVITY_TIME
import com.bilgehankalay.altinfiyattakip.Global.isServerOnline
import com.bilgehankalay.altinfiyattakip.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //BACKGROUND SERVICE
        val serviceIntent = Intent(this,MyBackgroundService::class.java)
        startService(serviceIntent)
        
        //ANIMATE
        binding.apply {
            splashActivityImageViewLogo.alpha = 0f
            splashActivityImageViewLogo.animate().setDuration(SPLASH_ACTIVITY_TIME).alpha(1f).withEndAction{
                val intent : Intent
                if (isServerOnline){
                    intent = Intent(this@SplashActivity,MainActivity::class.java)
                }
                else{
                    intent = Intent(this@SplashActivity,ServerErrorActivity::class.java)
                }
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                finish()


            }
        }


    }

}