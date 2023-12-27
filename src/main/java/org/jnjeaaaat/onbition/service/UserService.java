package org.jnjeaaaat.onbition.service;

import org.jnjeaaaat.onbition.domain.dto.user.UserModifyRequest;
import org.jnjeaaaat.onbition.domain.dto.user.UserModifyResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * 유저 정보 변경, 비밀번호 변경, 유저 조회 기능 Interface
 */
public interface UserService {

  // 프로필사진, 이름 변경
  UserModifyResponse updateUser(Long userId, MultipartFile image, UserModifyRequest request);

}
