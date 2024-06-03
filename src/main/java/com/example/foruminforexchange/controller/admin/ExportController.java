package com.example.foruminforexchange.controller.admin;

import com.example.foruminforexchange.dto.PostExportDto;
import com.example.foruminforexchange.dto.UserExportDto;
import com.example.foruminforexchange.service.ExportService;
import com.example.foruminforexchange.service.PostService;
import com.example.foruminforexchange.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/admin/export")
@RequiredArgsConstructor
public class ExportController {

    private final ExportService exportService;
    private final PostService postService;
    private final UserService userService;


    @GetMapping("/posts/excel")
    public void exportPostsToExcel(HttpServletResponse response) throws IOException {
        String[] headers = { "Post ID", "Title", "Create At", "Update At", "View Count", "Like Count", "Comment Count", "Share Count", "Report Count", "User ID", "Prefix ID", "Category ID", "Locked", "Poll ID" };
        byte[] excelBytes = exportService.exportToExcel(
                PageRequest.of(0, 100, Sort.by("createAt").descending()),
                pageable -> postService.getAllPost(pageable),
                headers,
                post -> {
                    PostExportDto exportDto = new PostExportDto();
                    exportDto.setPostId(post.getPostId());
                    exportDto.setTitle(post.getTitle());
                    exportDto.setCreateAt(post.getCreateAt());
                    exportDto.setUpdateAt(post.getUpdateAt());
                    exportDto.setViewCount(post.getViewCount());
                    exportDto.setLikeCount(post.getLikeCount());
                    exportDto.setCommentCount(post.getCommentCount());
                    exportDto.setShareCount(post.getShareCount());
                    exportDto.setReportCount(post.getReportCount());
                    exportDto.setUserId(post.getUser() != null ? post.getUser().getUserId() : null);
                    exportDto.setPrefixId(post.getPrefix() != null ? post.getPrefix().getPrefixId() : null);
                    exportDto.setCategoryId(post.getCategory() != null ? post.getCategory().getCategoryId() : null);
                    exportDto.setLocked(post.getLocked());
                    exportDto.setPollId(post.getPoll() != null ? post.getPoll().getPollId() : null);
                    return new Object[]{ exportDto.getPostId(), exportDto.getTitle(), exportDto.getCreateAt(), exportDto.getUpdateAt(), exportDto.getViewCount(), exportDto.getLikeCount(), exportDto.getCommentCount(), exportDto.getShareCount(), exportDto.getReportCount(), exportDto.getUserId(), exportDto.getPrefixId(), exportDto.getCategoryId(), exportDto.getLocked(), exportDto.getPollId() };
                }
        );

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=posts.xlsx");

        response.getOutputStream().write(excelBytes);
        response.getOutputStream().flush();
    }


    @GetMapping("/posts/pdf")
    public void exportPostsToPdf(HttpServletResponse response) throws IOException {
        String[] headers = { "Post ID", "Title", "Create At", "Update At", "View Count", "Like Count", "Comment Count", "Share Count", "Report Count", "User ID", "Prefix ID", "Category ID", "Locked", "Poll ID" };
        byte[] pdfBytes = exportService.exportToPdf(
                PageRequest.of(0, 100, Sort.by("createAt").descending()),
                pageable -> postService.getAllPost(pageable),
                headers,
                post -> {
                    PostExportDto exportDto = new PostExportDto();
                    exportDto.setPostId(post.getPostId());
                    exportDto.setTitle(post.getTitle());
                    exportDto.setCreateAt(post.getCreateAt());
                    exportDto.setUpdateAt(post.getUpdateAt());
                    exportDto.setViewCount(post.getViewCount());
                    exportDto.setLikeCount(post.getLikeCount());
                    exportDto.setCommentCount(post.getCommentCount());
                    exportDto.setShareCount(post.getShareCount());
                    exportDto.setReportCount(post.getReportCount());
                    exportDto.setUserId(post.getUser() != null ? post.getUser().getUserId() : null);
                    exportDto.setPrefixId(post.getPrefix() != null ? post.getPrefix().getPrefixId() : null);
                    exportDto.setCategoryId(post.getCategory() != null ? post.getCategory().getCategoryId() : null);
                    exportDto.setLocked(post.getLocked());
                    exportDto.setPollId(post.getPoll() != null ? post.getPoll().getPollId() : null);
                    return new Object[]{ exportDto.getPostId(), exportDto.getTitle(), exportDto.getCreateAt(), exportDto.getUpdateAt(), exportDto.getViewCount(), exportDto.getLikeCount(), exportDto.getCommentCount(), exportDto.getShareCount(), exportDto.getReportCount(), exportDto.getUserId(), exportDto.getPrefixId(), exportDto.getCategoryId(), exportDto.getLocked(), exportDto.getPollId() };
                }
        );

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=posts.pdf");

        response.getOutputStream().write(pdfBytes);
        response.getOutputStream().flush();
    }

