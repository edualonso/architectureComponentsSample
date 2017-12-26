//package com.example.edu.myapplication.api
//
//import okhttp3.mockwebserver.MockResponse
//import okhttp3.mockwebserver.MockWebServer
//import org.junit.Before
//import org.junit.Test
//
///**
// * Created by edu on 26/12/2017.
// */
//class WeatherApiClientTest {
//
//    lateinit var mockWebServer: MockWebServer
//
//    @Before
//    fun setUp() {
//        mockWebServer = MockWebServer()
//    }
//
//    @Test
//    fun searchForLocation() {
//        val mockResopnse = MockResponse()
//                .setBody("")
//        mockWebServer.enqueue()
//        mockWebServer.start()
//    }
//
//    @Test
//    fun getCurrentWeather() {
//    }
//
//}