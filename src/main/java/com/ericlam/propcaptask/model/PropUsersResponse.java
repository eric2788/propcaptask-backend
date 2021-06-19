package com.ericlam.propcaptask.model;

import com.ericlam.propcaptask.dao.PropUser;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
public class PropUsersResponse {

    private final int size;
    private final int totalPages;
    private final Iterable<PropUserDetail> users;

    @Getter
    public static class PropUserDetail {

        private final String username;
        private final String email;
        private final Date registered;

        public PropUserDetail(PropUser propUser) {
            this.username = propUser.getUsername();
            this.email = propUser.getEmail();
            this.registered = propUser.getRegistered();
        }
    }

}
