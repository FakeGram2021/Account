package fakegram.adapter.http.dto;

import fakegram.domain.model.account.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchUserDto {

    private UUID userId;
    private String username;
    private String webUrl;
    private String biography;
    private String userAvatar;

    public static SearchUserDto from(User user) {
        return SearchUserDto
                .builder()
                .userId(user.getAccountId())
                .username(user.getUsername())
                .webUrl(user.getWebUrl())
                .biography(user.getBiography())
                .userAvatar(user.getAvatar())
                .build();
    }
}
