package com.example.app.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.app.domain.Reservation;

@Mapper
public interface ReservationMapper {
	void insertReservation(Reservation reservation);
}
