package com.atname.itunesapp.router

import android.os.Bundle
import androidx.navigation.NavController
import com.atname.itunesapp.R

class AppRouter {
    var navController: NavController? = null

    fun toAlbumFragment(id:String){
        val bundle = Bundle()
        bundle.putString("albumId",id)
        navController!!.navigate(R.id.toAlbumFragment, bundle)
    }

    fun toSearchFragment(){
        navController?.navigate(R.id.toSearchFragment)
    }
}

