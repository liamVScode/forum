package com.example.foruminforexchange.Specifications;

import com.example.foruminforexchange.model.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<User> isLocked(Long locked) {
        return (root, query, cb) -> {
            if (locked == null) {
                return cb.conjunction();
            }

            if (locked == 0) {
                return cb.equal(root.get("isLocked"), false);
            } else if (locked == 1) {
                return cb.equal(root.get("isLocked"), true);
            }

            return cb.conjunction();
        };
    }

    public static Specification<User> searchByKeyword(String searchKeyword) {
        return (root, query, cb) -> {
            if (searchKeyword == null) {
                return cb.conjunction();
            }

            String keyword = "%" + searchKeyword + "%";
            return cb.or(
                    cb.like(root.get("email"), keyword),
                    cb.like(root.get("firstName"), keyword),
                    cb.like(root.get("lastName"), keyword)
            );
        };
    }
}
