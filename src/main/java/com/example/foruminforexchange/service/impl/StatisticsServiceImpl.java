package com.example.foruminforexchange.service.impl;

import com.example.foruminforexchange.Exception.AppException;
import com.example.foruminforexchange.Exception.ErrorCode;
import com.example.foruminforexchange.dto.UserDto;
import com.example.foruminforexchange.mapper.UserMapper;
import com.example.foruminforexchange.model.Role;
import com.example.foruminforexchange.model.Status;
import com.example.foruminforexchange.model.User;
import com.example.foruminforexchange.repository.CommentRepo;
import com.example.foruminforexchange.repository.LikeRepo;
import com.example.foruminforexchange.repository.PostRepo;
import com.example.foruminforexchange.repository.UserRepo;
import com.example.foruminforexchange.service.BaseRedisService;
import com.example.foruminforexchange.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final UserRepo userRepo;
    private final CommentRepo commentRepo;
    private final PostRepo postRepo;
    private final LikeRepo likeRepo;
    @Autowired
    private UserMapper userMapper;
    private final BaseRedisService baseRedisService;
    @Override
    public Long getNumberOfOnlineUser() {
        Long numberOfOnlineUser = userRepo.countByStatusAndRole(Status.ONLINE, Role.USER);
        return numberOfOnlineUser;
    }

    @Override
    public List<UserDto> getListOnlineAdmin() {
        List<User> users = userRepo.findAllByRole(Status.ONLINE, Role.ADMIN);
        if(users == null) throw new AppException(ErrorCode.USER_NOT_FOUND);
        List<UserDto> userDtos = users.stream().map(user -> userMapper.convertToUserDto(user)).collect(Collectors.toList());
        return userDtos;
    }

    @Override
    public Long getNumberOfPostPerDay() {
        String key = "average_posts_per_day";
        if (baseRedisService.hashExits(key, "average")) {
            Integer average = (Integer) baseRedisService.hashGet(key, "average");
            if (average != null && average == 0) {
                updateAveragePostsPerDay();
                average = (Integer) baseRedisService.hashGet(key, "average");
                long roundedAverage = (long) Math.ceil(average);
                return roundedAverage;
            }
            if (average != null && average != 0) {
                long roundedAverage = (long) Math.ceil(average);
                return roundedAverage;
            } else {
                return 0L;
            }
        } else {
            updateAveragePostsPerDay();
            Integer average = (Integer) baseRedisService.hashGet(key, "average");
            if (average != null && average != 0) {
                long roundedAverage = (long) Math.ceil(average);
                return roundedAverage;
            } else {
                return 0L;
            }
        }
    }

    @Override
    public Long getNumberOfPostPerMonth() {
        String key = "average_posts_per_month";
        if (baseRedisService.hashExits(key, "average")) {
            Integer average = (Integer) baseRedisService.hashGet(key, "average");
            if (average != null && average == 0) {
                updateAveragePostsPerMonth();
                average = (Integer) baseRedisService.hashGet(key, "average");
                long roundedAverage = (long) Math.ceil(average);
                return roundedAverage;
            }
            if (average != null) {
                long roundedAverage = (long) Math.ceil(average);
                return roundedAverage;
            } else {
                return 0L;
            }
        } else {
            updateAveragePostsPerMonth();
            Integer average = (Integer) baseRedisService.hashGet(key, "average");
            if (average != null) {
                long roundedAverage = (long) Math.ceil(average);
                return roundedAverage;
            } else {
                return 0L;
            }
        }
    }


    @Scheduled(cron = "0 0 5 * * *") // Hàng ngày vào lúc 5h sáng
    public void updateAveragePostsPerDay() {
        LocalDate yesterday = LocalDate.now().minusDays(30);
        LocalDateTime startDateTime = yesterday.atStartOfDay();
        LocalDateTime endDateTime = yesterday.plusDays(30).atStartOfDay();

        Long postsYesterday = postRepo.countByCreateAtBetween(startDateTime, endDateTime);

        baseRedisService.hashSet("posts_per_day", yesterday.toString(), postsYesterday);

            List<Object> dailyCounts = baseRedisService.hashGetByFieldPrefix("posts_per_day", "");
            Double average = dailyCounts.stream()
                    .mapToLong(count -> ((Integer) count).longValue())
                    .average()
                    .orElse(0.0);

            Long roundedAverage = (long) Math.ceil(average);

            baseRedisService.hashSet("average_posts_per_day", "average", roundedAverage);
            baseRedisService.setTimeToLive("average_posts_per_day", 1L);
    }

    @Scheduled(cron = "0 0 5 1 * *") // Hàng tháng vào lúc 5h sáng ngày 1
    public void updateAveragePostsPerMonth() {
        YearMonth lastMonth = YearMonth.now().minusMonths(1);
        LocalDateTime startDateTime = lastMonth.atDay(1).atStartOfDay();
        LocalDateTime endDateTime = lastMonth.plusMonths(1).atDay(30).atStartOfDay();

        Long postsLastMonth = postRepo.countByCreateAtBetween(startDateTime, endDateTime);

        baseRedisService.hashSet("posts_per_month", lastMonth.toString(), postsLastMonth);

        List<Object> monthlyCounts = baseRedisService.hashGetByFieldPrefix("posts_per_month", "");
        Double average = monthlyCounts.stream()
                .mapToLong(count -> ((Integer) count).longValue())
                .average()
                .orElse(0.0);
        Long roundedAverage = (long) Math.ceil(average);

        baseRedisService.hashSet("average_posts_per_month", "average", roundedAverage);
        baseRedisService.setTimeToLive("average_posts_per_month", 30L);
    }



    @Override
    public Long getNumberOfPost() {
        return postRepo.countAll();
    }

    @Override
    public Long getNumberOfComment() {
        return commentRepo.countAll();
    }

    @Override
    public Long getNumberOfMember() {
        Long numberOfMember = userRepo.countAll();
        return numberOfMember;
    }

    @Override
    public Long getNumberOfPostByUser(Long userId) {
        return postRepo.countAllByUserUserId(userId);
    }

    @Override
    public Long getNumberOfCommentByUser(Long userId) {
        return commentRepo.countAllByUserUserId(userId);
    }

    @Override
    public Long getNumberOfLikeByUserId(Long userId) {
        return likeRepo.countAllByUserUserId(userId);
    }
}