    @GetMapping("/users/excel")
    public void exportUsersToExcel(HttpServletResponse response) throws IOException {
        String[] headers = { "User ID", "Email", "Avatar", "First Name", "Last Name", "Date of Birth", "Location", "Status", "Is Locked", "Website", "About", "Skype", "Facebook", "Twitter", "Role", "Locked By", "Created At", "Number of Comment", "Number of Like", "Number of Post" };

        byte[] excelBytes = exportService.exportToExcel(
                PageRequest.of(0, 100, Sort.by("userId").descending()),
                pageable -> userService.getAllUser(pageable),
                headers,
                user -> {
                    UserExportDto userExportDto = new UserExportDto();
                    userExportDto.setUserId(user.getUserId());
                    userExportDto.setEmail(user.getEmail());
                    userExportDto.setAvatar(user.getAvatar());
                    userExportDto.setFirstName(user.getFirstName());
                    userExportDto.setLastName(user.getLastName());
                    userExportDto.setDateOfBirth(user.getDateOfBirth());
                    userExportDto.setLocation(user.getLocation());
                    userExportDto.setStatus(user.getStatus());
                    userExportDto.setIsLocked(user.getIsLocked());
                    userExportDto.setWebsite(user.getWebsite());
                    userExportDto.setAbout(user.getAbout());
                    userExportDto.setSkype(user.getSkype());
                    userExportDto.setFacebook(user.getFacebook());
                    userExportDto.setTwitter(user.getTwitter());
                    userExportDto.setRole(user.getRole());
                    userExportDto.setLockedBy(user.getLockedBy() != null ? user.getLockedBy().getUserId() : null);
                    userExportDto.setCreateAt(user.getCreateAt());
                    userExportDto.setNumberOfComment(user.getNumberOfComment());
                    userExportDto.setNumberOfLike(user.getNumberOfLike());
                    userExportDto.setNumberOfPost(user.getNumberOfPost());
                    return new Object[]{ userExportDto.getUserId(), userExportDto.getEmail(), userExportDto.getAvatar(),
                            userExportDto.getFirstName(), userExportDto.getLastName(), user.getDateOfBirth(),
                            userExportDto.getLocation(), userExportDto.getStatus(), userExportDto.getIsLocked(), userExportDto.getWebsite(), userExportDto.getAbout(),
                            userExportDto.getSkype(), userExportDto.getFacebook(), userExportDto.getTwitter(), userExportDto.getRole(),
                            userExportDto.getLockedBy() != null ? userExportDto.getLockedBy() : null, userExportDto.getCreateAt(),
                            userExportDto.getNumberOfComment(), userExportDto.getNumberOfLike(), userExportDto.getNumberOfPost() };
                }
        );

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=users.xlsx");

        response.getOutputStream().write(excelBytes);
        response.getOutputStream().flush();
    }


    @GetMapping("/users/pdf")
    public void exportUsersToPdf(HttpServletResponse response) throws IOException {
        String[] headers = { "User ID", "Email", "Avatar", "First Name", "Last Name", "Date of Birth", "Location", "Status", "Is Locked", "Website", "About", "Skype", "Facebook", "Twitter", "Role", "Locked By", "Created At", "Number of Comment", "Number of Like", "Number of Post" };

        byte[] pdfBytes = exportService.exportToPdf(
                PageRequest.of(0, 100, Sort.by("userId").descending()),
                pageable -> userService.getAllUser(pageable),
                headers,
                user -> {
                    UserExportDto userExportDto = new UserExportDto();
                    userExportDto.setUserId(user.getUserId());
                    userExportDto.setEmail(user.getEmail());
                    userExportDto.setAvatar(user.getAvatar());
                    userExportDto.setFirstName(user.getFirstName());
                    userExportDto.setLastName(user.getLastName());
                    userExportDto.setDateOfBirth(user.getDateOfBirth());
                    userExportDto.setLocation(user.getLocation());
                    userExportDto.setStatus(user.getStatus());
                    userExportDto.setIsLocked(user.getIsLocked());
                    userExportDto.setWebsite(user.getWebsite());
                    userExportDto.setAbout(user.getAbout());
                    userExportDto.setSkype(user.getSkype());
                    userExportDto.setFacebook(user.getFacebook());
                    userExportDto.setTwitter(user.getTwitter());
                    userExportDto.setRole(user.getRole());
                    userExportDto.setLockedBy(user.getLockedBy() != null ? user.getLockedBy().getUserId() : null);
                    userExportDto.setCreateAt(user.getCreateAt());
                    userExportDto.setNumberOfComment(user.getNumberOfComment());
                    userExportDto.setNumberOfLike(user.getNumberOfLike());
                    userExportDto.setNumberOfPost(user.getNumberOfPost());
                    return new Object[]{ userExportDto.getUserId(), userExportDto.getEmail(), userExportDto.getAvatar(),
                            userExportDto.getFirstName(), userExportDto.getLastName(), user.getDateOfBirth(),
                            userExportDto.getLocation(), userExportDto.getStatus(), userExportDto.getIsLocked(), userExportDto.getWebsite(), userExportDto.getAbout(),
                            userExportDto.getSkype(), userExportDto.getFacebook(), userExportDto.getTwitter(), userExportDto.getRole(),
                            userExportDto.getLockedBy() != null ? userExportDto.getLockedBy() : null, userExportDto.getCreateAt(),
                            userExportDto.getNumberOfComment(), userExportDto.getNumberOfLike(), userExportDto.getNumberOfPost() };
                }
        );

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=users.pdf");

        response.getOutputStream().write(pdfBytes);
        response.getOutputStream().flush();
    }

}
