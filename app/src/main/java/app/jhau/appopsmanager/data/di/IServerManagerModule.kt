package app.jhau.appopsmanager.data.di

import android.app.Application
import app.jhau.server.IServerManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class IServerManagerModule {

    @Provides
    fun getServerManager(application: Application): IServerManager {
        return IServerManager(application)
    }
}