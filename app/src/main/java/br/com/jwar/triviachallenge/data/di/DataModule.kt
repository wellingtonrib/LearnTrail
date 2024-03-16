package br.com.jwar.triviachallenge.data.di

import br.com.jwar.triviachallenge.data.datasources.trivia.TriviaRemoteDataSource
import br.com.jwar.triviachallenge.data.datasources.trivia.TriviaRemoteDataSourceImpl
import br.com.jwar.triviachallenge.data.mappers.UnitDataToDomainMapper
import br.com.jwar.triviachallenge.data.mappers.UnitDataToDomainMapperImpl
import br.com.jwar.triviachallenge.data.mappers.ActivityDataToDomainMapper
import br.com.jwar.triviachallenge.data.mappers.ActivityDataToDomainMapperImpl
import br.com.jwar.triviachallenge.data.repositories.UnitRepositoryImpl
import br.com.jwar.triviachallenge.data.repositories.ActivityRepositoryImpl
import br.com.jwar.triviachallenge.data.datasources.trivia.TriviaApi
import br.com.jwar.triviachallenge.data.services.translator.TranslatorService
import br.com.jwar.triviachallenge.data.services.translator.TranslatorServiceImpl
import br.com.jwar.triviachallenge.data.utils.HtmlStringAdapter
import br.com.jwar.triviachallenge.domain.repositories.UnitRepository
import br.com.jwar.triviachallenge.domain.repositories.ActivityRepository
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun providesConvertFactory() : Converter.Factory =
        MoshiConverterFactory.create(
            Moshi.Builder()
                .add(HtmlStringAdapter())
                .build()
        )

    @Provides
    @Singleton
    fun providesActivityService(
        convertFactory: Converter.Factory
    ): TriviaApi =
        Retrofit.Builder()
            .baseUrl("https://opentdb.com/")
            .addConverterFactory(convertFactory)
            .build()
            .create(TriviaApi::class.java)

    @Provides
    fun providesActivityDataSource(
        triviaApi: TriviaApi,
    ): TriviaRemoteDataSource = TriviaRemoteDataSourceImpl(triviaApi)

    @Provides
    @Singleton
    fun providesActivityDataToDomainMapper(
        translatorService: TranslatorService,
    ): ActivityDataToDomainMapper = ActivityDataToDomainMapperImpl(translatorService)

    @Provides
    @Singleton
    fun providesActivityRepository(
        triviaRemoteDataSource: TriviaRemoteDataSource,
        activityDataToDomainMapper: ActivityDataToDomainMapper,
    ): ActivityRepository = ActivityRepositoryImpl(triviaRemoteDataSource, activityDataToDomainMapper)

    @Provides
    @Singleton
    fun providesUnitDataSource(): UnitsDataSource = TriviaCategoriesDataSource()

    @Provides
    @Singleton
    fun providesUnitDataToDomainMapper(
        translatorService: TranslatorService
    ): UnitDataToDomainMapper = UnitDataToDomainMapperImpl(translatorService)

    @Provides
    fun providesUnitRepository(
        unitsDataSource: UnitsDataSource,
        unitDataToDomainMapper: UnitDataToDomainMapper,
    ): UnitRepository = UnitRepositoryImpl(unitsDataSource, unitDataToDomainMapper)

    @Provides
    @Singleton
    fun providesTranslatorService(): TranslatorService = TranslatorServiceImpl()
}