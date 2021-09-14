package com.steve.jungsoos

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.steve.jungsoos.adapter.MyAdapter
import com.steve.jungsoos.adapter.OnItemClickListener
import com.steve.jungsoos.databinding.ActivityMainBinding
import com.steve.jungsoos.enums.ItemActionEnum
import com.steve.jungsoos.model.Item
import com.steve.jungsoos.model.ItemPlus
import com.steve.jungsoos.model.MainViewModel
import com.steve.jungsoos.model.Shopping
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity(), OnItemClickListener {
    private val TAG = "MainActivity"

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: MyAdapter
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        if (!mainViewModel.inited) {
            convertJsonFileToClass()
            mainViewModel.inited = true
        }

        binding.fab.setOnClickListener { view ->
            binding.ll1.visibility = View.VISIBLE
            binding.fab.visibility = View.INVISIBLE
            binding.info.visibility = View.INVISIBLE
            binding.newId.requestFocus()
        }

        binding.addNewItem.setOnClickListener{
            mainViewModel.addNewItemOrIncreaseCount(binding.newId.text.toString())
            binding.newId.text.clear()
            binding.ll1.visibility = View.INVISIBLE
            binding.fab.visibility = View.VISIBLE
            binding.info.visibility = View.VISIBLE
            val inputManager:InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.SHOW_FORCED)
//            it.hideKeyboard()
//            binding.newId.hideKeyboard(Activity.INPUT_SERVICE)
        }

        fun View.hideKeyboard() {
            val inputManager = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(windowToken, 0)
        }

        fun hideSoftKeyBoard(view: View) {
            try {
                val imm = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun View.hideKeyboard(inputMethodManager: InputMethodManager) {
            inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
        }

        binding.newId.addTextChangedListener {
            binding.addNewItem.isEnabled = binding.newId.text.toString().length != 0
        }

        recyclerView = binding.recycleView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MyAdapter(mainViewModel.shoppingList.value!!, this)
        recyclerView.adapter = adapter

        mainViewModel.shoppingRVList.observe(this) {
            adapter.notifyDataSetChanged()
        }
        mainViewModel.totalCost.observe(this) {
                price ->
                val s = "$ ${price.toString()}"
                binding.totalCost.text = s
        }
    }

    private fun convertJsonFileToClass() {
        val srcFileId: Int = R.raw.items
        val jsonFileReader  =
            BufferedReader(InputStreamReader(applicationContext.getResources().openRawResource(srcFileId)))

        val shoppingCart = Gson().fromJson(jsonFileReader, Shopping::class.java)
        mainViewModel.addAvailableList(shoppingCart as List<Item>)
        val itemPlusList = shoppingCart.map {item -> ItemPlus(item, 1) }
        mainViewModel.addShoppingList(itemPlusList)
        mainViewModel.updateTotalCost()
    }

    override fun onItemClicked(view: ImageView, position: Int, action: ItemActionEnum) {
        mainViewModel.actionOnShoppingItem(position, action)
    }
}