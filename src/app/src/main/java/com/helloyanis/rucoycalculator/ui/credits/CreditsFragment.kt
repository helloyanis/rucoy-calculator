package com.helloyanis.rucoycalculator.ui.credits

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.helloyanis.rucoycalculator.databinding.CreditsBinding

class CreditsFragment : Fragment() {

    private var _binding: CreditsBinding? = null
    private val binding get() = _binding!!

    private val colors = listOf(
        Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA
    )
    private var currentIndex = 0
    private val handler = Handler(Looper.getMainLooper())
    private val delayMillis = 2000L // Change color every 1 second

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

        val githubbutton = binding.github

        githubbutton.setOnClickListener{
            val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/helloyanis/rucoy-calculator"))
            startActivity(i)
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
        handler.postDelayed(object : Runnable {
            override fun run() {
                // Change the button's background color
                button.setBackgroundColor(colors[currentIndex])

                // Increment the index, or loop back to 0 if it reaches the end
                currentIndex = (currentIndex + 1) % colors.size

                // Schedule the next color change
                handler.postDelayed(this, delayMillis)
            }
        }, delayMillis)
    }
}
