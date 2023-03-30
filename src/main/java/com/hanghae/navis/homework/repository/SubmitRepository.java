package com.hanghae.navis.homework.repository;

import com.hanghae.navis.group.entity.GroupMember;
import com.hanghae.navis.homework.dto.HomeworkSubmitListResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubmitRepository extends JpaRepository <GroupMember, Long> {
    @Query(value = "SELECT gm.user_id as userId, u.nickname as nickName, hs.submit as submit, GROUP_CONCAT(hsf.file_url SEPARATOR ', ') as fileUrl, GROUP_CONCAT(hsf.file_name SEPARATOR ', ') as fileName, hs.late as late, hs.created_at as createdAt, gm.group_role as role FROM group_member as gm " +
            "LEFT OUTER JOIN users u ON u.id = gm.user_id " +
            "LEFT OUTER JOIN basic_board bb ON bb.dtype = 'homework' AND bb.group_id = gm.group_id " +
            "LEFT OUTER JOIN homework_subject hs ON gm.user_id = hs.user_id and bb.id = hs.homework_id " +
            "LEFT OUTER JOIN homework_subject_file hsf ON hsf.homework_subject_id = hs.id " +
            "WHERE gm.group_id = :groupId and bb.id = :basicBoardId " +
            "GROUP BY gm.user_id", nativeQuery = true)
    List<HomeworkSubmitListResponseDto> findByGroupMember(@Param("groupId") Long groupId, @Param("basicBoardId") Long basicBoardId);
}
