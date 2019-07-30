package com.example.ducius

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.BounceInterpolator
import android.view.animation.TranslateAnimation
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.ducius.fragment.ShowsContainerActivity
import com.example.ducius.model.RegisterInfo
import com.example.ducius.shared.visible
import com.example.ducius.ui.LoginActivity
import com.example.ducius.ui.SplashActivityViewModel
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private lateinit var viewModel: SplashActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        viewModel = ViewModelProviders.of(this).get(SplashActivityViewModel::class.java)

        val textAnimation = AnimationUtils.loadAnimation(this, R.anim.pop_animation)
        textAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                loadUser()
            }
            override fun onAnimationStart(animation: Animation?) {}
        })

        val translateAnimation = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, 0f,
            Animation.RELATIVE_TO_PARENT, 0f,
            Animation.RELATIVE_TO_PARENT, 0f,
            Animation.RELATIVE_TO_PARENT, 1f)

        with(translateAnimation) {
            repeatCount = TranslateAnimation.ABSOLUTE
            fillAfter = true
            duration = 1500
            interpolator = BounceInterpolator()
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    showsSplash.startAnimation(textAnimation)
                    showsSplash.visible()
                }

                override fun onAnimationStart(animation: Animation?) {}
            })
        }
        dropDownLogo.startAnimation(translateAnimation)

    }

    private fun loadUser() {
        val username: String = viewModel.loadUsernameFromPrefrences()?.trim() ?: ""
        val password: String = viewModel.loadPasswordFromPrefrences()?.trim() ?: ""
        viewModel.getUserData(RegisterInfo(username, password))
        viewModel.liveData.observe(this, Observer {
            if (it.isSucccessful) {
                with(MyShowsApp.instance.getSharedPreferences(LoginActivity.PREFS_NAME, Context.MODE_PRIVATE).edit()) {
                    putString(LoginActivity.TOKEN, it.token?.token)
                    apply()
                }
                startActivity(Intent(this, ShowsContainerActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            } else {
                startActivity(Intent(this, LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
        })
    }
}
