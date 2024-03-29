package com.example.Sportube.homefragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.Sportube.BuildConfig
import com.example.Sportube.homefragment.HomeFragment.Companion.channelList
import com.example.Sportube.homefragment.HomeFragment.Companion.videoList
import com.example.Sportube.retrofit.NetWorkClient
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _searchParam: MutableLiveData<HashMap<String, String>> = MutableLiveData()
    val searchParam: LiveData<HashMap<String, String>> get() = _searchParam
    private val _searchChannelParam: MutableLiveData<HashMap<String, String>> = MutableLiveData()
    val searchChannelParam: LiveData<HashMap<String, String>> get() = _searchChannelParam
    private val _searchResult: MutableLiveData<List<MyVideo>> = MutableLiveData()
    val searchResult: LiveData<List<MyVideo>> get() = _searchResult
    private val _searchChannelResult: MutableLiveData<List<MyChannelItems>> = MutableLiveData()
    val searchChannelResult: LiveData<List<MyChannelItems>> get() = _searchChannelResult
    private val _filterVideo: MutableLiveData<List<MyVideo>> = MutableLiveData()
    val filterVideo: LiveData<List<MyVideo>> get() = _filterVideo
    private val channelIds = StringBuilder()
    private val authKey = BuildConfig.youtube_api_key

    fun setUpVideoParameter() {
        channelIds.clear()
        _searchParam.value = hashMapOf(
            "key" to authKey,
            "part" to "snippet,statistics,contentDetails",
            "chart" to "mostPopular",
            "maxResults" to "49",
            "regionCode" to "kr",
            "videoCategoryId" to "17"
        )
    }

    fun communicateNetWork(param: HashMap<String, String>) {
        viewModelScope.launch() {
            val responseData = NetWorkClient.youtubeNetWork.getYoutubeVideo(param)
            val searchItems = responseData.items

            if (searchItems.isNotEmpty()) {
                for (item in searchItems) {
                    val responseChannelData = NetWorkClient.youtubeNetWork.channelByCategory(hashMapOf(
                        "key" to authKey,
                        "part" to "snippet",
                        "id" to item.snippet.channelId
                    ))
                    val videoItem = MyVideo.MyVideoItems(
                        videoUri = item.id,
                        title = item.snippet.title,
                        thumbnail = item.snippet.thumbnails.default.url,
                        content = item.snippet.description,
                        isLike = false,
                        views = item.statistics.viewCount.toLong(),
                        tags = item.snippet.tags,
                        channelTitle = item.snippet.channelTitle,
                        publishedAt = item.snippet.publishedAt,
                        channelImage = responseChannelData.items[0].snippet.thumbnails.default.url
                    )
                    channelIds.append(item.snippet.channelId).append(",")
                    videoList.add(videoItem)

                }
                Log.d("channelIds" , "채널아이디 : ${channelIds}")
                _searchResult.value = videoList
                setUpChannelParameter()
            } else {
                // 데이터가 없을 경우에 대한 처리
            }
        }
    }
    private fun setUpChannelParameter() {
        _searchChannelParam.value = hashMapOf(
            "key" to authKey,
            "part" to "snippet",
            "id" to channelIds.toString()
        )
    }
    fun communicateChannelNetWork(param: HashMap<String, String>) {
        viewModelScope.launch() {
            val responseData = NetWorkClient.youtubeNetWork.channelByCategory(param)
            val channelItems = responseData.items
            if(channelItems.isNotEmpty()){
                channelList.clear()
                for(item in channelItems){
                    channelList.add(
                        MyChannelItems(
                            item.id,
                            item.snippet.thumbnails.medium.url,
                            item.snippet.title,
                        )
                    )
                }
                _searchChannelResult.value = channelList
            }
        }
    }

    fun filterVideoList(selectedTag: String?) {
        if(selectedTag.isNullOrEmpty()){
            _filterVideo.value = videoList
        } else{
            _filterVideo.value = videoList.filter { myVideoItems ->
                myVideoItems.tags?.contains(selectedTag) == true
            }
        }
    }
}