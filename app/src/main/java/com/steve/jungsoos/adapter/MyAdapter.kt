package com.steve.jungsoos.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.steve.jungsoos.databinding.ListItemBinding
import com.steve.jungsoos.enums.ItemActionEnum

import com.steve.jungsoos.model.ItemPlus
import com.steve.jungsoos.util.GlideApp

class MyAdapter (private val itemList: MutableList<ItemPlus>, val itemClickListener: OnItemClickListener)
    : RecyclerView.Adapter<MyAdapter.MyViewHolder>(){

    inner class MyViewHolder(private val itemBinding: ListItemBinding): RecyclerView.ViewHolder (itemBinding.root) {
        fun bind(itemPlus: ItemPlus, position: Int, clickListener: OnItemClickListener) {
            itemBinding.name.text = itemPlus.item.name
            itemBinding.itemId.text = itemPlus.item.id
            itemBinding.etAmount.text = itemPlus.quantity.toString()
            GlideApp.with(itemBinding.root.context)
                .load(itemPlus.item.thumbnail)
                .override(100,100)
                .into(itemBinding.image)
            itemBinding.plusOne.setOnClickListener{
                clickListener.onItemClicked(itemBinding.plusOne, position, ItemActionEnum.PLUS_ONE)
            }
            itemBinding.minusOne.setOnClickListener{
                clickListener.onItemClicked(itemBinding.minusOne, position, ItemActionEnum.MINUS_ONE)
            }
            itemBinding.trash.setOnClickListener{
                clickListener.onItemClicked(itemBinding.trash, position, ItemActionEnum.REMOVE)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(itemList.get(position), position, itemClickListener)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}

interface OnItemClickListener{
    fun onItemClicked(view: ImageView, index: Int, action: ItemActionEnum)
}
