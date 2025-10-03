package com.droidos.navigation.di


import com.droidos.navigation.util.networkMonitor.ConnectivityManagerNetworkMonitor
import com.droidos.navigation.util.networkMonitor.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsNetworkMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor,
    ): NetworkMonitor
}