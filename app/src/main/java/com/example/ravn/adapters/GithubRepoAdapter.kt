package com.example.ravn.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ravn.R
import com.example.ravn.models.GithubRepo


class GithubRepoAdapter(private val context: Context, val items : ArrayList<GithubRepo>): RecyclerView.Adapter<GithubRepoAdapter.ViewHolder>(){

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.github_repo_content, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        Log.d("items", items.get(position).name)
        holder.nameView.text = items.get(position).name

        holder.ownerView.text = items.get(position).owner

        holder.descripcionView.text = items.get(position).descripcion

        holder.bottomRepoLayout.setOnClickListener(View.OnClickListener() {
        })
    }


    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    //3
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val nameView = view.findViewById<TextView>(R.id.nameRepoTextView)
        val ownerView = view.findViewById<TextView>(R.id.ownerRepoTextView)
        val descripcionView = view.findViewById<TextView>(R.id.descripcionRepoTextView)
        val bottomRepoLayout = view.findViewById<RelativeLayout>(R.id.bottomRepoLayout)
    }
}