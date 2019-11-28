package com.example.ravn.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.ravn.adapters.GithubUserAdapter
import com.example.ravn.models.GithubUser
import kotlinx.android.synthetic.main.github_user_layout.*
import okhttp3.OkHttpClient
import java.util.logging.Logger
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.inputmethod.InputMethodManager
import android.widget.AbsListView
import android.widget.TextView
import com.example.ravn.R
import com.example.ravn.utilities.ApolloUtils
import com.example.ravn.utilities.Constants


class MainActivity : AppCompatActivity() {
    private val BASE_URL = Constants.BASE_URL
    private lateinit var client: ApolloClient
    var userList = ArrayList<GithubUser>()
    private lateinit var context:  Context
    private lateinit var listView: RecyclerView
    private lateinit var adapter : GithubUserAdapter
    private lateinit var lastVisible : String
    //private lateinit var refreshLayout : SwipeRefreshLayout

    companion object {
        val Log = Logger.getLogger(MainActivity::class.java.name)
    }

    fun hideKeyboard() {
        val inputManager: InputMethodManager =getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.SHOW_FORCED)
    }

    fun loadUsersAfterFilter(client: ApolloClient, after: String){
        progress_bar.visibility = View.VISIBLE
        var pagesize = 5
        client.query(ListUserAfterQuery
            .builder()
            .owner(owner_name_edittext.text.toString())
            .pagesize(pagesize)
            .after(after)
            .build())
            .enqueue(object : ApolloCall.Callback<ListUserAfterQuery.Data>(){
                override fun onFailure(e: ApolloException) {
                    Log.info("Error: " + e.message.toString())
                }

                override fun onResponse(response: Response<ListUserAfterQuery.Data>) {
                    //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    runOnUiThread({
                        progress_bar.visibility = View.GONE
                        var edges = response.data()?.search()?.edges()
                        if((edges?.size!!>0)) {
                            edges?.forEach {
                                    it ->
                                var profile = it?.node()?.fragments()?.profile()
                                userList.add(0, GithubUser(userList.size, profile?.id()!!, profile?.name()!!, 7, profile?.login()))
                            }
                            var recyclerViewState =
                                listView.getLayoutManager()!!.onSaveInstanceState();
                            adapter.notifyItemRangeInserted(0, edges?.size!! - 1)
                            listView.layoutManager!!.onRestoreInstanceState(recyclerViewState)
                            (listView.layoutManager as LinearLayoutManager).scrollToPosition(edges?.size!! - 1)
                            lastVisible = response?.data()?.search()?.pageInfo()?.endCursor()!!
                        }
                    })
                }
            })
    }

    fun loadUsersFilter(client: ApolloClient){
        progress_bar.visibility = View.VISIBLE
        var pagesize = 5
        client.query(ListUserQuery
            .builder()
            .owner(owner_name_edittext.text.toString())
            .pagesize(pagesize)
            .build())
            .enqueue(object : ApolloCall.Callback<ListUserQuery.Data>(){
                override fun onFailure(e: ApolloException) {
                    Log.info("Error: " + e.message.toString())
                }

                override fun onResponse(response: Response<ListUserQuery.Data>) {
                    //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    runOnUiThread({
                        progress_bar.visibility = View.GONE
                        Log.info("Response: " + response.data()?.search())
                        var edges = response.data()?.search()?.edges()
                        var currentPosition = userList.size
                        if((edges?.size!!>0)){
                            edges?.forEach {
                                    it ->
                                var profile = it?.node()?.fragments()?.profile()
                                userList.add(0, GithubUser(userList.size, profile?.id()!!, profile?.name()!!, 7, profile?.login()))
                            }
                            Log.info("listView " + userList.size)
                            adapter.notifyItemRangeInserted(0, edges?.size!! - 1)
                                (listView.layoutManager as LinearLayoutManager).scrollToPosition(
                                    edges?.size!! - 1
                                )
                                lastVisible = response?.data()?.search()?.pageInfo()?.endCursor()!!
                        }
                    })
                }
            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this
        client = ApolloUtils.setupApollo()
        listView = findViewById<RecyclerView>(R.id.list_github_users)
        val layoutManager = LinearLayoutManager(this)
        listView.setLayoutManager(layoutManager)
        //studentList.add(GithubUser(15,"ICC","Names", 7))
        adapter = GithubUserAdapter(this, userList)
        listView.adapter = adapter
        //(listView.layoutManager as LinearLayoutManager).stackFromEnd = true
        //refreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)
        listView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                val positionView =
                    (listView.getLayoutManager() as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    //Log.info("0 " + positionView)
                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    //Log.info("1 " + positionView)
                } else {
                    //Log.info("2 " + positionView)
                    if ((positionView==0)  and (userList.size<50)){
                        Log.info("listView " + userList.size)
                        loadUsersAfterFilter(client, lastVisible)
                    }
                }
            }
        })
        button_find.setOnClickListener({
            github_image_view.visibility = View.GONE
            userList.clear()
            adapter.notifyDataSetChanged()
            hideKeyboard()
            if (owner_name_edittext.text.toString().equals("")){
            } else {
                loadUsersFilter(client)
            }
        })

    }
}
