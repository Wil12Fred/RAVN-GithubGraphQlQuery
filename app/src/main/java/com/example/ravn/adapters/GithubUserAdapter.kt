package com.example.ravn.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ravn.R
import com.example.ravn.models.GithubUser
import com.example.ravn.view.RepositoriesActivity


class GithubUserAdapter(private val context: Context, val items : ArrayList<GithubUser>): RecyclerView.Adapter<GithubUserAdapter.ViewHolder>(){

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.github_user_content, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        holder.topLayout.visibility = RelativeLayout.GONE

        holder.nameView.text = items.get(position).names

        holder.groupView.text = items.get(position).group

        holder.percView.text = items.get(position).username

        if (items.get(position).state)
            holder.stateButton.setImageResource(android.R.drawable.presence_online)
        else
            holder.stateButton.setImageResource(android.R.drawable.presence_busy)
        if (items.get(position).state){
            holder.stateButton.setImageResource(android.R.drawable.presence_online)
            holder.bottomLayout.setBackgroundColor(Color.GREEN)
            holder.nameView.setTextColor(Color.BLACK)
            holder.percView.setTextColor(Color.BLACK)
        } else {
            holder.stateButton.setImageResource(android.R.drawable.presence_busy)
            holder.bottomLayout.setBackgroundColor(Color.RED)
            holder.nameView.setTextColor(Color.WHITE)
            holder.percView.setTextColor(Color.WHITE)
        }
        holder.bottomLayout.setOnClickListener(View.OnClickListener() {
            var intent = Intent(context, RepositoriesActivity::class.java)
            intent.putExtra("username", items.get(position).username)
            context.startActivity(intent)
        })
    }


    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    //3
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val nameView = view.findViewById<TextView>(R.id.nameTextView)
        val groupView = view.findViewById<TextView>(R.id.groupTextView)
        val stateButton = view.findViewById<ImageButton>(R.id.stateButton)
        val percView = view.findViewById<TextView>(R.id.estadoTextView)
        val topLayout = view.findViewById<RelativeLayout>(R.id.topLayout)
        val bottomLayout = view.findViewById<RelativeLayout>(R.id.bottomLayout)
    }
}