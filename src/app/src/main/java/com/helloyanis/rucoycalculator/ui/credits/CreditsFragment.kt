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
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.play.core.review.ReviewManagerFactory
import com.helloyanis.rucoycalculator.R
import com.helloyanis.rucoycalculator.databinding.CreditsBinding

class CreditsFragment : Fragment() {

    private var _binding: CreditsBinding? = null
    private val binding get() = _binding!!

    private val colors = listOf(
        Color.RED, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA
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
        val privacybutton = binding.privacy

        privacybutton.setOnClickListener{
            val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://gist.githubusercontent.com/helloyanis/25ae600a5d2d162a2912ec0a24ed2084/raw/74708abcb5688e54ebb59ac91830a3d4a2d26ba3/privacy.md"))
            startActivity(i)
        }

        val githubbutton = binding.github

        githubbutton.setOnClickListener{
            val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/helloyanis/rucoy-calculator"))
            startActivity(i)
        }

        val ratebutton = binding.rate

        ratebutton.setOnClickListener{
                val manager = ReviewManagerFactory.create(requireContext())
                val request = manager.requestReviewFlow()
                request.addOnCompleteListener { request ->
                    if (request.isSuccessful) {
                        // We got the ReviewInfo object
                        val reviewInfo = request.result
                        val flow = manager.launchReviewFlow(requireActivity(), reviewInfo)
                        flow.addOnCompleteListener { _ ->
                            binding.root.findViewById<TextView>(R.id.rate).visibility=View.GONE
                            binding.root.findViewById<TextView>(R.id.ratebackup).visibility=View.VISIBLE

                        }
                    } else {
                        Toast.makeText(context, "Could not find the Play Store installed on your device!", Toast.LENGTH_LONG).show()
                    }
                }
        }

        val ratebackupbutton = binding.ratebackup

        ratebackupbutton.setOnClickListener{
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.helloyanis.rucoycalculator")))
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
