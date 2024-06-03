package com.example.foruminforexchange.mapper;

import com.example.foruminforexchange.dto.FacebookUser;
import com.example.foruminforexchange.dto.GoogleUser;
import com.example.foruminforexchange.dto.UserDto;
import com.example.foruminforexchange.model.User;
import com.example.foruminforexchange.repository.CommentRepo;
import com.example.foruminforexchange.repository.LikeRepo;
import com.example.foruminforexchange.repository.PostRepo;
import com.example.foruminforexchange.service.StatisticsService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private LikeRepo likeRepo;
    private CommentRepo commentRepo;
    private PostRepo postRepo;

    @Autowired
    public UserMapper(LikeRepo likeRepo, CommentRepo commentRepo, PostRepo postRepo) {
        this.likeRepo = likeRepo;
        this.commentRepo = commentRepo;
        this.postRepo = postRepo;
    }



    public  FacebookUser convertToFacebookUser(JSONObject jsonObject){
        FacebookUser facebookUser = new FacebookUser();
        facebookUser.setFacebookId((String) jsonObject.get("id"));
        facebookUser.setEmail((String) jsonObject.get("email"));
        facebookUser.setFirstName((String) jsonObject.get("first_name"));
        facebookUser.setLastName((String) jsonObject.get("last_name"));
        facebookUser.setAvatar((String) jsonObject.get("picture"));
        return facebookUser;
    }

    public  GoogleUser convertToGoogleUser(JSONObject jsonObject){
        String email = (String) jsonObject.get("email");
        String firstName = (String) jsonObject.get("firstName");
        String lastName = (String) jsonObject.get("lastName");
        return new GoogleUser(email, firstName, lastName);
    }

    public  User convertToUser(UserDto userDto){
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setLocation(userDto.getLocation());
        return user;
    }

    public UserDto convertToUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getUserId());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setLocation(user.getLocation());
        userDto.setAvatar(user.getAvatar());
        userDto.setDateOfBirth(user.getDateOfBirth());
        userDto.setAbout(user.getAbout());
        userDto.setWebsite(user.getWebsite());
        userDto.setFacebook(user.getFacebook());
        userDto.setTwitter(user.getTwitter());
        userDto.setSkype(user.getSkype());
        userDto.setRole(user.getRole());
        userDto.setStatus(user.getStatus());
        userDto.setIsLocked(user.getLocked());
        if (user.getLockedBy() != null) {
            userDto.setLockedBy(convertToUserDto(user.getLockedBy()));
        } else userDto.setLockedBy(null);
        userDto.setCreateAt(user.getCreateAt());
        userDto.setNumberOfComment(commentRepo.countAllByUserUserId(user.getUserId()));
        userDto.setNumberOfLike(likeRepo.countAllByUserUserId(user.getUserId()));
        userDto.setNumberOfPost(postRepo.countAllByUserUserId(user.getUserId()));
        return userDto;
    }
}
