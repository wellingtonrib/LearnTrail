package br.com.jwar.triviachallenge.data.di

import br.com.jwar.triviachallenge.data.datasources.UnitsDataSource
import br.com.jwar.triviachallenge.data.datasources.UnitsDataSourceImpl
import br.com.jwar.triviachallenge.data.datasources.ActivityDataSource
import br.com.jwar.triviachallenge.data.datasources.ActivityDataSourceImpl
import br.com.jwar.triviachallenge.data.mappers.UnitDataToDomainMapper
import br.com.jwar.triviachallenge.data.mappers.UnitDataToDomainMapperImpl
import br.com.jwar.triviachallenge.data.mappers.ActivityDataToDomainMapper
import br.com.jwar.triviachallenge.data.mappers.ActivityDataToDomainMapperImpl
import br.com.jwar.triviachallenge.data.repositories.UnitRepositoryImpl
import br.com.jwar.triviachallenge.data.repositories.ActivityRepositoryImpl
import br.com.jwar.triviachallenge.data.services.ActivityService
import br.com.jwar.triviachallenge.data.services.translator.TranslatorService
import br.com.jwar.triviachallenge.data.services.translator.TranslatorServiceImpl
import br.com.jwar.triviachallenge.data.util.HtmlStringAdapter
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
    ): ActivityService =
        Retrofit.Builder()
            .baseUrl("https://opentdb.com/")
            .addConverterFactory(convertFactory)
            .build()
            .create(ActivityService::class.java)

    @Provides
    fun providesActivityDataSource(
        activityService: ActivityService,
    ): ActivityDataSource = ActivityDataSourceImpl(activityService)

    @Provides
    @Singleton
    fun providesActivityDataToDomainMapper(
        translatorService: TranslatorService,
    ): ActivityDataToDomainMapper = ActivityDataToDomainMapperImpl(translatorService)

    @Provides
    @Singleton
    fun providesActivityRepository(
        activityDataSource: ActivityDataSource,
        activityDataToDomainMapper: ActivityDataToDomainMapper,
    ): ActivityRepository = ActivityRepositoryImpl(activityDataSource, activityDataToDomainMapper)

    @Provides
    @Singleton
    fun providesUnitDataSource(): UnitsDataSource = UnitsDataSourceImpl()

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