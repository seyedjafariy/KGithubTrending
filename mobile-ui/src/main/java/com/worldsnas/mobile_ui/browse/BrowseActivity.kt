package com.worldsnas.mobile_ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_browse.*

class BrowseActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse)

        initBrowseRecycler()
    }

    private fun initBrowseRecycler() {
        rvProjects.layoutManager = LinearLayoutManager(this)
    }
}