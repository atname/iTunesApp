package com.atname.itunesapp.router

import android.os.Bundle
import androidx.navigation.NavController
import com.atname.itunesapp.R

class AppRouter {
    lateinit var navController: NavController

    fun toAlbumFragment(id:String){
        val bundle = Bundle()
        bundle.putString("albumId",id)
        navController.navigate(R.id.toAlbumFragment, bundle)
    }
}

