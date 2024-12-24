package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findItemsByOwnerId(Integer ownerId);

    Item getItemByIdAndOwnerId(Integer id, Integer ownerId);

    @Query("select i" +
            " from Item i" +
            " where i.available and (upper(i.name) like upper(?1) or upper(i.description) like upper(?1))")
    List<Item> findByText(String text);
}
