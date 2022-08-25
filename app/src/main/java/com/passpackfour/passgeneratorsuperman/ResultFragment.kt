package com.passpackfour.passgeneratorsuperman

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.passpackfour.passgeneratorsuperman.databinding.FragmentResultBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random

class ResultFragment : Fragment() {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(TMDBService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val serviceTMDB by lazy {
        retrofit.create(TMDBService::class.java)
    }
    var textPass = ""
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("ActivityMainBinding = null")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initProgBar()
        binding.btnCopy.setOnClickListener {
            try {
                saveToClipBoard()
            } catch (e: Exception) {
                initSnackBarError()
            }
        }
        binding.btnImgExit.setOnClickListener {
            initAlertDialog()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun initProgBar() {
        lifecycleScope.launch {
            binding.imgMain.visibility = View.GONE
            binding.btnCopy.visibility = View.GONE
            binding.tvResultText.visibility = View.GONE
            binding.btnImgExit.visibility = View.GONE
            binding.lottieAnimVaiting.visibility = View.VISIBLE
            binding.tvPleaseVaitLoading.visibility = View.VISIBLE
            delay(100)
            withContext(Dispatchers.IO) {
                try {
                    generatePass()
                } catch (e: Exception) {
                    initSnackBarError()
                }
            }
            delay(2000)
            binding.imgMain.visibility = View.VISIBLE
            binding.btnCopy.visibility = View.VISIBLE
            binding.tvResultText.visibility = View.VISIBLE
            binding.btnImgExit.visibility = View.VISIBLE
            binding.lottieAnimVaiting.visibility = View.GONE
            binding.tvPleaseVaitLoading.visibility = View.GONE
        }
    }

    private fun initSnackBarError() {
        Snackbar.make(binding.root, "There is some error, try again", Snackbar.LENGTH_LONG)
            .show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private suspend fun generatePass() {
        val res = serviceTMDB.getMoviByID()
        val list = res.body()!!.keywords!!.shuffled()
        list.forEach {
            if (textPass.length <= 64) {
                textPass = textPass + generateRandomNumber() + it?.name
                Log.d("testtag", "key is $textPass")
            }
        }
        val result = textPass.replace(" ", "")
        binding.tvResultText.text = result
    }

    private fun generateRandomNumber() = Random.nextInt(10, 100).toString()

    private fun saveToClipBoard() {
        val clipboardManager =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        ClipData.newPlainText("Encrypted text", textPass).also {
            clipboardManager.setPrimaryClip(it)
        }
        Snackbar.make(binding.root, "Copied!", Snackbar.LENGTH_LONG).show()
    }

    private fun initAlertDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Exit")
            .setMessage("Are you definitely want to log out, the current data will not be saved?")
            .setPositiveButton("Yes, Exit") { _, _ ->
                requireActivity().onBackPressed()
            }
            .setNegativeButton("Deny") { _, _ ->
            }
            .setCancelable(true)
            .create()
            .show()
    }
}