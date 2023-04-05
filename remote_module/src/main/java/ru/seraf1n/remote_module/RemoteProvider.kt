package ru.seraf1n.remote_module

interface RemoteProvider {
    fun provideRemote(): TmdbApi
}