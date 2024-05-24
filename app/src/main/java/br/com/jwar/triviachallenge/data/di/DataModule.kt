package br.com.jwar.triviachallenge.data.di

import android.content.Context
import androidx.room.Room
import br.com.jwar.triviachallenge.data.datasources.local.database.LocalDataSource
import br.com.jwar.triviachallenge.data.datasources.local.database.room.APP_DATABASE_NAME
import br.com.jwar.triviachallenge.data.datasources.local.database.room.RoomAppDatabase
import br.com.jwar.triviachallenge.data.datasources.local.database.room.RoomLocalDataSource
import br.com.jwar.triviachallenge.data.datasources.local.preferences.Preferences
import br.com.jwar.triviachallenge.data.datasources.local.preferences.datastore.DataStorePreferences
import br.com.jwar.triviachallenge.data.datasources.remote.RemoteDataSource
import br.com.jwar.triviachallenge.data.datasources.remote.opentdb.OPEN_TDB_API_HOST
import br.com.jwar.triviachallenge.data.datasources.remote.opentdb.OpenTDBApi
import br.com.jwar.triviachallenge.data.datasources.remote.sample.SampleRemoteDataSource
import br.com.jwar.triviachallenge.data.repositories.ActivityRepositoryImpl
import br.com.jwar.triviachallenge.data.repositories.UnitRepositoryImpl
import br.com.jwar.triviachallenge.data.repositories.UserRepositoryImpl
import br.com.jwar.triviachallenge.data.services.translator.MLKitTranslatorService
import br.com.jwar.triviachallenge.data.services.translator.TranslatorService
import br.com.jwar.triviachallenge.data.utils.HtmlStringAdapter
import br.com.jwar.triviachallenge.domain.repositories.ActivityRepository
import br.com.jwar.triviachallenge.domain.repositories.UnitRepository
import br.com.jwar.triviachallenge.domain.repositories.UserRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideTranslatorService(): TranslatorService = MLKitTranslatorService()

    @Provides
    fun provideUnitRepository(
        unitRepository: UnitRepositoryImpl
    ): UnitRepository = unitRepository

    @Provides
    fun provideActivityRepository(
        activityRepository: ActivityRepositoryImpl
    ): ActivityRepository = activityRepository

    @Provides
    fun provideUserRepository(
        userRepository: UserRepositoryImpl
    ): UserRepository = userRepository

    @Provides
    @Singleton
    fun provideConvertFactory() : Converter.Factory =
        MoshiConverterFactory.create(
            Moshi.Builder()
                .add(HtmlStringAdapter())
                .add(KotlinJsonAdapterFactory())
                .build()
        )

    @Provides
    @Singleton
    fun provideOpenTDBApi(
        convertFactory: Converter.Factory
    ): OpenTDBApi =
        Retrofit.Builder()
            .baseUrl(OPEN_TDB_API_HOST)
            .addConverterFactory(convertFactory)
            .build()
            .create(OpenTDBApi::class.java)


    @Provides
    fun provideSampleRemoteDataSource(
        @ApplicationContext context: Context
    ) = SampleRemoteDataSource(context)

    @Provides
    fun provideDefaultRemoteDataSource(
        remoteDataSourceStrategy: SampleRemoteDataSource
    ): RemoteDataSource = remoteDataSourceStrategy

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context.applicationContext,
        RoomAppDatabase::class.java,
        APP_DATABASE_NAME
    ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideUnitDao(database: RoomAppDatabase) = database.unitDao()

    @Provides
    fun provideActivityDao(database: RoomAppDatabase) = database.activityDao()

    @Provides
    fun provideQuestionDao(database: RoomAppDatabase) = database.questionDao()

    @Provides
    fun provideDefaultLocalDataSource(
        localDataSourceStrategy: RoomLocalDataSource
    ) : LocalDataSource = localDataSourceStrategy

    @Provides
    fun provideDataStorePreferences(
        @ApplicationContext context: Context
    ) = DataStorePreferences(context)

    @Provides
    fun provideDefaultPreferences(
        preferences: DataStorePreferences
    ) : Preferences = preferences
}