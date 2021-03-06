package com.example.productmvvmapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.productmvvmapp.core.BaseViewHolder
import com.example.productmvvmapp.data.model.Product
import com.example.productmvvmapp.databinding.ItemCartRowBinding
import com.example.productmvvmapp.presentation.CartViewModel

class CartAdapter(
    private val context: Context,
    private val itemClickListener: OnCartClickListener
) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    private var cartList = listOf<Product>()

    interface OnCartClickListener {
        fun onCartListener(product: Product, position: Int)
        fun onCartQuantityListener(products: List<Product>,product: Product,position: Int)
    }

    fun setCartList(cartList: List<Product>) {
        this.cartList = cartList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemBinding = ItemCartRowBinding.inflate(LayoutInflater.from(context), parent, false)
        val holder = MainViewHolder(itemBinding)


        return holder
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is MainViewHolder -> holder.bind(cartList[position], position)
        }

    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    private inner class MainViewHolder(
        private val binding: ItemCartRowBinding
    ) : BaseViewHolder<Product>(binding.root) {

        override fun bind(item: Product, position: Int) {
            val model = cartList[position]

            Glide.with(context).load(item.image).centerCrop().into(binding.imgCart)
            binding.txtTitleCart.text = item.name
            binding.txtDeriptionCart.text = item.description
            binding.txtPriceCart.text = "MXN ${item.price}"
            binding.amountCart.text = "${item.quantity}"

            binding.btnEraseCart.setOnClickListener {
                val position = adapterPosition.takeIf {it != NO_POSITION}
                    ?: return@setOnClickListener

                itemClickListener.onCartListener(cartList[position],position)
            }

            binding.plusCart.setOnClickListener {

                val position = adapterPosition.takeIf {it != NO_POSITION}
                    ?: return@setOnClickListener
                    if(model.quantity < model.totalRating){
                        model.quantity++
                        binding.amountCart.text = "${model.quantity}"
                    }
               itemClickListener.onCartQuantityListener(cartList,cartList[position],position)

            }

            binding.miniumCart.setOnClickListener {
                val position = adapterPosition.takeIf {it != NO_POSITION}
                    ?: return@setOnClickListener
                if(model.quantity > 0){
                    model.quantity--
                    binding.amountCart.text = "${model.quantity}"
                }
                itemClickListener.onCartQuantityListener(cartList,cartList[position],position)
            }
        }
    }
}