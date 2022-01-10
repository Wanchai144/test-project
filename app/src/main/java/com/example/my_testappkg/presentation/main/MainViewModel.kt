package com.example.my_testappkg.presentation.main

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.example.my_testappkg.data.model.Weather
import com.example.my_testappkg.domain.usecase.WeatherDetailUsecase
import com.example.my_testappkg.extention.collectSafe
import com.example.my_testappkg.extention.launchSafe
import com.example.my_testappkg.presentation.base.ScopedViewModel
import com.example.my_testappkg.presentation.base.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


@HiltViewModel
class MainViewModel  @Inject constructor(
    private val weather: WeatherDetailUsecase
) : ScopedViewModel() {

    val description = ObservableField("")

    val accuntInfo = MutableLiveData<Weather>()

    val isLoading = MutableLiveData<Int>()

    val showErrorPage = MutableLiveData<Unit>()

    var nextPageKey: String? = null

    fun onLoadDataWeatherInfo() = accuntInfo

    fun onErrorDataWeatherInfo() = showErrorPage

    fun getWeatherDetail(city:String) {
        launchSafe {
            weather.execute(city = city)
                .flowOn(Dispatchers.IO)
                .catch {
                    showErrorPage.value = Unit
                }
                .collectSafe { result ->
                    if (result is Success) {
                        accuntInfo.value = result.data
                    }
                }

        }
    }
}

