package br.com.jwar.triviachallenge.data.di

import br.com.jwar.triviachallenge.data.datasources.remote.RemoteDataSourceAdapter
import br.com.jwar.triviachallenge.data.datasources.remote.RemoteDataSourceStrategy
import br.com.jwar.triviachallenge.data.datasources.remote.fake.FakeRemoteDataSource
import br.com.jwar.triviachallenge.data.datasources.remote.trivia.TRIVIA_API_BASE_URL
import br.com.jwar.triviachallenge.data.datasources.remote.trivia.TriviaApi
import br.com.jwar.triviachallenge.data.datasources.remote.trivia.TriviaRemoteDataSourceAdapter
import br.com.jwar.triviachallenge.data.repositories.ActivityRepositoryImpl
import br.com.jwar.triviachallenge.data.repositories.UnitRepositoryImpl
import br.com.jwar.triviachallenge.data.services.translator.MLKitTranslatorService
import br.com.jwar.triviachallenge.data.services.translator.TranslatorService
import br.com.jwar.triviachallenge.data.utils.HtmlStringAdapter
import br.com.jwar.triviachallenge.domain.repositories.ActivityRepository
import br.com.jwar.triviachallenge.domain.repositories.UnitRepository
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    @Singleton
    fun provideConvertFactory() : Converter.Factory =
        MoshiConverterFactory.create(
            Moshi.Builder()
                .add(HtmlStringAdapter())
                .build()
        )

    @Provides
    @Singleton
    fun provideTriviaApi(
        convertFactory: Converter.Factory
    ): TriviaApi =
        Retrofit.Builder()
            .baseUrl(TRIVIA_API_BASE_URL)
            .addConverterFactory(convertFactory)
            .build()
            .create(TriviaApi::class.java)

    @Provides
    fun provideRemoteDataSourceStrategy(
        remoteDataSourceStrategy: FakeRemoteDataSource
    ): RemoteDataSourceStrategy = remoteDataSourceStrategy

    @Provides
    fun provideRemoteDataSourceAdapter(
        remoteDataSourceAdapter: TriviaRemoteDataSourceAdapter
    ): RemoteDataSourceAdapter = remoteDataSourceAdapter
}