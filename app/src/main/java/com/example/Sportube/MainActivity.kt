package com.example.Sportube

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.Sportube.databinding.ActivityMainBinding
import com.example.Sportube.homefragment.HomeFragment
import com.example.Sportube.mypagefragment.MypageFragment
import com.example.Sportube.searchfragment.SearchFragment
import com.example.Sportube.shortsFragment.ShortsFragment

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val homeFragment = HomeFragment()
    private val searchFragment = SearchFragment()
    private val mypageFragment = MypageFragment()
    private val shortsFragment = ShortsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        with(binding){
            bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.page_home -> {
                        replaceFragment(homeFragment)
                        true
                    }
                    R.id.mypage -> {
                        replaceFragment(mypageFragment)
                        true
                    }
                    R.id.search -> {
                        replaceFragment(searchFragment)
                        true
                    }
                    R.id.shorts -> {
                        replaceFragment(shortsFragment)
                        true
                    }
                    else -> false
                }
            }
            // 기본으로 첫 번째 아이템 선택
            bottomNavigationView.selectedItemId = R.id.page_home
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.linearLayout, fragment)
            .commit()
    }
}