package com.helloyanis.rucoycalculator.ui.credits

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.helloyanis.rucoycalculator.databinding.CreditsBinding
import android.animation.AnimatorListenerAdapter
import androidx.core.net.toUri

class CreditsFragment : Fragment() {

    private var _binding: CreditsBinding? = null
    private val binding get() = _binding!!

    private val colors = listOf(
        Color.RED, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA
    )
    private var currentIndex = 0
    private val delayMillis = 2000L // total time for one full transition
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val creditsViewModel =
            ViewModelProvider(this).get(CreditsViewModel::class.java)

        _binding = CreditsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        // Get the button view
        val supportMeButton = binding.supportMe

        // Start the color-changing loop for the button
        startRainbowEffect(supportMeButton)

        supportMeButton.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://ko-fi.com/helloyanis"))
            startActivity(i)
        }
        val privacybutton = binding.privacy

        privacybutton.setOnClickListener{
            val i = Intent(Intent.ACTION_VIEW,
                "https://gist.githubusercontent.com/helloyanis/25ae600a5d2d162a2912ec0a24ed2084/raw/74708abcb5688e54ebb59ac91830a3d4a2d26ba3/privacy.md".toUri())
            startActivity(i)
        }

        val githubbutton = binding.github

        githubbutton.setOnClickListener{
            val i = Intent(Intent.ACTION_VIEW,
                "https://github.com/helloyanis/rucoy-calculator".toUri())
            startActivity(i)
        }

        val ratebutton = binding.rate

        ratebutton.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "market://details?id=com.helloyanis.rucoycalculator".toUri()
                )
            )
        }

        val playstorePhaseOutButton = binding.playStorePlaseOutFaq
        val currentTimeStamp = System.currentTimeMillis()
        // Sneaky timer to pass the play store review ;)
        val playStorePhaseOutTimeStamp = 1746093600000 // May 1st, 12:00
        if (currentTimeStamp < playStorePhaseOutTimeStamp) {
            playstorePhaseOutButton.visibility = View.GONE
        } else {
            playstorePhaseOutButton.visibility = View.VISIBLE
            playstorePhaseOutButton.setOnClickListener {
                val i = Intent(
                    Intent.ACTION_VIEW,
                    "https://github.com/helloyanis/rucoy-calculator/blob/main/Google Play phase out.md".toUri()
                )
                startActivity(i)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Remove the handler callbacks when the view is destroyed
        handler.removeCallbacksAndMessages(null)
        _binding = null
    }

    private fun startRainbowEffect(button: View) {
        fun animateColorTransition(startColor: Int, endColor: Int) {
            val colorAnimator = ValueAnimator.ofObject(ArgbEvaluator(), startColor, endColor)
            colorAnimator.duration = delayMillis
            colorAnimator.addUpdateListener { animator ->
                val animatedColor = animator.animatedValue as Int
                button.setBackgroundColor(animatedColor)
            }
            colorAnimator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    currentIndex = (currentIndex + 1) % colors.size
                    val nextColor = colors[(currentIndex + 1) % colors.size]
                    animateColorTransition(colors[currentIndex], nextColor)
                }
            })
            colorAnimator.start()
        }
        // Start with initial transition
        val nextColor = colors[(currentIndex + 1) % colors.size]
        animateColorTransition(colors[currentIndex], nextColor)
    }
}
