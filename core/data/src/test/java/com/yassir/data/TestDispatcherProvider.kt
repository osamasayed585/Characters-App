package com.yassir.data

import com.yassir.common.di.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class TestDispatcherProvider : DispatcherProvider {
    override val main: CoroutineDispatcher = UnconfinedTestDispatcher()
    override val io: CoroutineDispatcher = UnconfinedTestDispatcher()
    override val default: CoroutineDispatcher = UnconfinedTestDispatcher()
}