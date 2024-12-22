package ru.practicum.shareit.item;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemMapper {
    public ItemDto mapToDto(Item item) {
        ItemDto result = new ItemDto();
        result.setId(item.getId());
        result.setOwnerId(item.getOwnerId());
        result.setName(item.getName());
        result.setDescription(item.getDescription());
        result.setAvailable(item.getAvailable());
        return result;
    }

    public Item mapFromDto(ItemDto item) {
        Item result = new Item();
        result.setId(item.getId());
        result.setOwnerId(item.getOwnerId());
        result.setName(item.getName());
        result.setDescription(item.getDescription());
        result.setAvailable(item.getAvailable());
        return result;
    }
}