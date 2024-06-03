package com.example.foruminforexchange.mapper;

import com.example.foruminforexchange.dto.ActivityDto;
import com.example.foruminforexchange.model.Activity;

public class ActivityMapper {
    public static ActivityDto convertToActivityDto(Activity activity){
        ActivityDto activityDto = new ActivityDto();
        activityDto.setType(activity.getType());
        activityDto.setContent(activity.getContent());
        activityDto.setCreatedAt(activity.getCreatedAt());
        activityDto.setLink(activity.getLink());
        return activityDto;
    }
}
