package com.example.unscramblekotlin.ui

import android.os.Bundle
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.unscramblekotlin.data.DataProvider
import com.example.unscramblekotlin.data.MAX_NO_OF_WORDS
import com.example.unscramblekotlin.databinding.FragmentUnscrambleBinding
import com.example.unscramblekotlin.viewmodel.GameViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UnscrambleFragment : Fragment() {

    private lateinit var binding: FragmentUnscrambleBinding

    private val viewModel: GameViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUnscrambleBinding.inflate(inflater)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { uiState ->
                binding.wordCount.text = "${uiState.currentWordCount}/$MAX_NO_OF_WORDS"
                binding.textViewUnscrambledWord.text = uiState.currentScrambledWord
                binding.score.text = uiState.score.toString()
            }
        }

        binding.apply {
            skip.setOnClickListener { viewModel.skipWord() }
            submit.setOnClickListener { viewModel.checkUserGuess() }
            save.setOnClickListener { viewModel.saveGame() }
            save.setOnClickListener { viewModel.loadGame() }
            userInput.addTextChangedListener { viewModel.updateUserGuess(it.toString()) }
        }

        return binding.root
    }
}