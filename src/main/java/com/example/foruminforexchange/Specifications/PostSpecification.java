package com.example.foruminforexchange.Specifications;

import com.example.foruminforexchange.model.Comment;
import com.example.foruminforexchange.model.Post;
import com.example.foruminforexchange.model.Report;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Date;

public class PostSpecification {

    public static Specification<Post> hasCategory(Long categoryId) {
        return (root, query, cb) -> cb.equal(root.get("category").get("categoryId"), categoryId);
    }

    public static Specification<Post> hasPrefix(Long prefixId) {
        return (root, query, cb) -> cb.equal(root.get("prefix").get("prefixId"), prefixId);
    }


    public static Specification<Post> hasKeywordInTitleOrFirstComment(String keyword) {
        return (root, query, cb) -> {
            // truy van comment dau tien cua bai viet
            Subquery<Date> dateSubquery = query.subquery(Date.class);
            Root<Comment> subCommentRoot = dateSubquery.from(Comment.class);
            dateSubquery.select(cb.least(subCommentRoot.<Date>get("createAt")));
            dateSubquery.where(cb.equal(subCommentRoot.get("post"), root));

            // truy van theo title
            Predicate titlePredicate = cb.like(root.get("title"), "%" + keyword + "%");

            // truy van con cho comment dau tien
            Subquery<Comment> commentSubquery = query.subquery(Comment.class);
            Root<Comment> commentRoot = commentSubquery.from(Comment.class);
            commentSubquery.select(commentRoot);
            commentSubquery.where(
                    cb.and(
                            cb.equal(commentRoot.get("createAt"), dateSubquery),
                            cb.equal(commentRoot.get("post"), root),
                            cb.like(commentRoot.get("content"), "%" + keyword + "%")
                    )
            );

            Predicate commentPredicate = cb.exists(commentSubquery);

            return cb.or(titlePredicate, commentPredicate);
        };
    }

    public static Specification<Post> updatedWithinDays(Long days) {
        return (root, query, cb) -> {
            //tu days ngay truoc den hien tai
            LocalDateTime daysAgo = LocalDateTime.now().minusDays(days);

            Predicate updatedWithinRange = cb.greaterThanOrEqualTo(root.get("updateAt"), daysAgo);

            return updatedWithinRange;
        };
    }

    public static Specification<Post> hasPoll(Long postType) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = null;

            if (postType == 0) {
                // Trả về các bài viết không có poll
                predicate = criteriaBuilder.isNull(root.get("poll"));
            } else if (postType == 1) {
                // Trả về các bài viết có poll
                predicate = criteriaBuilder.isNotNull(root.get("poll"));
            }

            return predicate != null ? predicate : criteriaBuilder.conjunction();
        };
    }

    public static Specification<Post> hasReport(Long report) {
        return (root, query, criteriaBuilder) -> {
            if (report == 0) {
                // Trả về các bài viết không có report
                return criteriaBuilder.isEmpty(root.get("report"));
            } else if (report == 1) {
                // Trả về các bài viết có report
                Join<Post, Report> reports = root.join("report", JoinType.LEFT);
                return criteriaBuilder.isNotNull(reports.get("reportId"));
            }
            return criteriaBuilder.conjunction();
        };
    }
}
