package ru.seraf1n.remote_module

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [RemoteModule::class]
)
interface RemoteComponent : RemoteProvider