package com.steve.jungsoos.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.steve.jungsoos.enums.ItemActionEnum
import com.steve.jungsoos.util.ItemUtil

class MainViewModel : ViewModel() {
    // available items from the vendor. This should be provided from the server.
    var availableItems: MutableList<Item> = mutableListOf()
    var shoppingList: MutableLiveData<MutableList<ItemPlus>> = MutableLiveData(mutableListOf())
    val shoppingRVList: LiveData<MutableList<ItemPlus>> = shoppingList
    private val totalCostLiveData = MutableLiveData<Float>()
    val totalCost: LiveData<Float> = totalCostLiveData
    var inited = false

    fun addAvailableList(items: List<Item>) {
        availableItems.addAll(items)
    }
    fun addShoppingList(list: List<ItemPlus>) {
        val oldList = shoppingList.value ?: mutableListOf()
        oldList.addAll(list)
        shoppingList.value = oldList
    }

    fun actionOnShoppingItem(position: Int, action: ItemActionEnum) {
        val shoppingItemList = shoppingList.value
        val shoppingItem = shoppingItemList?.get(position)
        if (shoppingItem != null) {
            when (action) {
                ItemActionEnum.PLUS_ONE -> shoppingItem.quantity++
                ItemActionEnum.MINUS_ONE -> {
                    shoppingItem.quantity--
                    if (shoppingItem.quantity <= 0)
                        shoppingItemList.removeAt(position)
                }
                ItemActionEnum.REMOVE -> shoppingItemList.removeAt(position)
                else -> println("ERROR: Wrong Action enums: $action")
            }
        }
        shoppingList.postValue(shoppingItemList)
        updateTotalCost()
    }

    fun addNewItemOrIncreaseCount(itemId: String) {
        val itemPlusList: MutableList<ItemPlus> = shoppingList.value ?: mutableListOf()
        var findItem = false

        val Id = ItemUtil().getIdString(itemId)
        // if this item already exist in the list:
        if (itemPlusList.size > 0) {
            for (i: Int in 0 until itemPlusList.size) {
                if (itemPlusList.get(i).item.id.removePrefix("$") == Id) {
                    itemPlusList.get(i).quantity++
                    findItem = true
                }
            }
        }
        if (findItem) {
            shoppingList.postValue(itemPlusList)
        } else {
            val item: Item = createItemByItemId(Id)
            itemPlusList.add(ItemPlus(item, 1))
            shoppingList.postValue(itemPlusList)
        }
        updateTotalCost()
    }

    fun createItemByItemId(itemId: String) : Item {
        return Item(
             itemId,
            ItemUtil().getName(itemId),
            "$" + itemId,
            ItemUtil().getQrUrl(itemId),
            ItemUtil().getThumbNail(itemId)
        )
    }

    fun updateTotalCost() {
        var totalCost = 0f
        shoppingList.value?.map{
            totalCost += it.quantity * it.item.price.removePrefix("$").toFloat()
        }
        totalCostLiveData.postValue(totalCost)
    }
}