package com.worldsnas.mobile_ui.browse

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.worldsnas.mobile_ui.R
import com.worldsnas.mobile_ui.model.Project
import javax.inject.Inject

class BrowseAdapter @Inject constructor() : RecyclerView.Adapter<BrowseAdapter.ViewHolder>(){

    var projects : List<Project> = arrayListOf()
    var projectListener : ProjectListener? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_project, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val project = projects[position]

        holder?.ownerNameText?.text = project.ownerName
        holder?.projectNameText?.text = project.name
        Glide.with(holder?.itemView?.context)
                .load(project.ownerAvatar)
                .apply(RequestOptions.circleCropTransform())
                .into(holder?.avatarImage)

        val asset : Int
        if (project.isBookmarked) {
            asset = R.drawable.ic_star_black_24dp
        }else{
            asset = R.drawable.ic_star_border_black_24dp
        }

        holder?.bookmarkedProject?.setImageResource(asset)

        holder?.itemView?.setOnClickListener{
            if (project.isBookmarked){
                projectListener?.onBookmarkedProjectClicked(projectId = project.id)
            }else{
                projectListener?.onProjectClicked(projectId = project.id)
            }
        }
    }

    override fun getItemCount(): Int {
        return projects.size
    }

    inner class ViewHolder(view: View) :RecyclerView.ViewHolder(view){
        var avatarImage : ImageView
        var ownerNameText: TextView
        var projectNameText : TextView
        var bookmarkedProject : ImageView

        init {
            avatarImage = view.findViewById(R.id.imgOwnerAvatar)
            ownerNameText = view.findViewById(R.id.imgOwnerName)
            projectNameText = view.findViewById(R.id.imgProjectName)
            bookmarkedProject = view.findViewById(R.id.imgBookmarked)
        }


    }
}