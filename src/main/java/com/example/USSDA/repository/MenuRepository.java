package com.example.USSDA.repository;
import com.example.USSDA.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
    // Find all submenus by parent ID
    List<Menu> findByParentId(int parentId);

    // Find menu ID by parent ID and action ID
    @Query("SELECT m.id FROM Menu m WHERE m.parentId = :currentMenu AND m.actionId = :input")
    Integer findMenuIdByParentIdAndActionId(@Param("currentMenu") Integer currentMenu, @Param("input") int input);



}