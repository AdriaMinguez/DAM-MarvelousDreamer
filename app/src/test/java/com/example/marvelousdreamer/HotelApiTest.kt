package com.example.marvelousdreamer

import com.example.marvelousdreamer.data.remote.api.HotelApiService
import com.example.marvelousdreamer.data.remote.dto.ReserveRequestDto
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HotelApiTest {

    private lateinit var server: MockWebServer
    private lateinit var api: HotelApiService

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
        api = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HotelApiService::class.java)
    }

    @After
    fun tearDown() { server.shutdown() }

    @Test
    fun `getAvailability returns hotels`() = runBlocking {
        server.enqueue(MockResponse().setBody("""
            {
                "available_hotels": [
                    {
                        "id": "h1",
                        "name": "Grand Hotel",
                        "address": "London",
                        "rating": 4,
                        "image_url": "/img/h1.jpg",
                        "rooms": [
                            { "id": "r1", "room_type": "Single", "price": 100.0, "images": ["/img/r1.jpg"] },
                            { "id": "r2", "room_type": "Double", "price": 150.0, "images": [] }
                        ]
                    }
                ]
            }
        """.trimIndent()).setResponseCode(200))

        val result = api.getAvailability("G11", "2026-06-01", "2026-06-05")
        assertEquals(1, result.available_hotels.size)
        assertEquals("Grand Hotel", result.available_hotels[0].name)
        assertEquals(2, result.available_hotels[0].rooms?.size)
    }

    @Test
    fun `getAvailability with city filter`() = runBlocking {
        server.enqueue(MockResponse().setBody("""{"available_hotels":[]}""").setResponseCode(200))
        val result = api.getAvailability("G11", "2026-06-01", "2026-06-05", city = "Paris")
        assertEquals(0, result.available_hotels.size)

        val request = server.takeRequest()
        assertTrue(request.path!!.contains("city=Paris"))
    }

    @Test
    fun `reserveRoom returns reservation`() = runBlocking {
        server.enqueue(MockResponse().setBody("""
            {
                "message": "Reservation created",
                "nights": 3,
                "reservation": {
                    "id": "res_123",
                    "hotel_id": "h1",
                    "room_id": "r1",
                    "start_date": "2026-06-01",
                    "end_date": "2026-06-04",
                    "guest_name": "Test",
                    "guest_email": "test@test.com",
                    "hotel": { "id": "h1", "name": "Grand Hotel", "address": "London", "rating": 4, "image_url": "/img/h1.jpg" },
                    "room": { "id": "r1", "room_type": "Single", "price": 100.0, "images": [] }
                }
            }
        """.trimIndent()).setResponseCode(200))

        val result = api.reserveRoom("G11", ReserveRequestDto(
            hotel_id = "h1", room_id = "r1",
            start_date = "2026-06-01", end_date = "2026-06-04",
            guest_name = "Test", guest_email = "test@test.com"
        ))
        assertEquals("res_123", result.reservation.id)
        assertEquals(3, result.nights)
    }

    @Test
    fun `getGroupReservations returns list`() = runBlocking {
        server.enqueue(MockResponse().setBody("""
            {
                "reservations": [
                    {
                        "id": "res_1", "hotel_id": "h1", "room_id": "r1",
                        "start_date": "2026-06-01", "end_date": "2026-06-05",
                        "guest_name": "A", "guest_email": "a@a.com",
                        "hotel": { "id": "h1", "name": "H1", "address": "London", "rating": 3, "image_url": "/h1.jpg" },
                        "room": { "id": "r1", "room_type": "Single", "price": 80.0, "images": [] }
                    }
                ]
            }
        """.trimIndent()).setResponseCode(200))

        val result = api.getGroupReservations("G11")
        assertEquals(1, result.reservations.size)
        assertEquals("res_1", result.reservations[0].id)
    }

    @Test
    fun `deleteReservationById returns deleted reservation`() = runBlocking {
        server.enqueue(MockResponse().setBody("""
            {
                "id": "res_1", "hotel_id": "h1", "room_id": "r1",
                "start_date": "2026-06-01", "end_date": "2026-06-05",
                "guest_name": "A", "guest_email": "a@a.com",
                "hotel": { "id": "h1", "name": "H1", "address": "London", "rating": 3, "image_url": "/h1.jpg" },
                "room": { "id": "r1", "room_type": "Single", "price": 80.0, "images": [] }
            }
        """.trimIndent()).setResponseCode(200))

        val result = api.deleteReservationById("res_1")
        assertEquals("res_1", result.id)

        val request = server.takeRequest()
        assertEquals("DELETE", request.method)
    }

    @Test
    fun `availability request has correct path`() = runBlocking {
        server.enqueue(MockResponse().setBody("""{"available_hotels":[]}""").setResponseCode(200))
        api.getAvailability("G11", "2026-06-01", "2026-06-05", city = "Barcelona")

        val request = server.takeRequest()
        assertTrue(request.path!!.startsWith("/hotels/G11/availability"))
        assertTrue(request.path!!.contains("start_date=2026-06-01"))
        assertTrue(request.path!!.contains("end_date=2026-06-05"))
        assertTrue(request.path!!.contains("city=Barcelona"))
    }
}
