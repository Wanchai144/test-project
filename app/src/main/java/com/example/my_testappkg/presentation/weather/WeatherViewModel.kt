package com.example.my_testappkg.presentation.weather

import androidx.lifecycle.MutableLiveData
import com.example.my_testappkg.data.model.WeatherHourly
import com.example.my_testappkg.domain.usecase.WeatherHourlyUsecase
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
class WeatherViewModel @Inject constructor(
    private val weather: WeatherHourlyUsecase
) : ScopedViewModel() {

    val accuntInfo = MutableLiveData<WeatherHourly>()

    val isLoading = MutableLiveData<Int>()

    val showErrorPage = MutableLiveData<Unit>()

    var nextPageKey: String? = null

    fun onLoadDataWeatherHourlyInfo() = accuntInfo

    fun onErrorDataWeatherHourlyInfo() = showErrorPage

    fun getWeatherDetail(lat:Double,lon:Double) {
        launchSafe {
            weather.execute(lat = lat,lon = lon)
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
