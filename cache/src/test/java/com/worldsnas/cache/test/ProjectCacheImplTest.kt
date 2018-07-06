package com.worldsnas.cache.test

import com.worldsnas.cache.model.MyObjectBox
import io.objectbox.BoxStore
import io.objectbox.DebugFlags
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.File

@RunWith(JUnit4::class)
class ProjectCacheImplTest {
    private val TEST_DIRECTORY = File("objectbox-example/test-db")
    lateinit var store : BoxStore

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
    }

    @Test
    fun firstDbTEst() {

    }

    @After
    fun tearDown() {
        store.close()
        BoxStore.deleteAllFiles(TEST_DIRECTORY)
    }
}