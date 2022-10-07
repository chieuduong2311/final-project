package com.student.tkpmnc.finalproject.helper;

import com.student.tkpmnc.finalproject.api.model.Place;
import com.student.tkpmnc.finalproject.entity.RawPlace;
import com.student.tkpmnc.finalproject.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlaceHelper {
    @Autowired
    PlaceRepository placeRepository;

    public void savePlaceIfNotExisted(Place place) {
        if (placeRepository.findFirstByPlaceId(place.getPlaceId()).isEmpty()) {
            RawPlace record = RawPlace.builder()
                    .placeId(place.getPlaceId())
                    .lat(place.getLat())
                    .lng(place.getLng())
                    .fullAddressInString(place.getFullAddressInString())
                    .isDeleted(false)
                    .placeName(place.getPlaceName())
                    .build();
            placeRepository.save(record);
        }
    }
}
