package com.example.simplerecipehelper.ui.cooking

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.simplerecipehelper.databinding.FragmentCookingBinding

class CookingFragment : Fragment(), TextToSpeech.OnInitListener {

    private var _binding: FragmentCookingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CookingViewModel by activityViewModels()

    private var textToSpeech: TextToSpeech? = null
    private var isSpeaking: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        textToSpeech = TextToSpeech(requireContext(), this)

        viewModel.steps.observe(viewLifecycleOwner) { steps ->
            updateStepText(steps, viewModel.currentIndex.value ?: 0)
        }

        viewModel.currentIndex.observe(viewLifecycleOwner) { index ->
            updateStepText(viewModel.steps.value.orEmpty(), index)
        }

        binding.nextButton.setOnClickListener {
            viewModel.nextStep()
            speakCurrentStep()
        }
        binding.previousButton.setOnClickListener {
            viewModel.previousStep()
            speakCurrentStep()
        }
        binding.markCompleteButton.setOnClickListener { viewModel.markComplete() }

        // Exit button
        binding.exitButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.playPauseButton.setOnClickListener {
            if (isSpeaking) {
                textToSpeech?.stop()
                isSpeaking = false
                binding.playPauseButton.text = "▶"
            } else {
                // Always start reading from step 1 when play is pressed
                viewModel.resetToFirstStep()
                speakCurrentStep()
            }
        }

        binding.speedSeekBar.progress = 10 // 1.0x
        binding.speedSeekBar.setOnSeekBarChangeListener(object :
                android.widget.SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: android.widget.SeekBar?, progress: Int, fromUser: Boolean) {
                    val rate = (progress / 10f).coerceIn(0.5f, 2.0f)
                    textToSpeech?.setSpeechRate(rate)
                }

                override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {}
            })
    }

    private fun updateStepText(steps: List<String>, index: Int) {
        if (steps.isEmpty()) {
            binding.stepTextView.text = "No recipe selected"
            binding.stepCounterTextView.text = ""
        } else {
            val safeIndex = index.coerceIn(0, steps.size - 1)
            binding.stepTextView.text = steps[safeIndex]
            binding.stepCounterTextView.text = "${safeIndex + 1}/${steps.size}"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        textToSpeech = null
    }

    private fun speakCurrentStep() {
        val steps = viewModel.steps.value.orEmpty()
        val index = viewModel.currentIndex.value ?: 0
        if (steps.isEmpty()) return
        val text = steps[index.coerceIn(0, steps.size - 1)]
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "step_$index")
        isSpeaking = true
        binding.playPauseButton.text = "⏸"
    }

    override fun onInit(status: Int) {
        // No-op; using default language
    }
}


