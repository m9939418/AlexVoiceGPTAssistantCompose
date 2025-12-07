package com.alex.yang.alexvoicegptassistantcompose.core.utils.network

import okhttp3.Interceptor

/**
 * Created by AlexYang on 2025/12/7.
 *
 *
 */
class OpenAIAuthInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain) =
        chain.proceed(
            chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer $apiKey")
                .build()
        )
}