package com.example.ravn.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.ravn.R
import com.example.ravn.adapters.GithubRepoAdapter
import com.example.ravn.models.GithubRepo
import com.example.ravn.models.GithubUser
import com.example.ravn.utilities.ApolloUtils
import fragment.RepositoryDetail
import kotlinx.android.synthetic.main.activity_repositories.*


class RepositoriesActivity : AppCompatActivity() {

    private lateinit var client: ApolloClient
    private lateinit var username: String
    private lateinit var listView: RecyclerView
    private lateinit var adapter : GithubRepoAdapter
    var repoList = ArrayList<GithubRepo>()


    fun findRepo(client: ApolloClient, repository: String) {
        client.query(
            FindRepoQuery
                .builder()
                .name(repository)
                .owner(username)
                .build()
        )
            .enqueue(object : ApolloCall.Callback<FindRepoQuery.Data>() {

                override fun onFailure(e: ApolloException) {
                    MainActivity.Log.info(e.message.toString())
                }

                override fun onResponse(response: Response<FindRepoQuery.Data>) {
                    MainActivity.Log.info(" " + response.data()?.repository())
                    runOnUiThread({
                        var repository = response?.data()?.repository()
                    })
                }

            })
    }


    fun loadRepoByUser(client: ApolloClient) {
        client.query(
            ListRepoUserQuery
                .builder()
                .owner("org:" + username)
                .pagesize(10)
                .build()
        )
            .enqueue(object : ApolloCall.Callback<ListRepoUserQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    Log.d("Error: ", e.message.toString())
                }

                override fun onResponse(response: Response<ListRepoUserQuery.Data>) {
                    runOnUiThread({
                        Log.d("LOADREPO: ", response.toString())
                        var search = response?.data()?.search()?.edges()
                        var repositoriesSize = search?.size!!
                        if(repositoriesSize>0){
                            search?.forEach{
                                it->
                                var repository = it?.node()?.fragments()?.repo()
                                repoList.add(0,GithubRepo(repoList.size,
                                repository?.id()!!,
                                repository?.name()!!,
                                repository?.owner()?.login()!!,
                                repository?.description()!!))
                                adapter.notifyItemRangeInserted(0, repositoriesSize!! - 1)
                            }
                        }
                    })
                }
            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repositories)
        username = intent.getStringExtra("username")
        val usernameTextView = findViewById<TextView>(R.id.usernameGithub)
        usernameTextView.text = username
        client = ApolloUtils.setupApollo()
        Log.d("USERNAME: ", username)
        loadRepoByUser(client)
        listView = findViewById<RecyclerView>(R.id.list_repositories)
        val layoutManager = LinearLayoutManager(this)
        listView.setLayoutManager(layoutManager)
        adapter = GithubRepoAdapter(this, repoList)
        listView.adapter = adapter
    }
}