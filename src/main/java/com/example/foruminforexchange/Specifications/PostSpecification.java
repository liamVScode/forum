package com.example.foruminforexchange.Specifications;

import com.example.foruminforexchange.model.Comment;
import com.example.foruminforexchange.model.Post;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Date;

public class PostSpecification {

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

    public static Specification<Post> hasPoll(String postType) {
        return (root, query, criteriaBuilder) -> {
            Boolean hasPoll = null;
            if (postType != null && !postType.isEmpty()) {
                if ("Discussion".equals(postType)) {
                    hasPoll = false;
                } else if ("Poll".equals(postType)) {
                    hasPoll = true;
                }
            }
            //khong co poll, khong truy van
            if (hasPoll == null) {
                return criteriaBuilder.conjunction();
            } else if (hasPoll) {
                return criteriaBuilder.isNotNull(root.get("poll"));
            } else {
                return criteriaBuilder.isNull(root.get("poll"));
            }
        };
    }



}
