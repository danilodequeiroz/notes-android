package com.danilodequeiroz.notes.ui.widget

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.danilodequeiroz.notes.R
import com.danilodequeiroz.notes.ui.login.LoginActivity
import com.danilodequeiroz.notes.ui.notelist.LoginDataClickLisneter
import com.danilodequeiroz.notes_persistence.LoginData
import java.util.*
import kotlin.collections.ArrayList


class LoginDataAdapter(
    context: Context, private val resourceId: Int,
    private val items: MutableList<LoginData>,
    private val dataClickLisneter: LoginDataClickLisneter
) : ArrayAdapter<LoginData>(context, resourceId, items) {

    private var tempItems: MutableList<LoginData> = mutableListOf()
    private var suggestions: MutableList<LoginData> = mutableListOf()

    init {
        tempItems = ArrayList(items)
        suggestions = ArrayList()
    }

    private val loginDataFilter = object : Filter() {
        override fun convertResultToString(resultValue: Any): CharSequence {
            val loginData = resultValue as LoginData
            return loginData.email
        }

        override fun performFiltering(charSequence: CharSequence?): FilterResults {
            return if (charSequence != null) {
                suggestions.clear()
                for (loginData in tempItems) {
                    if (loginData.email.toLowerCase(Locale("pt", "BR")).startsWith(charSequence.toString())) {
                        suggestions.add(loginData)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = suggestions
                filterResults.count = suggestions.size
                filterResults
            } else {
                FilterResults()
            }
        }

        override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults?) {
            filterResults?.values?.let {
                val tempValues : MutableList<LoginData> = it as MutableList<LoginData>
                if (filterResults.count > 0) {
                    clear()
                    for (loginData in tempValues) {
                        add(loginData)
                    }
                    notifyDataSetChanged()
                } else {
                    clear()
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView

        if (convertView == null) {
            val inflater = (context as Activity).layoutInflater
            view = inflater.inflate(resourceId, parent, false)
        }
        val loginData = getItem(position)
        val name = view?.findViewById(R.id.textView) as TextView
        name.text = loginData?.email
        view.setOnClickListener { loginData?.let { it -> dataClickLisneter.onLoginDataCicked(it) } }
        return view
    }


    override fun getItem(position: Int): LoginData? {
        return items[position]
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getFilter(): Filter {
        return loginDataFilter
    }
}