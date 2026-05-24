package com.example.marvelousdreamer.data.remote.mapper

import com.example.marvelousdreamer.data.remote.dto.*
import com.example.marvelousdreamer.domain.model.*

fun HotelDto.toDomain(): Hotel = Hotel(
    id = id, name = name, address = address, rating = rating,
    imageUrl = image_url, rooms = rooms?.map { it.toDomain() } ?: emptyList()
)

fun RoomDto.toDomain(): Room = Room(
    id = id, roomType = room_type, price = price,
    images = images ?: emptyList()
)

fun ReservationDto.toDomain(): Reservation = Reservation(
    id = id, hotelId = hotel_id, roomId = room_id,
    startDate = start_date, endDate = end_date,
    guestName = guest_name, guestEmail = guest_email,
    hotel = hotel?.toDomain() ?: Hotel(id = hotel_id, name = "", address = "", rating = 0, imageUrl = ""),
    room = room?.toDomain() ?: Room(id = room_id, roomType = "", price = 0f)
)

fun ReserveRequest.toDto(): ReserveRequestDto = ReserveRequestDto(
    hotel_id = hotelId, room_id = roomId,
    start_date = startDate, end_date = endDate,
    guest_name = guestName, guest_email = guestEmail
)
