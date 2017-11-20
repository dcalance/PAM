package com.example.user.lab3


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_add_rss.*
import kotlinx.android.synthetic.main.fragment_rss_list.*
import kotlinx.android.synthetic.main.rss_list_layout.*


class RssListFragment : Fragment() {



    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater!!.inflate(R.layout.fragment_rss_list, container, false)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val db = DatabaseHandler(activity)
        val mainActivity = activity as MainActivity
        listRss.adapter = mainActivity.rssInfoAdapter
        mainActivity.rssInfoAdapter.addAll(db.getAllItems())
        mainActivity.rssInfoAdapter.notifyDataSetChanged()
        addBtn.setOnClickListener {_ ->
            val intent = Intent(activity, AddRssActivity::class.java)
            intent.putExtra("request", mainActivity.REQUEST_ADD_RSS)

            startActivityForResult(intent, mainActivity.REQUEST_ADD_RSS)
        }
        listRss.setOnItemClickListener{ parent, _, position, _ ->
            val currentElement = parent.adapter.getItem(position) as RssInfo
            val intent = Intent(activity, DisplayRssActivity::class.java)
            intent.putExtra("link", currentElement.link)
            startActivity(intent)
        }
        registerForContextMenu(listRss)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo)
        if (v.id == R.id.listRss) {
            val inflater = activity.menuInflater
            inflater.inflate(R.menu.menu_list, menu)
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val mainActivity = activity as MainActivity
        when (item.itemId) {
            R.id.edit -> {
                val intent = Intent(activity, AddRssActivity::class.java)
                intent.putExtra("itemId", mainActivity.rssInfoAdapter.getItem(info.position).id)
                intent.putExtra("request", mainActivity.REQUEST_EDIT_RSS)
                startActivityForResult(intent, mainActivity.REQUEST_EDIT_RSS)
                return true
            }
            R.id.delete -> {
                val db = DatabaseHandler(activity)
                db.deleteItem(mainActivity.rssInfoAdapter.getItem(info.position).id)
                mainActivity.updateList()
                return true
            }
            else -> return super.onContextItemSelected(item)
        }
    }


}
