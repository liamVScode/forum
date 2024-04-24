package com.example.foruminforexchange.mapper;

import com.example.foruminforexchange.dto.FacebookUser;
import com.example.foruminforexchange.dto.GoogleUser;
import com.example.foruminforexchange.dto.UserDto;
import com.example.foruminforexchange.model.User;
import net.minidev.json.JSONObject;

public class UserMapper {
    private FacebookUser facebookUser;

    public static FacebookUser convertToFacebookUser(JSONObject jsonObject){
        String email = (String) jsonObject.get("email");
        String firstName = (String) jsonObject.get("firstName");
        String lastName = (String) jsonObject.get("lastName");
        return new FacebookUser(email, firstName, lastName);
    }

    public static GoogleUser convertToGoogleUser(JSONObject jsonObject){
        String email = (String) jsonObject.get("email");
        String firstName = (String) jsonObject.get("firstName");
        String lastName = (String) jsonObject.get("lastName");
        return new GoogleUser(email, firstName, lastName);
    }

    public static User convertToUser(UserDto userDto){
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setLocation(userDto.getLocation());
        return user;
    }

    public static UserDto convertToUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getUserId());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setLocation(user.getLocation());
        return userDto;
    }
}
