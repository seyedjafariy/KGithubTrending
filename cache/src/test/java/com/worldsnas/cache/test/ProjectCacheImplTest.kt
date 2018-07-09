package com.worldsnas.cache.test

import com.worldsnas.cache.ProjectsCacheImpl
import com.worldsnas.cache.mapper.CachedProjectMapper
import com.worldsnas.cache.model.CachedProject
import com.worldsnas.cache.model.Config
import com.worldsnas.cache.model.MyObjectBox
import com.worldsnas.cache.test.factory.DataFactory
import com.worldsnas.cache.test.factory.ProjectDataFactory
import com.worldsnas.data.model.ProjectEntity
import io.objectbox.BoxStore
import io.objectbox.DebugFlags
import org.junit.After
import org.junit.Before
import org.junit.ComparisonFailure
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.File
import kotlin.test.assertEquals

@RunWith(JUnit4::class)
class ProjectCacheImplTest {
    private val TEST_DIRECTORY = File("objectbox-example/test-db")
    lateinit var store: BoxStore

    private val mapper = CachedProjectMapper()
    private lateinit var projectCache: ProjectsCacheImpl

    @Before
    fun setUp() {
        // delete database files before each test to start with a clean database
        BoxStore.deleteAllFiles(TEST_DIRECTORY)
        store = MyObjectBox.builder()
                // add directory flag to change where ObjectBox puts its database files
                .directory(TEST_DIRECTORY)
                // optional: add debug flags for more detailed ObjectBox log output
                .debugFlags(DebugFlags.LOG_QUERIES or DebugFlags.LOG_QUERY_PARAMETERS)
                .build()

        projectCache = ProjectsCacheImpl(store, mapper)
    }

    @Test
    fun clearTablesCompletes() {
        val testObserver = projectCache.clearProjects().test()
        testObserver.assertComplete()
    }

    @Test
    fun clearTablesRemovesAllEntities() {
        projectCache.clearProjects().test()
        assertEquals(0, store.boxFor(CachedProject::class.java).count())
    }

    @Test
    fun saveProjectsCompletes() {
        val entities = listOf(ProjectDataFactory.makeProjectEntity())

        val testObserver = projectCache.saveProjects(entities).test()

        testObserver.assertComplete()
    }

    @Test
    fun saveProjectsSavesData() {
        val entities = listOf(ProjectDataFactory.makeProjectEntity(), ProjectDataFactory.makeProjectEntity(), ProjectDataFactory.makeProjectEntity())

        projectCache.saveProjects(entities).test()

        val retrievedEntities = store.boxFor(CachedProject::class.java).all.map { mapper.mapFromCached(it) }

        assertEquals(entities.size, retrievedEntities.size)

        assertEqualProjectEntityList(entities, retrievedEntities)
    }

    @Test
    fun getProjectsReturnsData() {
        val entities = listOf(ProjectDataFactory.makeProjectEntity(), ProjectDataFactory.makeProjectEntity(), ProjectDataFactory.makeProjectEntity(), ProjectDataFactory.makeProjectEntity())

        projectCache.saveProjects(entities).test()

        val testObserver = projectCache.getProjects().test()

        val retrievedEntities = testObserver.values().get(0)

        assertEquals(entities.size, retrievedEntities.size)

        assertEqualProjectEntityList(entities, retrievedEntities)
    }

    @Test
    fun getProjectsNeverCompletes() {
        val entities = listOf(ProjectDataFactory.makeProjectEntity())

        projectCache.saveProjects(entities).test()

        val testObserver = projectCache.getProjects().test()

        testObserver.assertNotComplete()
    }

    @Test
    fun getBookmarkProjectsReturnsData() {
        val bookmarkEntities = listOf(ProjectDataFactory.makeProjectEntity(isBookmark = true), ProjectDataFactory.makeProjectEntity(true))
        val entities = listOf(ProjectDataFactory.makeProjectEntity(false), ProjectDataFactory.makeProjectEntity(false), bookmarkEntities[0], bookmarkEntities[1])

        projectCache.saveProjects(entities).test()

        val testObserver = projectCache.getBookmarkProjects().test()

        val retrievedEntities = testObserver.values().get(0)

        // wont add all the project removes one per run
        assertEquals(bookmarkEntities.size, retrievedEntities.size)

        assertEqualProjectEntityList(bookmarkEntities, retrievedEntities)
    }

