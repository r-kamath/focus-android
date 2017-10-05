/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.focus.menu.home

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.mozilla.focus.R
import org.mozilla.focus.whatsnew.WhatsNew

class HomeMenuAdapter(
        private val context: Context,
        private val listener: View.OnClickListener
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items: List<MenuItem>

    init {
        items = mutableListOf()

        with(items) {
            add(MenuItem(R.id.whats_new, WhatsNewViewHolder.LAYOUT_ID, context.getString(R.string.menu_whats_new)))
            add(MenuItem(R.id.about, MenuItemViewHolder.LAYOUT_ID, context.getString(R.string.menu_about)))
            add(MenuItem(R.id.help, MenuItemViewHolder.LAYOUT_ID, context.getString(R.string.menu_help)))
            add(MenuItem(R.id.rights, MenuItemViewHolder.LAYOUT_ID, context.getString(R.string.menu_rights)))
            add(MenuItem(R.id.settings, MenuItemViewHolder.LAYOUT_ID, context.getString(R.string.menu_settings)))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(viewType, parent, false)

        return when (viewType) {
            WhatsNewViewHolder.LAYOUT_ID -> WhatsNewViewHolder(view, listener)
            MenuItemViewHolder.LAYOUT_ID -> MenuItemViewHolder(view as TextView, listener)
            else -> throw IllegalArgumentException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (holder) {
            is MenuItemViewHolder -> holder.bind(items[position])
            is WhatsNewViewHolder -> holder.bind()
            else -> throw IllegalArgumentException("Unknown view holder")
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int = items[position].viewType

    private class MenuItemViewHolder(
            val labelView: TextView,
            val listener: View.OnClickListener) : RecyclerView.ViewHolder(labelView) {

        companion object {
            val LAYOUT_ID: Int = R.layout.menu_item
        }

        fun bind(item: MenuItem) {
            labelView.id = item.id
            labelView.text = item.label

            labelView.setOnClickListener(listener)
        }
    }

    private class WhatsNewViewHolder(
            itemView: View,
            val listener: View.OnClickListener) : RecyclerView.ViewHolder(itemView) {
        val dotView : View = itemView.findViewById(R.id.dot)

        companion object {
            val LAYOUT_ID: Int = R.layout.menu_whatsnew
        }

        fun bind() {
            val updated = WhatsNew.wasUpdatedRecently(itemView.context)

            if (updated) {
                itemView.setBackgroundResource(R.drawable.menu_item_dark_background)
            }

            itemView.setOnClickListener(listener)

            dotView.visibility = if (updated) View.VISIBLE else View.GONE
        }
    }

    private class MenuItem(
            val id: Int,
            val viewType: Int,
            val label: String
    )
}