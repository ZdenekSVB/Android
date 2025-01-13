package cz.pef.project.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitModuleTest {

    @Test
    fun `test provideClient creates OkHttpClient with correct configuration`() {
        val client = RetrofitModule.provideClient()

        assertNotNull(client)
        assertEquals(20, client.dispatcher.maxRequests)
        assertEquals(20, client.connectTimeoutMillis.toLong() / 1000)
    }

    @Test
    fun `test provideMoshi creates Moshi instance with KotlinJsonAdapterFactory`() {
        val moshi = RetrofitModule.provideMoshi()

        assertNotNull(moshi)
        assertEquals(Moshi::class.java, moshi::class.java)
    }

    @Test
    fun `test provideRetrofit creates Retrofit with correct configuration`() {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val client = OkHttpClient.Builder().build()

        val retrofit = RetrofitModule.provideRetrofit(moshi, client)

        assertNotNull(retrofit)
        assertEquals(Retrofit.Builder::class.java, retrofit.newBuilder()::class.java)
    }
}
