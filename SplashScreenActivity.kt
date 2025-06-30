package com.example.tic_tak_toe

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create layout
        val splashLayout = RelativeLayout(this).apply {
            setBackgroundColor(android.graphics.Color.parseColor("#f5faff")) // theme background
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
            )
        }

        // Create logo image
        val logo = ImageView(this).apply {
            setImageResource(R.drawable.logo)  // Ensure logo.jpg is in res/drawable
            layoutParams = RelativeLayout.LayoutParams(400, 400).apply {
                addRule(RelativeLayout.CENTER_IN_PARENT)
            }
        }

        splashLayout.addView(logo)
        setContentView(splashLayout)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 1500) // 1.5 second splash duration
    }
}
