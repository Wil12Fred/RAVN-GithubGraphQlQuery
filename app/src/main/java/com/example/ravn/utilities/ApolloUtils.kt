package com.example.ravn.utilities

import com.apollographql.apollo.ApolloClient
import okhttp3.OkHttpClient

object ApolloUtils {
    public fun setupApollo(): ApolloClient {
        val okHttp = OkHttpClient
            .Builder()
            .addInterceptor({ chain ->
                val original = chain.request()
                val builder = original.newBuilder().method(original.method(),
                    original.body())
                builder.addHeader("Authorization"
                    , "Bearer " + Constants.AUTH_APOLLO)
                chain.proceed(builder.build())
            })
            .build()
        return ApolloClient.builder()
            .serverUrl(Constants.BASE_URL)
            .okHttpClient(okHttp)
            .build()
    }
}