    @Test
    fun getBookmarkProjectsNeverCompletes() {
        val bookmarkEntities = listOf(ProjectDataFactory.makeProjectEntity(true), ProjectDataFactory.makeProjectEntity(true))
        val entities = listOf(ProjectDataFactory.makeProjectEntity(false), ProjectDataFactory.makeProjectEntity(false), bookmarkEntities[0], bookmarkEntities[1])

        projectCache.saveProjects(entities).test()

        val testObserver = projectCache.getBookmarkProjects().test()

        testObserver.assertNotComplete()
    }

    @Test
    fun setProjectAsBookmarkCompletes() {
        val entities = listOf(ProjectDataFactory.makeProjectEntity(false))

        projectCache.saveProjects(entities).test()

        val testObservable = projectCache.setProjectAsBookmark(entities[0].id).test()

        testObservable.assertComplete()
    }

    @Test
    fun setProjectAsNotBookmarkCompletes() {
        val entities = listOf(ProjectDataFactory.makeProjectEntity(false))

        projectCache.saveProjects(entities).test()

        val testObservable = projectCache.setProjectAsNotBootkmark(entities[0].id).test()

        testObservable.assertComplete()
    }

    @Test
    fun areProjectsCached() {
        val entities = listOf(ProjectDataFactory.makeProjectEntity(false))

        projectCache.saveProjects(entities).test()

        val testObservable = projectCache.areProjectsCached().test()

        testObservable.assertValue(true)
    }

    @Test
    fun areProjectsNotCached(){
        val testObservable = projectCache.areProjectsCached().test()

        testObservable.assertValue(false)
    }

    @Test
    fun setLastCacheTimeCompletes(){
        val lastTime = DataFactory.randomLong()

        val testObserver = projectCache.setLastCacheTime(lastTime).test()

        testObserver.assertComplete()
    }

    @Test
    fun setLastCacheTimeStoresData(){
        val lastTime = DataFactory.randomLong()

        projectCache.setLastCacheTime(lastTime).test()

        val configs = store.boxFor(Config::class.java).all

        assertEquals(1, configs.size)

        assertEquals(lastTime, configs[0].lastCacheTime)
    }

    @Test
    fun isProjectCacheExpiredTrue(){
        val testObserver = projectCache.isProjectCacheExpired().test()

        testObserver.assertValue(true)
    }

    @Test
    fun isProjectCacheExpiredNotExpired(){
        projectCache.setLastCacheTime(System.currentTimeMillis()).test()

        val testObserver = projectCache.isProjectCacheExpired().test()

        testObserver.assertValue(false)
    }

    @Test
    fun isProjectCacheExpiredTimeExpired(){
        projectCache.setLastCacheTime(System.currentTimeMillis() - 60 * 10 * 1000).test()

        val testObserver = projectCache.isProjectCacheExpired().test()

        testObserver.assertValue(true)
    }

    @After
    fun tearDown() {
        store.boxFor(CachedProject::class.java).removeAll()
        store.boxFor(Config::class.java).removeAll()
        store.close()
        BoxStore.deleteAllFiles(TEST_DIRECTORY)
    }

    private fun assertEqualProject(entity: ProjectEntity, model: ProjectEntity) {
        assertEquals(entity.id, model.id)
        assertEquals(entity.name, model.name)
        assertEquals(entity.fullName, model.fullName)
        assertEquals(entity.ownerName, model.ownerName)
        assertEquals(entity.ownerAvatar, model.ownerAvatar)
        assertEquals(entity.isBookmarked, model.isBookmarked)
        assertEquals(entity.dateCreated, model.dateCreated)
        assertEquals(entity.starCount, model.starCount)

    }

    private fun assertEqualProjectEntityList(entities: List<ProjectEntity>, retrievedEntities: List<ProjectEntity>) {
        for (i in entities.indices) {
            var isChecked = false

            for (j in retrievedEntities.indices) {
                if (entities[i].id.equals(retrievedEntities[j].id, true)) {
                    isChecked = true
                    assertEqualProject(entities[i], retrievedEntities[j])
                    break
                }
            }

            if (!isChecked) {
                throw ComparisonFailure("entity failed to match", "" + entities[i].id, "")
            }
        }
    }
}